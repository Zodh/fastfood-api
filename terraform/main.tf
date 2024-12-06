provider "aws" {
  region = var.aws_region
}

resource "kubernetes_secret" "fastfood_secret" {
  metadata {
    name = "fastfood-secret"
  }

  data = {
    POSTGRES_USER          = base64encode(var.postgres_user)
    POSTGRES_PASSWORD      = base64encode(var.postgres_password)
    FASTFOOD_MAIL_PASSWORD = base64encode(var.fastfood_mail_password)
  }

  type = "Opaque"
}

# Criar a VPC
resource "aws_vpc" "eks_vpc" {
  cidr_block = "10.0.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true
  tags = {
    Name = "eks-vpc"
  }
}

data "aws_availability_zones" "available" {
  state = "available"
}

# Criar uma subnet pública
resource "aws_subnet" "eks_public_subnet" {
  vpc_id                  = aws_vpc.eks_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "${var.aws_region}a"
  map_public_ip_on_launch = true
  tags = {
    Name = "eks-public-subnet"
  }
}

# Criar uma subnet privada
resource "aws_subnet" "eks_private_subnet" {
  vpc_id                  = aws_vpc.eks_vpc.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "${var.aws_region}b"
  map_public_ip_on_launch = true
  tags = {
    Name = "eks-private-subnet"
  }
}

# Criar uma segunda subnet privada em uma zona de disponibilidade diferente
resource "aws_subnet" "eks_private_subnet_2" {
  vpc_id                  = aws_vpc.eks_vpc.id
  cidr_block              = "10.0.3.0/24"  # Um novo bloco de IP
  availability_zone       = "${var.aws_region}c"
  map_public_ip_on_launch = true
  tags = {
    Name = "eks-private-subnet-2"
  }
}

# Criar o Internet Gateway para a VPC
resource "aws_internet_gateway" "eks_igw" {
  vpc_id = aws_vpc.eks_vpc.id
  tags = {
    Name = "eks-igw"
  }
}

# Criar a tabela de rotas para a subnet pública
resource "aws_route_table" "eks_public_route_table" {
  vpc_id = aws_vpc.eks_vpc.id
}

resource "aws_route" "public_route" {
  route_table_id         = aws_route_table.eks_public_route_table.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.eks_igw.id
}

# Associa a tabela de rotas com a subnet pública
resource "aws_route_table_association" "eks_public_subnet_association" {
  subnet_id      = aws_subnet.eks_public_subnet.id
  route_table_id = aws_route_table.eks_public_route_table.id
}

# Criar a tabela de rotas para a subnet privada (sem rotas públicas)
resource "aws_route_table" "eks_private_route_table" {
  vpc_id = aws_vpc.eks_vpc.id
}

resource "aws_route" "private_route" {
  route_table_id         = aws_route_table.eks_private_route_table.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id             = aws_internet_gateway.eks_igw.id
}

# Associa a tabela de rotas com a subnet privada
resource "aws_route_table_association" "eks_private_subnet_association" {
  subnet_id      = aws_subnet.eks_private_subnet.id
  route_table_id = aws_route_table.eks_private_route_table.id
}

resource "aws_route_table_association" "eks_private_subnet_association_2" {
  subnet_id      = aws_subnet.eks_private_subnet_2.id
  route_table_id = aws_route_table.eks_private_route_table.id
}

resource "aws_security_group" "eks_node_group_sg" {
  name        = "SG-${var.projectName}"
  description = "Security group for EKS node group"
  vpc_id      = aws_vpc.eks_vpc.id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Criar o cluster EKS
resource "aws_eks_cluster" "eks-cluster" {
  name     = var.projectName
  role_arn = var.labRole

  vpc_config {
    subnet_ids         = [aws_subnet.eks_public_subnet.id, aws_subnet.eks_private_subnet.id, aws_subnet.eks_private_subnet_2.id]
    security_group_ids = [aws_security_group.eks_node_group_sg.id]
  }
}

resource "aws_eks_node_group" "eks_node_group" {
  depends_on = [aws_eks_cluster.eks-cluster]

  cluster_name    = aws_eks_cluster.eks-cluster.name
  node_group_name = "eks-node-group-${var.projectName}"
  node_role_arn   = var.labRole
  subnet_ids      = [aws_subnet.eks_public_subnet.id, aws_subnet.eks_private_subnet.id, aws_subnet.eks_private_subnet_2.id]
  disk_size       = 20

  scaling_config {
    desired_size = 2
    min_size     = 1
    max_size     = 3
  }

  instance_types = ["t3.medium"]
  ami_type = "AL2_x86_64"
}
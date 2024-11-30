provider "aws" {
  region = var.aws_region
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
  tags = {
    Name = "eks-private-subnet"
  }
}

# Criar uma segunda subnet privada em uma zona de disponibilidade diferente
resource "aws_subnet" "eks_private_subnet_2" {
  vpc_id                  = aws_vpc.eks_vpc.id
  cidr_block              = "10.0.3.0/24"  # Um novo bloco de IP
  availability_zone       = "${var.aws_region}a"
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

# Associa a tabela de rotas com a subnet pública
resource "aws_route_table_association" "eks_public_subnet_association" {
  subnet_id      = aws_subnet.eks_public_subnet.id
  route_table_id = aws_route_table.eks_public_route_table.id
}

# Criar a tabela de rotas para a subnet privada (sem rotas públicas)
resource "aws_route_table" "eks_private_route_table" {
  vpc_id = aws_vpc.eks_vpc.id
}

# Associa a tabela de rotas com a subnet privada
resource "aws_route_table_association" "eks_private_subnet_association" {
  subnet_id      = aws_subnet.eks_private_subnet.id
  route_table_id = aws_route_table.eks_private_route_table.id
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

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_eks_access_policy_association" "eks-access-policy" {
  cluster_name  = aws_eks_cluster.eks-cluster.name
  policy_arn    = var.policyArn
  principal_arn = var.principalArn

  access_scope {
    type = "cluster"
  }
}

resource "aws_eks_access_entry" "eks-access-entry" {
  cluster_name      = aws_eks_cluster.eks-cluster.name
  principal_arn     = var.principalArn
  kubernetes_groups = ["fiap"]
  type              = "STANDARD"
}

# Criar o grupo de nós (worker nodes)
resource "aws_eks_node_group" "eks-node" {
  cluster_name    = aws_eks_cluster.eks-cluster.name
  node_group_name = var.nodeGroup
  node_role_arn   = var.labRole
  subnet_ids      = [aws_subnet.eks_private_subnet.id]
  disk_size       = 10
  instance_types  = [var.instanceType]

  scaling_config {
    desired_size = 2
    min_size     = 1
    max_size     = 3
  }

  update_config {
    max_unavailable = 1
  }
}
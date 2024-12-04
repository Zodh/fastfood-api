data "aws_eks_cluster" "cluster" {
  name = "fastfood-api"
}

data "aws_eks_cluster_auth" "cluster" {
  name = data.aws_eks_cluster.cluster.name
}

provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority[0].data)
  token                  = data.aws_eks_cluster_auth.cluster.token
}

data "aws_vpc" "eks_vpc" {
  filter {
    name   = "cidr"
    values = ["10.0.0.0/16"]
  }

  tags = {
    Name = "eks-vpc"
  }
}

data "aws_subnet" "eks_private_subnet" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.eks_vpc.id]
  }

  filter {
    name   = "cidrBlock"
    values = ["10.0.2.0/24"]
  }
}

data "aws_subnet" "eks_private_subnet2" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.eks_vpc.id]
  }

  filter {
    name   = "cidrBlock"
    values = ["10.0.3.0/24"]
  }
}

data "kubernetes_secret" "fastfood_secret" {
  metadata {
    name = "fastfood-secret"
  }
}

# Criar o RDS (por exemplo, um banco de dados MySQL)
resource "aws_db_instance" "rds" {
  identifier        = "mydb-instance"
  engine            = "postgres"
  instance_class    = "db.t3.micro"
  allocated_storage = 20
  db_name           = "postgres"
  username          = data.kubernetes_secret.fastfood_secret.data.POSTGRES_USER
  password          = data.kubernetes_secret.fastfood_secret.data.POSTGRES_PASSWORD
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  db_subnet_group_name = aws_db_subnet_group.rds_subnet_group.name
  multi_az          = false
  publicly_accessible = false
  tags = {
    Name = "MyRDSInstance"
  }

  # Provisionador para rodar o script init.sql
  provisioner "local-exec" {
    command = <<EOT
      PGPASSWORD="${data.kubernetes_secret.fastfood_secret.data.POSTGRES_PASSWORD}" psql \
        --host=${aws_db_instance.rds.endpoint} \
        --port=5432 \
        --username=${data.kubernetes_secret.fastfood_secret.data.POSTGRES_USER} \
        --dbname=postgres \
        --file=./init.sql
    EOT
  }
}

# Criar o DB Subnet Group para o RDS (usando a subnet privada)
resource "aws_db_subnet_group" "rds_subnet_group" {
  name       = "my-rds-subnet-group"
  subnet_ids = [data.aws_subnet.eks_private_subnet.id, data.aws_subnet.eks_private_subnet2.id]

  tags = {
    Name = "MyRDSSubnetGroup"
  }
}

# Criar o Security Group para o RDS
resource "aws_security_group" "rds_sg" {
  name        = "rds-sg"
  description = "Security group para RDS"
  vpc_id      = data.aws_vpc.eks_vpc.id

  # Regras de segurança (exemplo para permitir tráfego de dentro da VPC)
  egress {
    cidr_blocks = ["0.0.0.0/0"]
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
  }

  ingress {
    cidr_blocks = ["10.0.0.0/16"]  # Permitir acesso de dentro da VPC
    from_port   = 5432  # A porta do PostgreSQL
    to_port     = 5432  # A porta do PostgreSQL
    protocol    = "tcp"
  }
}
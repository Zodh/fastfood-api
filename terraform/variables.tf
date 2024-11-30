variable "aws_region" {
  description = "Região da AWS"
  type        = string
  default     = "us-east-1"
}

variable "cluster_name" {
  description = "Nome do cluster EKS"
  type        = string
  default     = "fastfood-cluster"
}

variable "regionDefault" {
  default = "us-east-1"
}

variable "instanceType" {
  default = "t3.micro"
}

variable "projectName" {
  default = "EKS-FIAP"
}

variable "labRole" {
  default = "arn:aws:iam::397142877541:role/LabRole"
}

variable "principalArn" {
  default = "arn:aws:iam::397142877541:role/voclabs"
}

variable "policyArn" {
  default = "arn:aws:eks::aws:cluster-access-policy/AmazonEKSClusterAdminPolicy"
}

variable "nodeGroup" {
  default = "fiap"
}
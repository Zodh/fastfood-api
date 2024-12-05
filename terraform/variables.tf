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

variable "projectName" {
  default = "fastfood-api"
}

variable "labRole" {
  default = "arn:aws:iam::397142877541:role/LabRole"
}

variable "postgres_user" {
  description = "Usuário do banco de dados"
  type        = string
  sensitive   = true
}

variable "postgres_password" {
  description = "Senha do banco de dados"
  type        = string
  sensitive   = true
}

variable "fastfood_mail_password" {
  description = "Senha do email"
  type        = string
  sensitive   = true
}
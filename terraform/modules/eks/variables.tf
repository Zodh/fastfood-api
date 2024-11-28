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

variable "desired_capacity" {
  default = 2
}

variable "min_capacity" {
  default = 1
}

variable "max_capacity" {
  default = 3
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

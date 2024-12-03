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
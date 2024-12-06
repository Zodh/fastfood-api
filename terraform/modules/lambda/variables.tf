variable "labRole" {
  default = "arn:aws:iam::397142877541:role/LabRole"
}

variable "aws_region" {
  description = "Região da AWS"
  type        = string
  default     = "us-east-1"
}
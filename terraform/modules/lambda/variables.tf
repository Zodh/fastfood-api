variable "labRole" {
  default = "arn:aws:iam::397142877541:role/LabRole"
}

variable "externalIp" {
  default = "http://a001b8f76ce2248af886da23244ce08e-7e90dc30a5cfd14a.elb.us-east-1.amazonaws.com:8080"
}

variable "aws_region" {
  description = "Região da AWS"
  type        = string
  default     = "us-east-1"
}
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

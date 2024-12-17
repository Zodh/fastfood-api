output "fastfood_api_url" {
  value = kubernetes_manifest.fastfood_service
  description = "Endpoint público do FastFood API."
}

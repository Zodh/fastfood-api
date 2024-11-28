data "aws_eks_cluster" "eks_cluster" {
  name = "fastfood-cluster"
}

data "aws_db_instance" "rds" {
  db_instance_identifier = "mydb-instance"
}

# Deployment do FastFood API
resource "kubernetes_manifest" "fastfood_deployment" {
  depends_on = [kubernetes_secret.fastfood_secret]  # Garante que o EKS seja provisionado antes do Kubernetes

  manifest = {
    apiVersion = "apps/v1"
    kind       = "Deployment"
    metadata = {
      name = "fastfood-api"
    }
    spec = {
      replicas = 2
      selector = {
        matchLabels = {
          app = "fastfood-api"
        }
      }
      template = {
        metadata = {
          labels = {
            app = "fastfood-api"
          }
        }
        spec = {
          containers = [{
            name  = "fastfood-api"
            image = "zodh/fastfood-api:latest"
            ports = [{
              containerPort = 8080
            }]
            env = [
              {
                name  = "FASTFOOD_DATABASE_URL"
                value = "jdbc:postgresql://${data.aws_db_instance.rds.endpoint}:5432/${data.aws_db_instance.rds.db_name}"
              },
              {
                name  = "FASTFOOD_API_PORT"
                value = "8080"
              },
              {
                name  = "FASTFOOD_API_ENVIRONMENT"
                value = "dev-local"
              },
              {
                name = "FASTFOOD_DATABASE_USER"
                valueFrom = {
                  secretKeyRef = {
                    name = kubernetes_secret.fastfood_secret.metadata[0].name
                    key  = "POSTGRES_USER"
                  }
                }
              },
              {
                name = "FASTFOOD_DATABASE_PASSWORD"
                valueFrom = {
                  secretKeyRef = {
                    name = kubernetes_secret.fastfood_secret.metadata[0].name
                    key  = "POSTGRES_PASSWORD"
                  }
                }
              },
              {
                name = "FASTFOOD_MAIL_PASSWORD"
                valueFrom = {
                  secretKeyRef = {
                    name = kubernetes_secret.fastfood_secret.metadata[0].name
                    key  = "FASTFOOD_MAIL_PASSWORD"
                  }
                }
              },
              {
                name  = "PAYMENT_API_URL"
                value = "http://payment-api:8081/payments"
              }
            ]
          }]
        }
      }
    }
  }
}

# Deploy do Service
resource "kubernetes_manifest" "fastfood_service" {
  depends_on = [kubernetes_secret.fastfood_secret]  # Garante que o EKS seja provisionado antes do Kubernetes

  manifest = {
    apiVersion = "v1"
    kind       = "Service"
    metadata = {
      name = "fastfood-api"
    }
    spec = {
      selector = {
        app = "fastfood-api"
      }
      ports = [{
        port       = 8080
        protocol   = "TCP"
        targetPort = 8080
        nodePort   = 32100
      }]
      type = "NodePort"
    }
  }
}

# Deploy do Horizontal Pod Autoscaler (HPA)
resource "kubernetes_manifest" "fastfood_hpa" {
  depends_on = [kubernetes_secret.fastfood_secret]  # Garante que o EKS seja provisionado antes do Kubernetes
  manifest = {
    apiVersion = "autoscaling/v2"
    kind       = "HorizontalPodAutoscaler"
    metadata = {
      name = "fastfood-api-hpa"
    }
    spec = {
      scaleTargetRef = {
        apiVersion = "apps/v1"
        kind       = "Deployment"
        name       = "fastfood-api"
      }
      minReplicas = 1
      maxReplicas = 5
      metrics = [{
        type = "Resource"
        resource = {
          name   = "cpu"
          target = {
            type                  = "Utilization"
            averageUtilization    = 70
          }
        }
      }]
    }
  }
}

resource "kubernetes_secret" "fastfood_secret" {
  metadata {
    name = "fastfood-secret"
  }

  data = {
    POSTGRES_USER          = base64encode(var.postgres_user)
    POSTGRES_PASSWORD      = base64encode(var.postgres_password)
    FASTFOOD_MAIL_PASSWORD = base64encode(var.fastfood_mail_password)
  }

  type = "Opaque"
}
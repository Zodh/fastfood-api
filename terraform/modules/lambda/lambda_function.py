import boto3
import requests
import os

# Inicializa o cliente EKS
eks_client = boto3.client("eks")

def get_eks_endpoint(cluster_name):
    """
    Retorna o endpoint de um cluster EKS.
    """
    try:
        response = eks_client.describe_cluster(name=cluster_name)
        return response["cluster"]["endpoint"]
    except Exception as e:
        print(f"Erro ao obter o endpoint do cluster EKS: {str(e)}")
        raise

def lambda_handler(event, context):
    cluster_name = os.getenv("CLUSTER_NAME", "default-cluster")
    eks_endpoint = get_eks_endpoint(cluster_name)

    # Extrair informações da solicitação do API Gateway
    path = event['path']  # Ex: "/user", "/product"
    http_method = event['httpMethod']  # Ex: "GET", "POST", "DELETE"
    body = event.get('body', None)  # Corpo da requisição (se aplicável)
    query_string = event.get('queryStringParameters', {})  # Parâmetros de query

    # Construir a URL completa para o EKS
    eks_url = f"{eks_endpoint}{path}"

    # Fazer a requisição para o EKS
    try:
        if http_method == "GET":
            response = requests.get(eks_url, params=query_string)
        elif http_method == "POST":
            response = requests.post(eks_url, json=body)
        elif http_method == "DELETE":
            response = requests.delete(eks_url, params=query_string)
        elif http_method == "PUT":
            response = requests.put(eks_url, json=body)
        else:
            return {
                "statusCode": 405,
                "body": "Method Not Allowed"
            }

        # Retornar a resposta da API no EKS
        return {
            "statusCode": response.status_code,
            "body": response.text
        }

    except Exception as e:
        print(f"Erro ao processar a solicitação: {str(e)}")
        return {
            "statusCode": 500,
            "body": f"Erro interno: {str(e)}"
        }

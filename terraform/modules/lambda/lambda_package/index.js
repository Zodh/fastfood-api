const axios = require('axios');
const https = require('https');
const { Client } = require('pg');

// Configuração para o banco RDS
const DB_HOST = process.env.DB_HOST || 'your-rds-endpoint.amazonaws.com';
const DB_NAME = process.env.DB_NAME || 'your_database_name';
const DB_USER = process.env.DB_USER || 'your_database_user';
const DB_PASSWORD = process.env.DB_PASSWORD || 'your_database_password';
const EXTERNAL_IP = process.env.EXTERNAL_IP || 'default-elb';

// Configuração do agente HTTPS para ignorar validação de SSL
const httpsAgent = new https.Agent({
    rejectUnauthorized: false, // Ignora validação de certificados
});

async function validateUserExists(cpf) {
    const client = new Client({
        host: DB_HOST,
        database: DB_NAME,
        user: DB_USER,
        password: DB_PASSWORD,
        ssl: {
            rejectUnauthorized: false, // Se você deseja ignorar a verificação do certificado SSL
        }
    });

    try {
        await client.connect();
        const query = 'SELECT COUNT(*) FROM users WHERE cpf = $1';
        const res = await client.query(query, [cpf]);
        return res.rows[0].count > 0;
    } catch (error) {
        console.error('Erro ao conectar ao banco de dados:', error);
        throw error;
    } finally {
        await client.end();
    }
}

exports.lambdaHandler = async (event) => {
    const eksEndpoint = EXTERNAL_IP;

    // Extrair informações da solicitação do API Gateway
    const path = event.path;
    const httpMethod = event.httpMethod;
    const body = event.body ? JSON.parse(event.body) : null;
    const queryString = event.queryStringParameters || {};

    // Construir a URL completa para o EKS
    const eksUrl = `${eksEndpoint}${path}`;

    if (httpMethod === 'DELETE') {
        const cpf = queryString.cpf;
        if (!cpf) {
            return {
                statusCode: 400,
                body: JSON.stringify({ message: 'CPF é obrigatório para DELETE' }),
            };
        }

        // Verifica no banco de dados se o usuário existe
        const userExists = await validateUserExists(cpf);
        if (!userExists) {
            return {
                statusCode: 401,
                body: JSON.stringify({ message: 'Usuário não autenticado ou não encontrado' }),
            };
        }
    }

    // Fazer a requisição para o EKS
    try {
        let response;
        switch (httpMethod) {
            case 'GET':
                response = await axios.get(eksUrl, { params: queryString, httpsAgent });
                break;
            case 'POST':
                response = await axios.post(eksUrl, body, { httpsAgent });
                break;
            case 'DELETE':
                response = await axios.delete(eksUrl, { params: queryString, httpsAgent });
                break;
            case 'PUT':
                response = await axios.put(eksUrl, body, { httpsAgent });
                break;
            default:
                return {
                    statusCode: 405,
                    body: JSON.stringify({ message: 'Method Not Allowed' }),
                };
        }

        // Retornar a resposta da API no EKS
        return {
            statusCode: response.status,
            body: JSON.stringify(response.data),
        };
    } catch (error) {
        const statusCode = error.response ? error.response.status : 500;
        const errorResponse = error.response ? error.response.data : { message: 'Erro inesperado' };

        console.error('Erro ao processar a solicitação:', error);
        return {
            statusCode: statusCode,
            body: JSON.stringify({
                message: `Erro ao processar a requisição: ${error.message}`,
                response: errorResponse
            }),
        };
    }
};

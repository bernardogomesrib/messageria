# Messageria

## Arquitetura Adotada

A aplicação é composta por múltiplos containers Docker que se comunicam entre si utilizando RabbitMQ para troca de mensagens. A arquitetura é dividida em quatro principais componentes:

1. **Ordenador**: Um container responsável por receber mensagens externas e enviá-las para uma fila no RabbitMQ.
2. **Processador**: Um container que consome mensagens de uma fila, processa os dados e os salva em um banco de dados.
3. **Notificador**: Um container que consome mensagens de uma fila de notificações e envia notificações via WebSocket para o frontend.
4. **Frontend**: Um container Angular que exibe as notificações em tempo real para os usuários.

### Diagrama da Arquitetura

```mermaid
graph TD
    A[Frontend] --> B[Backend Ordenador Springboot]
    B --> C[Fila de Pedidos RabbitMQ]
    C --> D[Backend Processador Springboot]
    D --> E[Banco de Dados PostgreSQL]
    D --> F[Fila de Notificações RabbitMQ]
    F --> G[Backend Notificador Springboot]
    G --> A[Frontend Angular]
```

---

## Descrição da Troca de Mensagens
1.  **Frontend** gera uma ordem de produto que manda para o **Ordenador**.
2.  **Ordenador** recebe mensagens do **Frontend** e as envia para a fila de pedidos da **Exchange**.
3.  **Exchange** recebe as ordens e coloca na fila de pedidos.
4.  **Processador** consome as mensagens da fila de pedidos e envia uma notificação para a fila de notificações da **Exchange**.
5.  **Notificador** consome mensagens da fila de notificações e utiliza WebSocket para enviar notificações em tempo real ao **Frontend**.
6.  **Frontend** exibe as notificações para os usuários e manda ack para o **Notificador** para retirar a notificação da fila.


---

## Estratégias de Tolerância a Falhas

- **Retries**: Implementação de tentativas automáticas em caso de falha no processamento das mensagens.
- - Se falhar ao salvar a requisição ela volta para a fila.
- - Se falhar ao enviar notificação ela volta para a fila.
- **Persistência**: As mensagens são persistidas no RabbitMQ até serem processadas com sucesso.
- **Logs**: Logs detalhados são gerados em cada container para monitoramento e depuração.
- **Isolamento de Containers**: Cada componente é executado em um container separado, garantindo que falhas em um não afetem os outros.

---

## Tecnologias Utilizadas

- **Docker v26.1.3**: Para orquestração e isolamento dos containers.
- **RabbitMQ v4.1.0**: Para troca de mensagens entre os componentes.
- **PostgreSQL v17.5**: Para armazenamento persistente dos dados processados.
- **WebSocket**: Para comunicação em tempo real entre o backend **Notificador** e o **Frontend**.
- **Angular v19.2.0**: Para desenvolvimento do frontend.
- **Java 21**: A linguagem utilizada nos containers backend.

---

## Como Executar

1. Certifique-se de ter o Docker e o Docker Compose instalados.
2. Clone este repositório.
3. crie um .env como apontado pelo .env.example
4. Execute `docker-compose up -d --build` para iniciar todos os containers.
5. Acesse o frontend no navegador com o ip que você informou no .env ou com localhost:4200 para fazer pedidos visualizar as notificações.

---



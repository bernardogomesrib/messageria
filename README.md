# Messageria - README

## Arquitetura Adotada

A aplicação é composta por múltiplos containers Docker que se comunicam entre si utilizando RabbitMQ para troca de mensagens. A arquitetura é dividida em quatro principais componentes:

1. **Producer**: Um container responsável por receber mensagens externas e enviá-las para uma fila no RabbitMQ.
2. **Processor**: Um container que consome mensagens de uma fila, processa os dados e os salva em um banco de dados.
3. **Notifier**: Um container que consome mensagens de uma fila de notificações e envia notificações via WebSocket para o frontend.
4. **Frontend**: Um container Angular que exibe as notificações em tempo real para os usuários.

### Diagrama da Arquitetura

```mermaid
graph TD
    A[Producer] -->|Envia mensagens| B[Fila no RabbitMQ]
    B -->|Consome mensagens| C[Processor]
    C -->|Salva dados| D[Banco de Dados]
    E[Notifier] -->|Consome notificações| F[Fila de Notificações]
    F -->|Notifica via WebSocket| G[Frontend (Angular)]
```

---

## Descrição da Troca de Mensagens

1. O **Producer** recebe mensagens externas (via API ou outro meio) e as envia para uma fila no RabbitMQ.
2. O **Processor** consome as mensagens da fila, realiza o processamento necessário e salva os dados no banco de dados.
3. O **Notifier** consome mensagens de uma fila de notificações e utiliza WebSocket para enviar notificações em tempo real ao **Frontend**.
4. O **Frontend** exibe as notificações para os usuários.

---

## Estratégias de Tolerância a Falhas

- **Retries**: Implementação de tentativas automáticas em caso de falha no processamento das mensagens.
- **Persistência**: As mensagens são persistidas no RabbitMQ até serem processadas com sucesso.
- **Logs**: Logs detalhados são gerados em cada container para monitoramento e depuração.
- **Isolamento de Containers**: Cada componente é executado em um container separado, garantindo que falhas em um não afetem os outros.

---

## Tecnologias Utilizadas

- **Docker**: Para orquestração e isolamento dos containers.
- **RabbitMQ**: Para troca de mensagens entre os componentes.
- **Banco de Dados**: Para armazenamento persistente dos dados processados.
- **WebSocket**: Para comunicação em tempo real entre o backend e o frontend.
- **Angular**: Para desenvolvimento do frontend.
- **Linguagem de Programação**: A linguagem utilizada nos containers backend pode variar (ex.: Python, Node.js, etc.).

---

## Como Executar

1. Certifique-se de ter o Docker e o Docker Compose instalados.
2. Clone este repositório.
3. crie um .env como apontado pelo .env.example
4. Execute `docker-compose up` para iniciar todos os containers.
5. Acesse o frontend no navegador para visualizar as notificações.

---



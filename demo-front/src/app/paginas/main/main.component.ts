import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Client, IMessage, Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Pedido } from '../../../util/types';
import { ToastrModule } from 'ngx-toastr';
import { ToastrService } from 'ngx-toastr';
declare var window: any;
@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
  standalone: true,
  imports: [FormsModule, ToastrModule],
})
export class MainComponent {
  private stompClient: any;
  pedido: Pedido = {
    id: 0,
    nome: '',
    itens: '',
    quantidade: 0,
    valor: 0,
  };

  pedidos: Pedido[] = []; // Para armazenar os pedidos recebidos do backend
  private ip = window.location.hostname.split(':')[0];
  constructor(private http: HttpClient, private toastr: ToastrService) {}

  onSubmit() {
    const url = `http://${this.ip}:8080/api/pedido/send`;
    this.http.post(url, this.pedido).subscribe({
      next: (response) => {
        console.log('Pedido enviado com sucesso:', response);
        this.limpar();
      },
      error: (error) => {
        console.error('Erro ao enviar o pedido:', error);
        this.toastr.error('Erro ao enviar o pedido', error.message);
      },
    });
  }

  limpar() {
    this.pedido = {
      id: 0,
      nome: '',
      itens: '',
      quantidade: 0,
      valor: 0,
    };
    console.log(this.pedidos);
  }

  ngOnInit() {
    this.carregarPedidos();
    this.conectarWebSocket();
  }

  carregarPedidos() {
    const url = `http://${this.ip}:8081/pedidos?page=0&size=20`;
    this.http.get<any>(url).subscribe({
      next: (response) => {
        this.pedidos = response.content || []; // Supondo que a resposta tenha um campo `content`
        console.log('Pedidos carregados:', this.pedidos);
      },
      error: (error) => {
        console.error('Erro ao carregar pedidos:', error);
        this.toastr.error('Erro ao carregar pedidos');
      },
    });
  }

  conectarWebSocket() {
    console.log('Tentando conectar ao WebSocket...');

    // Configuração do cliente Stomp
    this.stompClient = new Client({
      brokerURL: `ws://${this.ip}:8082/ws-notifications`, // URL WebSocket
      connectHeaders: {},
      debug: (str) => console.log(str), // Para depuração
      reconnectDelay: 3000, // Tempo de espera antes de tentar reconectar (em ms)
      heartbeatIncoming: 0, // Desabilita heartbeat de entrada
      heartbeatOutgoing: 20000, // Heartbeat de saída a cada 20 segundos
      webSocketFactory: () =>
        new SockJS(`http://${this.ip}:8082/ws-notifications`), // Usa SockJS
    });

    // Configurações de conexão
    this.stompClient.onConnect = (frame:any) => {
      console.log('Conectado ao WebSocket');
      this.stompClient.subscribe(
        '/topic/notifications',
        (message: IMessage) => {
          const pedido = JSON.parse(message.body);
          console.log('Notificação recebida:', pedido);
          this.pedidos.unshift(pedido); // Adiciona o pedido recebido ao início da lista
          this.toastr.success(
            `Pedido com id ${pedido.id} foi salvo com sucesso!`
          );
        }
      );
    };

    // Configurações de erro
    this.stompClient.onStompError = (frame: { headers: { [x: string]: any; }; body: any; }) => {
      console.error('Erro no STOMP:', frame.headers['message']);
      console.error('Detalhes:', frame.body);
    };

    this.stompClient.onWebSocketClose = () => {
      console.error('Conexão WebSocket foi fechada.');
    };

    this.stompClient.onWebSocketError = (error: any) => {
      console.error('Erro na conexão WebSocket:', error);
    };

    // Conecta ao WebSocket
    this.stompClient.activate();
  }
}


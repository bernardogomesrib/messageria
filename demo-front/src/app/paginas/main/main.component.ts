import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Stomp } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Pedido } from '../../../util/types';
import { ToastrModule } from 'ngx-toastr';
import { ToastrService } from 'ngx-toastr';
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

  constructor(private http: HttpClient, private toastr: ToastrService) {}

  onSubmit() {
    const url = 'http://192.168.0.25:8080/api/pedido/send';
    this.http.post(url, this.pedido).subscribe({
      next: (response) => {
        console.log('Pedido enviado com sucesso:', response);
        this.limpar();
      },
      error: (error) => {
        console.error('Erro ao enviar o pedido:', error);
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
    const url = 'http://192.168.0.25:8081/pedidos?page=0&size=20';
    this.http.get<any>(url).subscribe({
      next: (response) => {
        this.pedidos = response.content || []; // Supondo que a resposta tenha um campo `content`
        console.log('Pedidos carregados:', this.pedidos);
      },
      error: (error) => {
        console.error('Erro ao carregar pedidos:', error);
      },
    });
  }

  conectarWebSocket() {
    const socket = new SockJS('http://192.168.0.25:8082/ws-notifications');
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect(
      {},
      () => {
        console.log('Conectado ao WebSocket');
        this.stompClient.subscribe('/topic/notifications', (message: any) => {
          const pedido = JSON.parse(message.body);
          console.log('Notificação recebida:', pedido);
            this.pedidos.unshift(pedido); // Adiciona o pedido recebido ao início da lista
          this.toastr.success(`Pedido com id ${pedido.id} foi salvo com sucesso!`)
        });
      },
      (error: any) => {
        console.error('Erro na conexão WebSocket:', error);
      }
    );
  }
}


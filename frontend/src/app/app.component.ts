import { Component, OnInit } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import * as $ from 'jquery';
import { HttpClient } from '@angular/common/http';
declare var Logger: any;
declare var log: any;


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'app';

  private stompClient;
  private serverUrl = 'http://localhost:8080/socket'

  constructor(private http: HttpClient) {
    // this.initializeWebSocketConnection();
  }
  ngOnInit(): void {
    Logger.open();
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection() {
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function (frame) {
      that.stompClient.subscribe("/chat", (message) => {
        if (message.body) {
          log(message.body);
          console.log(message.body);
        }
      });
    });
  }

  sendMessage() {
    let data = $('#exampleTextarea').val();
    log(data);
    this.http.post('http://localhost:8080/send', {message:"asdf"}).subscribe(res => {
      log(res);
    });

  }

  toggleLogger() {
    Logger.toggle();
  }

  clearLogger() {
    Logger.clear();
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BASE_URL } from '../constants/constants';

@Injectable({
  providedIn: 'root',
})

export class ChatService {

  baseUrl = BASE_URL;

  constructor(private http: HttpClient) {}

  createChat(data: any) {
    return this.http.post(this.baseUrl + '/messages', data);
  }

  getChatMessagesList() {
    return this.http.get(this.baseUrl + '/messages');
  }
}

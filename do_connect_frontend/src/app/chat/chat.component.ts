import { Component, OnDestroy, OnInit } from '@angular/core';
import { ChatService } from '../service/chat.service';
import { getCurrentUsername, isUserAdmin } from '../utils/util';

const CHAT_REFRESH_FRQUENCY_IN_SECONDS = 5;

interface ChatMessage {
  message: string;
  postedBy: string;
  postedAt: Date;
}

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})

export class ChatComponent implements OnInit, OnDestroy {

  message: string;
  chatMessageList: ChatMessage[];
  isUserAdmin: boolean;
  interval: number | null = null;
  currentUsername?: string = getCurrentUsername();

  constructor(private _chatservice: ChatService) {
    this.message = '';
    this.chatMessageList = [];
    this.isUserAdmin = isUserAdmin();
  }

  ngOnInit(): void {
    if (!this.isUserAdmin) {
      this.fetchChatMessages();
      this.interval = window.setInterval(() => {
        this.fetchChatMessages();
      }, CHAT_REFRESH_FRQUENCY_IN_SECONDS * 1000);
    }
  }

  ngOnDestroy(): void {
    if (!this.isUserAdmin && this.interval) {
      clearInterval(this.interval);
      this.interval = null;
    }
  }

  fetchChatMessages() {
    this._chatservice.getChatMessagesList().subscribe({
      next: (results) => (this.chatMessageList = results as ChatMessage[]),
      error: (error) => console.error('Error while fetching chat messages list. Details: ', error),
    });
  }

  onSendMessage() {
    this._chatservice.createChat({ message: this.message }).subscribe({
      next: (results) => {
        this.message = '';
        this.chatMessageList = results as ChatMessage[];
      },
      error: (error) => console.error('Error while sending chat message. Details: ', error),
    });
  }
}

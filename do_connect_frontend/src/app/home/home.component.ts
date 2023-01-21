import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { QUESTIONS_TOPICS, QuestionType } from '../constants/constants';
import { UserService } from '../service/user.service';
import { handleErrorResponse, isUserAdmin, isUserLoggedIn } from '../utils/util';

const OPEN_CHAT_BUTTON_LABEL = 'Chat';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})

export class HomeComponent implements OnInit {

  questionList: QuestionType[] = [];
  search: string = '';
  topic: string = 'All';
  topicOptions: string[] = [ 'All', ...QUESTIONS_TOPICS ];
  chatbox = 'none';
  isAdmin: boolean = isUserAdmin();
  isUserLoggedIn: boolean = isUserLoggedIn();
  chatButton: string = OPEN_CHAT_BUTTON_LABEL;

  constructor(private _userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this._userService.getApprovedQuestions().subscribe({
      next: (result) => (this.questionList = result as QuestionType[]),
      error: (error: HttpErrorResponse) => handleErrorResponse(error, this.router),
    });
  }

  toggleChatBox() {
    this.chatbox = this.chatbox === 'block' ? 'none' : 'block';
  }

  openQuestion(id: number) {
    this.router.navigate([`/question/${id}`]);
  }

  searchHelper(query: string, topic: string) {
    this._userService.searchQuestion(query, (topic === 'All') ? '' : topic).subscribe({
      next: (result) => (this.questionList = result as QuestionType[]),
      error: (error: HttpErrorResponse) => handleErrorResponse(error, this.router),
    });
  }

  onSearch() {
    this.searchHelper(this.search, (this.topic === 'All') ? '' : this.topic);
  }

  onTopicSelected(value: string) {
    this.topic = value;
    this.search = '';
    this.searchHelper(this.search, (this.topic === 'All') ? '' : this.topic);
  }
}

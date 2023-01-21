import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ConditionalExpr } from '@angular/compiler';
import { Injectable } from '@angular/core';
import { AnswerPostType, BASE_URL, QuestionPostType, UserLoginType, UserRegisterType } from '../constants/constants';

@Injectable({
  providedIn: 'root',
})

export class UserService {

  constructor(private http: HttpClient) {}

  register(user: UserRegisterType) {
    return this.http.post(`${BASE_URL}/register`, user, { responseType: 'text' });
  }

  login(user: UserLoginType) {
    return this.http.post(`${BASE_URL}/login`, user);
  }

  getQuestion(id: number) {
    return this.http.get(BASE_URL + '/questions/' + id);
  }

  getApprovedQuestions() {
    return this.http.get(BASE_URL + '/questions');
  }

  postQuestion(question: QuestionPostType) {
    return this.http.post(BASE_URL + '/questions', question);
  }

  postAnswer(questionId: number, answer: AnswerPostType) {
    return this.http.post(BASE_URL + '/questions/' + questionId + '/answers', answer);
  }

  searchQuestion(query: string, topic: string) {
    return this.http.get(`${BASE_URL}/questions?search=${query}&topic=${topic}`);
  }

  getAnswers(quesId: number) {
    return this.http.get(BASE_URL + '/questions/' + quesId + '/answers');
  }

  logout() {
    return this.http.get(BASE_URL + '/signout', { responseType: 'text' });
  }
}

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AnswerType, BASE_URL } from '../constants/constants';

@Injectable({
  providedIn: 'root',
})

export class AdminService {

  constructor(private http: HttpClient) {}

  getUnapprovedQuestions() {
    return this.http.get(BASE_URL + '/questions?status=unapproved');
  }

  getUnapprovedAnswers() {
    return this.http.get(BASE_URL + '/answers?status=unapproved');
  }

  getUsers() {
    return this.http.get(BASE_URL + '/users');
  }

  deleteUser(id: number) {
    return this.http.delete(BASE_URL + '/users/' + id, { responseType: 'text' });
  }

  approveQuestion(id: number) {
    return this.http.put(BASE_URL + '/questions/' + id, { isApproved: true });
  }

  approveAnswer(answer: AnswerType) {
    return this.http.put(BASE_URL + '/questions/' + answer.question.id + '/answers/' + answer.id, { isApproved: true });
  }

  deleteQuestion(id: number) {
    return this.http.delete(BASE_URL + '/questions/' + id, { responseType: 'text' });
  }

  deleteAnswer(answer: AnswerType) {
    return this.http.delete(BASE_URL + '/questions/' + answer.question.id + '/answers/' + answer.id, { responseType: 'text' });
  }

}

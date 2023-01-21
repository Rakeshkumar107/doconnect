import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AnswerType, QuestionType, UserType } from '../constants/constants';
import { AdminService } from '../service/admin.service';
import { handleErrorResponse } from '../utils/util';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})

export class DashboardComponent implements OnInit {

  mode = 'questions';
  questionList: QuestionType[] = [];
  answerList: AnswerType[] = [];
  userList: UserType[] = [];

  constructor(private _adminService: AdminService, private router: Router) {}

  ngOnInit(): void {
    this.getUnapprovedQuestions();
  }

  getUnapprovedQuestions() {
    this.mode = 'questions';
    this._adminService.getUnapprovedQuestions().subscribe({
      next: (result) => (this.questionList = result as QuestionType[]),
      error: (err) => handleErrorResponse(err, this.router),
    });
  }

  getUnapprovedAnswers() {
    this.mode = 'answers';
    this._adminService.getUnapprovedAnswers().subscribe({
      next: (result) => (this.answerList = result as AnswerType[]),
      error: (err) => handleErrorResponse(err, this.router),
    });
  }

  getUsers() {
    this.mode = 'users';
    this._adminService.getUsers().subscribe({
      next: (result) => (this.userList = result as UserType[]),
      error: (err) => handleErrorResponse(err, this.router),
    });
  }

  onDelete(id: number) {
    if (confirm('Are you sure you want to remove this user? This action cannot be reverted.')) {
      this._adminService.deleteUser(id).subscribe({
        next: (res) => this.getUsers(),
        error: (err) => handleErrorResponse(err, this.router),
      });
    }
  }

  onApproveQuestion(id: number) {
    this._adminService.approveQuestion(id).subscribe({
      next: (res) => this.getUnapprovedQuestions(),
      error: (err) => handleErrorResponse(err, this.router),
    });
  }

  onDeleteQuestion(id: number) {
    if (confirm('Are you sure? This action cannot be reverted.')) {
      this._adminService.deleteQuestion(id).subscribe({
        next: (res) => this.getUnapprovedQuestions(),
        error: (err) => handleErrorResponse(err, this.router),
      });
    }
  }

  onApproveAnswer(answer: AnswerType) {
    this._adminService.approveAnswer(answer).subscribe({
      next: (res) => this.getUnapprovedAnswers(),
      error: (err) => handleErrorResponse(err, this.router),
    });
  }

  onDeleteAnswer(answer: AnswerType) {
    if (confirm('Are you sure? This action cannot be reverted.')) {
      this._adminService.deleteAnswer(answer).subscribe({
        next: (res) => this.getUnapprovedAnswers(),
        error: (err) => handleErrorResponse(err, this.router),
      });
    }
  }
}

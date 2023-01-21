import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { AnswerType, BASE_URL, QuestionType } from '../constants/constants';
import { UploadImageService } from '../service/upload-image.service';
import { UserService } from '../service/user.service';
import { handleErrorResponse } from '../utils/util';

@Component({
  selector: 'app-question',
  templateUrl: './question.component.html',
  styleUrls: ['./question.component.css'],
})
export class QuestionComponent implements OnInit {
  public answerForm !: FormGroup;
  questionData: QuestionType | undefined = undefined;
  answersList: AnswerType[] = [];
  questionId: number | undefined;
  uploadedImages: string[] = [];

  constructor(private _userService: UserService, private activatedRoute: ActivatedRoute, private router: Router, private _uploadService: UploadImageService) {}

  ngOnInit(): void {
    this.answerForm = new FormGroup({
      answer: new FormControl('', [Validators.required]),
      images: new FormControl('')
    })
    this.getData();
  }

  getData() {
    const urlParams = this.activatedRoute.snapshot.params;
    this.questionId = urlParams['id'];
    if (this.questionId) {
      this._userService.getQuestion(this.questionId).subscribe({
        next: (result) => (this.questionData = result as QuestionType),
        error: (error) => handleErrorResponse(error, this.router),
      });
      this._userService.getAnswers(this.questionId).subscribe({
        next: (result) => (this.answersList = result as AnswerType[]),
        error: (error) => handleErrorResponse(error, this.router),
      });
    }
  }

  onChange(event: any) {
    const imageFile = event.target.files[0];
    if (imageFile) {
      this._uploadService.uploadImage(imageFile).subscribe({
        next: (result) => {
          this.uploadedImages.push(result);
        },
        error: (error: HttpErrorResponse) => handleErrorResponse(error, this.router),
      });
    }
  }

  getImageUrl(imageName: string) {
    return `${BASE_URL}/images/${imageName}`;
  }

  submit() {
    if (this.questionId) {
      this._userService
      .postAnswer(this.questionId, {
        answer: this.answerForm.value.answer,
        images: this.uploadedImages,
      })
      .subscribe({
        next: (result) => {
          alert('Your answer submission was successful.');
          this.router.navigate(['/']);
        },
        error: (error) => handleErrorResponse(error, this.router),
      });
    }
  }
}

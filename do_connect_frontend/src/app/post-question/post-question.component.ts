import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BASE_URL, QUESTIONS_TOPICS } from '../constants/constants';
import { UploadImageService } from '../service/upload-image.service';
import { UserService } from '../service/user.service';
import { handleErrorResponse } from '../utils/util';

@Component({
  selector: 'app-post-question',
  templateUrl: './post-question.component.html',
  styleUrls: ['./post-question.component.css'],
})

export class PostQuestionComponent implements OnInit {
  public questionForm !: FormGroup;
  topicOptions: string[] = QUESTIONS_TOPICS;
  uploadedImages: string[] = [];

  constructor(private _uploadService: UploadImageService, private _userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.questionForm = new FormGroup({
      question: new FormControl('', [Validators.required]),
      topic: new FormControl(QUESTIONS_TOPICS[0], [Validators.required]),
      images: new FormControl('')
    });
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
    this._userService
      .postQuestion({
        question: this.questionForm.value.question,
        topic: this.questionForm.value.topic,
        images: this.uploadedImages,
      })
      .subscribe({
        next: (result) => {
          alert('Your question submission was successful.');
          this.router.navigate(['/']);
        },
        error: (error) => handleErrorResponse(error, this.router),
      });
  }

}

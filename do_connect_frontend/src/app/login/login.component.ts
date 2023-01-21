import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import jwtDecode from 'jwt-decode';
import { JWTCustomPayload } from '../constants/constants';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  public loginForm !: FormGroup;

  constructor(private router: Router, private _userService: UserService) {}

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)])
    });
  }

  login() {
    this._userService
      .login({ username: this.loginForm.value.username, password: this.loginForm.value.password })
      .subscribe({
        next: (result: any) => {
          const token: string = result.token;
          const jwtData: JWTCustomPayload = jwtDecode(token);
          localStorage.setItem('token', token);
          if (jwtData.isAdmin) {
            this.router.navigate(['/dashboard']);
          } else {
            this.router.navigate(['/']);
          }
        },
        error: (error: HttpErrorResponse) => {
          alert('Invalid Credentials. Please try again.');
        },
      });
  }
}
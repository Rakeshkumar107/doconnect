import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { HttpErrorResponse } from '@angular/common/http';
import { handleErrorResponse } from '../utils/util';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})

export class RegisterComponent implements OnInit {
  public registerForm !: FormGroup;

  constructor(private _userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.registerForm = new FormGroup({
      username: new FormControl('', [Validators.required]),
      name: new FormControl('', [Validators.required]),
      email: new FormControl('', [
          Validators.required,
          Validators.pattern("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"),
      ]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)]),
      role: new FormControl('user', [Validators.required]),
  });
  }

  register() {
    let isAdmin = this.registerForm.value.role === 'user' ? false : true;
    this._userService
      .register({
        name: this.registerForm.value.name,
        email: this.registerForm.value.email,
        password: this.registerForm.value.password,
        username: this.registerForm.value.username,
        isAdmin: isAdmin,
      })
      .subscribe({
        next: (result) => {
          alert(result);
          this.router.navigate(['/login']);
        },
        error: (error: HttpErrorResponse) => handleErrorResponse(error, this.router),
      });
  }
}

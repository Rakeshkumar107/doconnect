import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../service/user.service';
import { getCurrentUsername, handleErrorResponse, isUserAdmin, isUserLoggedIn } from '../utils/util';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {

  isAdmin = isUserAdmin();
  isLoggedIn = isUserLoggedIn();
  username = getCurrentUsername();

  constructor(private _userService: UserService, private router: Router) {}

  ngOnInit(): void {
    this.router.events.subscribe(event => {
      if (event.constructor.name === "NavigationEnd") {
        this.isAdmin = isUserAdmin();
        this.isLoggedIn = isUserLoggedIn();
        this.username = getCurrentUsername();
      }
    });
  }

  onLogout() {
    this._userService.logout().subscribe({
      next: (result) => {
        localStorage.clear();
        alert(result);
        this.router.navigate(['/login']);
      },
      error: (error: HttpErrorResponse) => handleErrorResponse(error, this.router),
    });
  }
}

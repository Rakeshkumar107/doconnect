import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { isUserAdmin, isUserLoggedIn } from './util';

function getResolvedUrl(route: ActivatedRouteSnapshot): string {
  return route.pathFromRoot
    .map(v => v.url.map(segment => segment.toString()).join('/'))
    .join('/');
}

@Injectable({
  providedIn: 'root'
})

export class AuthGuard implements CanActivate {

  constructor(public router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const url = getResolvedUrl(route);
    if (isUserLoggedIn()) {
      if (['/register', '/login'].includes(url)) {
        if (isUserAdmin()) {
          this.router.navigate(['dashboard']);
          return false;
        }
        else {
          this.router.navigate(['']);
          return false;
        }
      }
      else if (['/', '/post-question'].includes(url) || url.startsWith('/question/')) {
        if (isUserAdmin()) {
          this.router.navigate(['dashboard']);
          return false;
        }
        else {
          return true;
        }
      }
      else if (['/dashboard'].includes(url)) {
        if (isUserAdmin()) {
          return true;
        }
        else {
          this.router.navigate(['']);
          return false;
        }
      }
      else {
        return true;
      }
    }
    else {
      if (['/dashboard', '/post-question'].includes(url) || url.startsWith('/question/')) {
        this.router.navigate(['']);
        return false;
      }
      else {
        return true;
      }
    }
  }
}

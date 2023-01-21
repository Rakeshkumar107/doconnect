import { HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import jwtDecode from 'jwt-decode';
import { JWTCustomPayload } from '../constants/constants';

export const isUserLoggedIn = () => {
  const token = localStorage.getItem('token');
  return (token !== null && token !== '' && token.split('.').length === 3);
};

export const isUserAdmin = () => {
  if (isUserLoggedIn()) {
    const token = localStorage.getItem('token');
    if (token) {
      const jwtData: JWTCustomPayload = jwtDecode(token);
      return jwtData.isAdmin;
    }
    return false;
  }
  return false;
};

export const getCurrentUsername = () => {
  if (isUserLoggedIn()) {
    const token = localStorage.getItem('token');
    if (token) {
      const jwtData: JWTCustomPayload = jwtDecode(token);
      return jwtData.sub;
    }
    return undefined;
  }
  return undefined;
};

export const handleErrorResponse = (error: HttpErrorResponse, router: Router) => {
  if (error.status === 400) {
    if (error?.error) {
      alert(error.error);
    }
    return;
  }
  else if (error.status === 401) {
    if (error?.error) {
      alert(error.error);
    }
    router.navigate(['login']);
    return;
  }
  else if (error.status === 403) {
    if (error?.error) {
      alert(error.error);
    }
    router.navigate(['unauthorized']);
    return;
  }
  else {
    if (error?.error) {
      alert(error.error);
    }
    router.navigate(['page-not-found']);
  }
};

import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BASE_URL } from '../constants/constants';

@Injectable({
  providedIn: 'root',
})

export class SearchService {
  searchActive = 'none';

  constructor(private http:HttpClient) {}

  onOpen(data:any) {
    return this.http.get(`${BASE_URL}/questions?search=${data}`);
  }
}

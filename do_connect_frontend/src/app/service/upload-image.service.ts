import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { BASE_URL } from '../constants/constants';

@Injectable({
  providedIn: 'root',
})

export class UploadImageService {

  constructor(private http: HttpClient, private sanitizer: DomSanitizer) {}

  uploadImage(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('image', file); // Store form name as "file" with file data
    return this.http.post(`${BASE_URL}/images`, formData, { responseType: 'text' }); // Make http post request over api with formData as req
  }

}

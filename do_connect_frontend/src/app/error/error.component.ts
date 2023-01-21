import { Component, OnInit } from '@angular/core';
import { isUserAdmin } from '../utils/util';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css'],
})
export class ErrorComponent implements OnInit {

  isUserAdmin = isUserAdmin();

  constructor() {}

  ngOnInit(): void {}

}

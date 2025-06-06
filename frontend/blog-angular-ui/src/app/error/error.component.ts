import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class ErrorComponent implements OnInit {

  errorMessage = 'An Error Occurred! ';

  constructor() {
  }

  ngOnInit(): void {
  }

}

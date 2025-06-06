import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {CommonModule, TitleCasePipe} from '@angular/common';
import {PingResponse, WelcomeDataService} from '../service/data/welcome-data.service';

const newLocal = 'name';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
  standalone: true,
  imports: [CommonModule, TitleCasePipe]
})
export class WelcomeComponent implements OnInit {

  name = '';
  welcomeMessage = '';

  // inject ActivatedRoute
  constructor(
    private route: ActivatedRoute,
    private helloWorldService: WelcomeDataService
  ) {
  }

  ngOnInit(): void {
    this.name = this.route.snapshot.params[newLocal];
  }

  getWelcomeMessage() {
    // to call any service we should do it asynchronously
    this.helloWorldService.executeHelloWorldBeanService().subscribe(
      response => this.handleSuccessFulResponse(response),
      error => this.handleErrorResponse(error)
    );
  }

  handleSuccessFulResponse(response: PingResponse): void {
    this.welcomeMessage = response.message;
  }

  handleErrorResponse(error: unknown): void {
    // Type assertion to handle the unknown type
    if (this.isHttpErrorResponse(error)) {
      this.welcomeMessage = error.error?.message || 'An error occurred';
    } else {
      this.welcomeMessage = 'An unexpected error occurred';
    }
  }

  private isHttpErrorResponse(error: unknown): error is { error: { message: string } } {
    return error !== null &&
      typeof error === 'object' &&
      'error' in error &&
      error.error !== null &&
      typeof error.error === 'object' &&
      'message' in error.error;
  }

}

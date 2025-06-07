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
    this.name = this.route.snapshot.params[newLocal] || '';
  }

  getWelcomeMessage() {
    // First try with name parameter
    this.helloWorldService.executeHelloWorldBeanServiceWithPathVariable(this.name).subscribe({
      next: (response) => this.handleSuccessFulResponse(response),
      error: (error) => {
        console.log('Error with path variable, falling back to basic service:', error);
        // Fall back to the basic service if the personalized one fails
        this.helloWorldService.executeHelloWorldBeanService().subscribe({
          next: (response) => this.handleSuccessFulResponse(response),
          error: (fallbackError) => this.handleErrorResponse(fallbackError)
        });
      }
    });
  }

  handleSuccessFulResponse(response: PingResponse): void {
    this.welcomeMessage = response.message;
  }

  handleErrorResponse(error: unknown): void {
    console.error('Error fetching welcome message:', error);
    // Type assertion to handle the unknown type
    if (this.isHttpErrorResponse(error)) {
      this.welcomeMessage = `Error: ${error.error?.message || 'Server error occurred'}`;
    } else {
      this.welcomeMessage = 'An unexpected error occurred. Please check if the backend server is running.';
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

import { Component, ChangeDetectionStrategy, inject, signal, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TitleCasePipe } from '@angular/common';
import { PingResponse, WelcomeDataService } from '../service/data/welcome-data.service';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [TitleCasePipe]
})
export class WelcomeComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly helloWorldService = inject(WelcomeDataService);

  readonly name = signal('');
  readonly welcomeMessage = signal('');

  ngOnInit(): void {
    const nameParam = this.route.snapshot.params['name'] || '';
    this.name.set(nameParam);
  }

  getWelcomeMessage(): void {
    this.helloWorldService.executeHelloWorldBeanServiceWithPathVariable(this.name()).subscribe({
      next: response => this.handleSuccessfulResponse(response),
      error: error => {
        console.log('Error with path variable, falling back to basic service:', error);
        this.helloWorldService.executeHelloWorldBeanService().subscribe({
          next: response => this.handleSuccessfulResponse(response),
          error: fallbackError => this.handleErrorResponse(fallbackError)
        });
      }
    });
  }

  private handleSuccessfulResponse(response: PingResponse): void {
    this.welcomeMessage.set(response.message);
  }

  private handleErrorResponse(error: unknown): void {
    console.error('Error fetching welcome message:', error);
    if (this.isHttpErrorResponse(error)) {
      this.welcomeMessage.set(`Error: ${error.error?.message || 'Server error occurred'}`);
    } else {
      this.welcomeMessage.set(
        'An unexpected error occurred. Please check if the backend server is running.'
      );
    }
  }

  private isHttpErrorResponse(error: unknown): error is { error: { message: string } } {
    return (
      error !== null &&
      typeof error === 'object' &&
      'error' in error &&
      error.error !== null &&
      typeof error.error === 'object' &&
      'message' in error.error
    );
  }
}

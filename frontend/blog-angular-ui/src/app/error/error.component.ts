import { Component, ChangeDetectionStrategy, signal } from '@angular/core';


@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.css'],
  imports: []
})
export class ErrorComponent {
  readonly errorMessage = signal('An Error Occurred!');
}

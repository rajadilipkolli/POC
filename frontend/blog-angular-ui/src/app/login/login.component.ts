import { Component, ChangeDetectionStrategy, inject, signal, computed } from '@angular/core';
import { Router } from '@angular/router';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { HardcodedAuthenticationService } from '../service/hardcoded-authentication.service';
import { BasicAuthenticationService } from '../service/basic-authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule]
})
export class LoginComponent {
  private readonly router = inject(Router);
  private readonly formBuilder = inject(FormBuilder);
  private readonly hardcodedAuthenticationService = inject(HardcodedAuthenticationService);
  private readonly basicAuthenticationService = inject(BasicAuthenticationService);

  // Signals for reactive state management
  readonly isLoading = signal(false);
  readonly invalidLogin = signal(false);
  readonly errorMessage = signal('Invalid Credentials');

  // Form state signals
  private readonly formValueChanges = signal<Record<string, unknown> | null>(null);
  private readonly formStatusChanges = signal<string>('VALID');

  // Reactive form
  readonly loginForm = this.formBuilder.group({
    username: ['username', [Validators.required, Validators.minLength(3)]],
    password: ['dummy', [Validators.required, Validators.minLength(3)]]
  });

  // Computed values using signals that react to form changes
  readonly isFormValid = computed(() => {
    // Trigger recomputation when form status changes
    this.formStatusChanges();
    return this.loginForm.valid;
  });

  readonly usernameError = computed(() => {
    // Trigger recomputation when form value changes
    this.formValueChanges();
    const control = this.loginForm.get('username');
    return control?.touched && control?.errors ? 'Username is required (min 3 characters)' : null;
  });

  readonly passwordError = computed(() => {
    // Trigger recomputation when form value changes
    this.formValueChanges();
    const control = this.loginForm.get('password');
    return control?.touched && control?.errors ? 'Password is required (min 3 characters)' : null;
  });

  constructor() {
    // Subscribe to form changes and update signals
    this.loginForm.valueChanges.subscribe(value => {
      this.formValueChanges.set(value);
    });

    this.loginForm.statusChanges.subscribe(status => {
      this.formStatusChanges.set(status);
    });
  }
  handleLogin(): void {
    if (!this.isFormValid()) {
      this.markFormGroupTouched();
      return;
    }

    const { username, password } = this.loginForm.value;

    if (this.hardcodedAuthenticationService.authenticate(username!, password!)) {
      this.router.navigate(['welcome', username]);
      this.invalidLogin.set(false);
    } else {
      this.invalidLogin.set(true);
    }
  }

  handleBasicAuthLogin(): void {
    if (!this.isFormValid()) {
      this.markFormGroupTouched();
      return;
    }

    const { username, password } = this.loginForm.value;
    this.isLoading.set(true);
    this.invalidLogin.set(false);

    this.basicAuthenticationService.executeAuthenticationService(username!, password!).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate(['welcome', username]);
        this.invalidLogin.set(false);
      },
      error: error => {
        console.error('Authentication failed:', error);
        this.isLoading.set(false);
        this.invalidLogin.set(true);
      }
    });
  }

  private markFormGroupTouched(): void {
    Object.keys(this.loginForm.controls).forEach(key => {
      this.loginForm.get(key)?.markAsTouched();
    });
  }
}

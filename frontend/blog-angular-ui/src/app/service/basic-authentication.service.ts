import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { API_URL } from '../app.constants';

export const TOKEN = 'token';
export const AUTHENTICATED_USER = 'authenticatedUser';

export interface PingResponse {
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class BasicAuthenticationService {
  private readonly httpClient = inject(HttpClient);

  // Initialize signals with current session storage values
  private readonly authenticatedUser = signal<string | null>(this.getStoredUser());

  private readonly token = signal<string | null>(this.getStoredToken());

  // Computed values
  readonly isUserLoggedIn = computed(() => {
    const user = this.authenticatedUser();
    return user !== null && user !== '' && user !== 'null';
  });
  readonly currentUser = computed(() => this.authenticatedUser());
  readonly authToken = computed(() => this.token() || '');

  private getStoredUser(): string | null {
    const user = sessionStorage.getItem(AUTHENTICATED_USER);
    return user === 'null' ? null : user;
  }

  private getStoredToken(): string | null {
    const token = sessionStorage.getItem(TOKEN);
    return token === 'null' ? null : token;
  }

  private updateSignalsFromStorage(): void {
    this.authenticatedUser.set(this.getStoredUser());
    this.token.set(this.getStoredToken());
  }

  executeAuthenticationService(username: string, password: string) {
    const basicAuthHeaderString = 'Basic ' + window.btoa(username + ':' + password);

    const headers = new HttpHeaders({
      Authorization: basicAuthHeaderString
    });

    return this.httpClient.get<PingResponse>(`${API_URL}/pingWithAuthentication`, { headers }).pipe(
      map(data => {
        console.log(`inside basic authentication ${username} & token ${basicAuthHeaderString}`);
        sessionStorage.setItem(AUTHENTICATED_USER, username);
        sessionStorage.setItem(TOKEN, basicAuthHeaderString);

        // Update signals
        this.authenticatedUser.set(username);
        this.token.set(basicAuthHeaderString);

        return data;
      })
    );
  }

  getAuthenticatedUser(): string | null {
    return this.authenticatedUser();
  }

  getAuthenticatedToken(): string {
    return this.authToken();
  }

  logout(): void {
    sessionStorage.removeItem(AUTHENTICATED_USER);
    sessionStorage.removeItem(TOKEN);

    // Update signals
    this.authenticatedUser.set(null);
    this.token.set(null);
  }

  // Method to refresh signals from storage (for testing)
  refreshFromStorage(): void {
    this.updateSignalsFromStorage();
  }
}

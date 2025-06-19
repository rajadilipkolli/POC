import { Injectable, signal, computed } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HardcodedAuthenticationService {
  private readonly authenticatedUser = signal<string | null>(
    sessionStorage.getItem('authenticatedUser')
  );

  readonly isUserLoggedIn = computed(() => this.authenticatedUser() !== null);
  readonly currentUser = computed(() => this.authenticatedUser());

  authenticate(userName: string, passCode: string): boolean {
    if (userName === 'username' && passCode === 'dummy') {
      sessionStorage.setItem('authenticatedUser', userName);
      this.authenticatedUser.set(userName);
      return true;
    }
    return false;
  }

  logout(): void {
    sessionStorage.removeItem('authenticatedUser');
    this.authenticatedUser.set(null);
  }
}

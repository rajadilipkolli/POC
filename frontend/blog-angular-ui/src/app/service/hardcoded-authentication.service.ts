import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HardcodedAuthenticationService {

  constructor() { }

  authenticate(userName: string, passCode: string) {
    if (userName === 'username' && passCode === 'dummy') {
      sessionStorage.setItem('authenticatedUser', userName);
      return true;
    } else {
      return false;
    }
  }

  isUserLoggedIn() {
    return !(sessionStorage.getItem('authenticatedUser') === null);
  }

  logout() {
    sessionStorage.removeItem('authenticatedUser');
  }
}

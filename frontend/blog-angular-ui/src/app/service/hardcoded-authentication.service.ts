import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HardcodedAuthenticationService {

  constructor() { }

  authenticate(userName: string, passCode: string) {
    if (userName === 'username' && passCode === 'dummy') {
      return true;
    } else {
      return false;
    }
  }
}

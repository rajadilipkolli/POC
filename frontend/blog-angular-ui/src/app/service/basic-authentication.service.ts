import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PingResponse } from './data/welcome-data.service';
import { map } from 'rxjs/operators';


export const TOKEN = 'token';
export const AUTHENTICATED_USER = 'authenticaterUser';

@Injectable({
  providedIn: 'root'
})
export class BasicAuthenticationService {

  constructor(
    private httpClient: HttpClient
  ) { }

  executeAuthenticationService(username: string, password: string) {

    const basicAuthHeaderString = 'Basic ' + window.btoa(username + ':' + password);

    const headers = new HttpHeaders({
      Authorization: basicAuthHeaderString
    });
    console.log(`heders set ${headers}`);
    return this.httpClient.get<PingResponse>(`http://localhost:8080/pingWithAuthentication`,
      { headers }).pipe(
        map(
          data => {
            console.log(`inside basic authentication ${username} & token ${basicAuthHeaderString}`);
            sessionStorage.setItem(AUTHENTICATED_USER, username);
            sessionStorage.setItem(TOKEN, basicAuthHeaderString);
            return data;
          }
        )
      );
  }

  getAuthenticatedUser() {
    return sessionStorage.getItem(AUTHENTICATED_USER);
  }

  getAuthenticatedToken() {
    if (this.getAuthenticatedUser()) {
      return sessionStorage.getItem(TOKEN);
    }
  }

  isUserLoggedIn() {
    const user = sessionStorage.getItem(AUTHENTICATED_USER);
    return !(user === null);
  }

  logout() {
    sessionStorage.removeItem(AUTHENTICATED_USER);
    sessionStorage.removeItem(TOKEN);
  }
}

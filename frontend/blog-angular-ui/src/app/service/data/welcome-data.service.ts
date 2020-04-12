import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class WelcomeDataService {

  constructor(
    // when we try to use any third party application we need to add it in app.module.ts as a module
    private http: HttpClient
  ) { }

  executeHelloWorldBeanService() {
    // TODO configure properties
    return this.http.get<PingResponse>('http://localhost:8080/ping');
  }
}

export class PingResponse {
  constructor(public message: string) { }
}

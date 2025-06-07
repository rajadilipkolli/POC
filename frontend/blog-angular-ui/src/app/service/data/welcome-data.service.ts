import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {API_URL} from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class WelcomeDataService {

  constructor(
    // when we try to use any third party application we need to add it in app.module.ts as a module
    private http: HttpClient
  ) {
  }

  executeHelloWorldBeanService() {
    // Updated endpoint to match backend API
    return this.http.get<PingResponse>(`${API_URL}/ping`);
  }

  executeHelloWorldBeanServiceWithPathVariable(name: string) {
    // Endpoint with path variable for personalized welcome message
    return this.http.get<PingResponse>(`${API_URL}/ping?userName=${name}`);
  }
}

export class PingResponse {
  constructor(public message: string) {
  }
}

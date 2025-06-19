import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URL } from '../../app.constants';

export interface PingResponse {
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class WelcomeDataService {
  private readonly http = inject(HttpClient);

  executeHelloWorldBeanService() {
    return this.http.get<PingResponse>(`${API_URL}/ping`);
  }

  executeHelloWorldBeanServiceWithPathVariable(name: string) {
    return this.http.get<PingResponse>(`${API_URL}/ping?userName=${name}`);
  }
}

import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { HttpInterceptorBasicAuthService } from './http-interceptor-basic-auth.service';

describe('HttpInterceptorBasicAuthService', () => {
  let service: HttpInterceptorBasicAuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule, 
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(HttpInterceptorBasicAuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

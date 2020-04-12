import { TestBed } from '@angular/core/testing';

import { HttpInterceptorBasicAuthService } from './http-interceptor-basic-auth.service';

describe('HttpInterceptorBasicAuthService', () => {
  let service: HttpInterceptorBasicAuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HttpInterceptorBasicAuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

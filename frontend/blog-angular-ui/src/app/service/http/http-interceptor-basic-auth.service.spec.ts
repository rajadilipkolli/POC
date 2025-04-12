import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { HttpInterceptorBasicAuthService } from './http-interceptor-basic-auth.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('HttpInterceptorBasicAuthService', () => {
  let service: HttpInterceptorBasicAuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({
    imports: [RouterTestingModule],
    providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
});
    service = TestBed.inject(HttpInterceptorBasicAuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

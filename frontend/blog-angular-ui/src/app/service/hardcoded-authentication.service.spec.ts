import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { HardcodedAuthenticationService } from './hardcoded-authentication.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('HardcodedAuthenticationService', () => {
  let service: HardcodedAuthenticationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
    imports: [RouterTestingModule],
    providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
});
    service = TestBed.inject(HardcodedAuthenticationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

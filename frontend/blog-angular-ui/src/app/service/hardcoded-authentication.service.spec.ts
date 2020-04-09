import { TestBed } from '@angular/core/testing';

import { HardcodedAuthenticationService } from './hardcoded-authentication.service';

describe('HardcodedAuthenticationService', () => {
  let service: HardcodedAuthenticationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HardcodedAuthenticationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

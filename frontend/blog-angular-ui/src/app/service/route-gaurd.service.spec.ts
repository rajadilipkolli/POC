import {provideHttpClientTesting} from '@angular/common/http/testing';
import {TestBed} from '@angular/core/testing';
import {provideRouter} from '@angular/router';

import {RouteGaurdService} from './route-gaurd.service';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';

describe('RouteGaurdService', () => {
  let service: RouteGaurdService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter([]),
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(RouteGaurdService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

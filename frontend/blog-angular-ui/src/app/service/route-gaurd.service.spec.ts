import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { RouteGaurdService } from './route-gaurd.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('RouteGaurdService', () => {
  let service: RouteGaurdService;

  beforeEach(() => {
    TestBed.configureTestingModule({
    imports: [RouterTestingModule],
    providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
});
    service = TestBed.inject(RouteGaurdService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

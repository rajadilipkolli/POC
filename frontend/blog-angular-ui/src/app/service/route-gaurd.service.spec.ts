import { TestBed } from '@angular/core/testing';

import { RouteGaurdService } from './route-gaurd.service';

describe('RouteGaurdService', () => {
  let service: RouteGaurdService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RouteGaurdService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

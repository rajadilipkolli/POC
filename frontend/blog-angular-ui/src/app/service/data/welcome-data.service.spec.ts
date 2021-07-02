import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { WelcomeDataService } from './welcome-data.service';

describe('WelcomeDataService', () => {
  let service: WelcomeDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule, 
        HttpClientTestingModule
      ],
    });
    service = TestBed.inject(WelcomeDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

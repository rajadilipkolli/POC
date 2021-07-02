import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { PostDataService } from './post-data.service';

describe('PostDataService', () => {
  let service: PostDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule, 
        HttpClientTestingModule
      ]
    });
    service = TestBed.inject(PostDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

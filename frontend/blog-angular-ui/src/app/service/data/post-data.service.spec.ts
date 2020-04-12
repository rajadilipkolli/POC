import { TestBed } from '@angular/core/testing';

import { PostDataService } from './post-data.service';

describe('PostDataService', () => {
  let service: PostDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

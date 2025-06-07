import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {ActivatedRoute, provideRouter, Router} from '@angular/router';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {FormsModule} from '@angular/forms';
import {DatePipe} from '@angular/common';

import {PostComponent} from './post.component';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {Comment, Post, Tag} from '../list-posts/list-posts.component';
import {API_URL} from '../app.constants';

describe('PostComponent', () => {
  let component: PostComponent;
  let fixture: ComponentFixture<PostComponent>;
  let httpTestingController: HttpTestingController;
  let router: Router;
  let datePipe: jasmine.SpyObj<DatePipe>;

  const mockComments = [new Comment('Test Comment')];
  const mockTags = [new Tag('Test Tag')];
  const fixedDate = '2025-06-06T10:00:00';
  const testUserName = 'raja';
  const mockPost = new Post(
    'Test Title',
    'Test Content',
    'testUser',
    fixedDate,
    mockComments,
    mockTags
  );
  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, PostComponent],
      providers: [
        provideRouter([]),
        {
          provide: DatePipe,
          useValue: jasmine.createSpyObj('DatePipe', ['transform'])
        },
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              params: {title: mockPost.title}
            }
          }
        }
      ]
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(PostComponent);
        component = fixture.componentInstance;
        httpTestingController = TestBed.inject(HttpTestingController);
        router = TestBed.inject(Router);
        datePipe = TestBed.inject(DatePipe) as jasmine.SpyObj<DatePipe>;

        // Reset and set up the spy for each test
        datePipe.transform.calls.reset();
        datePipe.transform.and.returnValue(fixedDate);

        // We're setting the title directly instead of calling fixture.detectChanges()
        // which would trigger ngOnInit and make an HTTP call
        component.title = mockPost.title;
      });
  }));

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should load post details on init', () => {
    component.ngOnInit();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${testUserName}/posts/${mockPost.title}`
    );
    expect(req.request.method).toBe('GET');
    req.flush(mockPost);

    expect(component.post).toEqual(mockPost);
    expect(component.title).toBe(mockPost.title);
  });
  it('should handle error when loading post', () => {
    const consoleSpy = spyOn(console, 'error');
    component.ngOnInit();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${testUserName}/posts/${mockPost.title}`
    );
    req.error(new ErrorEvent('Network error'));

    expect(consoleSpy).toHaveBeenCalled();
  });
  it('should update post successfully', () => {
    // No need to spy on datePipe.transform again as it's already set up in beforeEach
    spyOn(router, 'navigate');

    component.post = {...mockPost};
    component.updatePost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${testUserName}/posts/${mockPost.title}`
    );
    expect(req.request.method).toBe('PUT');

    const expectedPost = {...mockPost, createdOn: fixedDate};
    expect(req.request.body).toEqual(expectedPost);

    req.flush(null);

    expect(router.navigate).toHaveBeenCalledWith(['posts']);
  });

  it('should handle error when updating post', () => {
    const consoleSpy = spyOn(console, 'error');
    const navigateSpy = spyOn(router, 'navigate');

    component.post = {...mockPost};
    component.updatePost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${testUserName}/posts/${mockPost.title}`
    );
    req.error(new ErrorEvent('Network error'));

    expect(consoleSpy).toHaveBeenCalled();
    expect(navigateSpy).not.toHaveBeenCalled();
  });
  it('should transform date when updating post', () => {
    // No need to spy on datePipe.transform again
    component.post = {...mockPost};
    component.updatePost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${testUserName}/posts/${mockPost.title}`
    );
    expect(req.request.body.createdOn).toBe(fixedDate);
    req.flush(null);
  });

  it('should initialize with empty post', () => {
    expect(component.post).toBeDefined();
    expect(component.post.title).toBe('');
    expect(component.post.content).toBe('');
    expect(component.post.comments).toEqual([]);
    expect(component.post.tags).toEqual([]);
  });

  it('should preserve post metadata when updating', () => {
    component.post = {...mockPost};
    // No need to spy on datePipe.transform again

    component.updatePost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${testUserName}/posts/${mockPost.title}`
    );
    expect(req.request.body.comments).toEqual(mockComments);
    expect(req.request.body.tags).toEqual(mockTags);
    req.flush(null);
  });
  
  it('should handle null date when updating post', () => {
    const postWithNullDate = {...mockPost, createdOn: null as any};
    component.post = postWithNullDate;
    // We're using a hardcoded value in the component, so we don't need to mock the DatePipe transform
    
    component.updatePost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${testUserName}/posts/${mockPost.title}`
    );
    expect(req.request.body.createdOn).toBe('1970-01-01T00:00:00');
    req.flush(null);
  });  
  
  it('should handle invalid date when updating post', () => {
    const postWithInvalidDate = {...mockPost, createdOn: 'invalid-date'};
    component.post = postWithInvalidDate;
    
    // Mock the datePipe to return null for invalid date (which is the actual behavior)
    datePipe.transform.and.returnValue(null);
    
    spyOn(console, 'log');
    
    component.updatePost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${testUserName}/posts/${mockPost.title}`
    );
    
    // The original date should be preserved when DatePipe returns null
    expect(req.request.body.createdOn).toBe('invalid-date');
    req.flush(null);
  });

  it('should handle 404 error when loading post', () => {
    const consoleSpy = spyOn(console, 'error');
    component.ngOnInit();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${testUserName}/posts/${mockPost.title}`
    );
    req.flush('Post not found', { status: 404, statusText: 'Not Found' });

    expect(consoleSpy).toHaveBeenCalledWith('Error retrieving post:', jasmine.any(Object));
  });

  it('should handle server error when updating post', () => {
    const consoleSpy = spyOn(console, 'error');
    component.post = {...mockPost};
    component.updatePost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${testUserName}/posts/${mockPost.title}`
    );
    req.flush('Server Error', { status: 500, statusText: 'Internal Server Error' });

    expect(consoleSpy).toHaveBeenCalledWith('Error updating post:', jasmine.any(Object));
  });
});

import {DatePipe} from '@angular/common';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import {FormsModule} from '@angular/forms';
import {provideRouter, Router} from '@angular/router';

import {CreatePostComponent} from './create-post.component';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {Post} from '../list-posts/list-posts.component';
import {API_URL} from '../app.constants';

describe('CreatePostComponent', () => {
  let component: CreatePostComponent;
  let fixture: ComponentFixture<CreatePostComponent>;
  let httpTestingController: HttpTestingController;
  let router: Router;
  let datePipe: jasmine.SpyObj<DatePipe>;
  const fixedDate = '2025-06-06T10:00:00';
  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [FormsModule, CreatePostComponent],
      providers: [
        provideRouter([]),
        {
          provide: DatePipe,
          useValue: jasmine.createSpyObj('DatePipe', ['transform'])
        },
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    })
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(CreatePostComponent);
        component = fixture.componentInstance;
        httpTestingController = TestBed.inject(HttpTestingController);
        router = TestBed.inject(Router);
        datePipe = TestBed.inject(DatePipe) as jasmine.SpyObj<DatePipe>;

        // Reset and set up the spy for each test
        datePipe.transform.calls.reset();
        datePipe.transform.and.returnValue(fixedDate);

        // We don't need to call detectChanges() here to prevent multiple HTTP calls
      });
  }));

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty post', () => {
    expect(component.post).toBeDefined();
    expect(component.post.title).toBe('');
    expect(component.post.content).toBe('');
    expect(component.post.comments).toEqual([]);
    expect(component.post.tags).toEqual([]);
  });


  it('should create post successfully', () => {
    // No need to spy on datePipe.transform again since it's already set up in beforeEach
    spyOn(router, 'navigate');
    spyOn(console, 'log');

    const testPost = new Post('Test Title', 'Test Content', 'testUser', new Date().toISOString(), [], []);
    component.post = testPost;
    component.createPost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/raja/posts/`
    );
    expect(req.request.method).toBe('POST');

    // Compare properties individually instead of the whole object
    expect(req.request.body.title).toEqual(testPost.title);
    expect(req.request.body.content).toEqual(testPost.content);
    expect(req.request.body.createdBy).toEqual(testPost.createdBy);
    expect(req.request.body.createdOn).toEqual(fixedDate);
    expect(req.request.body.comments).toEqual([]);
    expect(req.request.body.tags).toEqual([]);

    req.flush({message: 'Post created successfully'});

    expect(component.message).toContain('Successfully Created Post');
    expect(router.navigate).toHaveBeenCalledWith(['posts']);
    expect(console.log).toHaveBeenCalled();
  });

  it('should handle error when creating post', () => {
    spyOn(console, 'log');
    spyOn(router, 'navigate');

    component.createPost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/raja/posts/`
    );
    req.error(new ErrorEvent('Network error'));

    expect(router.navigate).not.toHaveBeenCalled();
    expect(console.log).toHaveBeenCalled();
  });
  it('should transform date when creating post', () => {
    // No need to spy on datePipe.transform again
    component.createPost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/raja/posts/`
    );
    expect(req.request.body.createdOn).toBe(fixedDate);
    req.flush({message: 'Post created successfully'});
  });

  it('should initialize new post on ngOnInit', () => {
    component.ngOnInit();
    expect(component.post).toBeDefined();
    expect(component.post.title).toBe('');
    expect(component.post.content).toBe('');
    expect(component.post.comments).toEqual([]);
    expect(component.post.tags).toEqual([]);
    expect(component.message).toBe('');
  });

  it('should handle post with empty fields', () => {
    const emptyPost = new Post('', '', '', new Date().toISOString(), [], []);
    component.post = emptyPost;

    // No need to spy on datePipe.transform again
    component.createPost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/raja/posts/`
    );
    // Compare individual properties rather than the whole object
    expect(req.request.body.title).toBe('');
    expect(req.request.body.content).toBe('');
    expect(req.request.body.createdOn).toBe(fixedDate);
    req.flush({message: 'Post created successfully'});
  });
});

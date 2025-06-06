import { waitForAsync, ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';

import { PostComponent } from './post.component';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { Post, Comment, Tag } from '../list-posts/list-posts.component';
import { API_URL } from '../app.constants';

describe('PostComponent', () => {
  let component: PostComponent;
  let fixture: ComponentFixture<PostComponent>;
  let httpTestingController: HttpTestingController;
  let router: Router;
  let datePipe: DatePipe;

  const mockComments = [new Comment('Test Comment')];
  const mockTags = [new Tag('Test Tag')];
  const mockPost = new Post(
    'Test Title',
    'Test Content',
    'testUser',
    new Date().toISOString(),
    mockComments,
    mockTags
  );

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [PostComponent],
      imports: [RouterModule.forRoot([]), FormsModule],
      providers: [
        DatePipe,
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              params: { title: mockPost.title }
            }
          }
        }
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PostComponent);
    component = fixture.componentInstance;
    httpTestingController = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    datePipe = TestBed.inject(DatePipe);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load post details on init', () => {
    component.ngOnInit();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/raja/posts/${mockPost.title}`
    );
    expect(req.request.method).toBe('GET');
    req.flush(mockPost);

    expect(component.post).toEqual(mockPost);
    expect(component.title).toBe(mockPost.title);
  });

  it('should handle error when loading post', () => {
    spyOn(console, 'log');
    component.ngOnInit();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/raja/posts/${mockPost.title}`
    );
    const errorResponse = new ProgressEvent('error');
    req.error(errorResponse);

    expect(console.log).toHaveBeenCalled();
  });

  it('should update post successfully', () => {
    const transformedDate = '2025-06-06T10:00:00';
    spyOn(datePipe, 'transform').and.returnValue(transformedDate);
    spyOn(router, 'navigate');

    component.post = { ...mockPost };
    component.updatePost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/raja/posts/${mockPost.title}`
    );
    expect(req.request.method).toBe('PUT');
    
    const expectedPost = { ...mockPost, createdOn: transformedDate };
    expect(req.request.body).toEqual(expectedPost);
    
    req.flush(null);

    expect(router.navigate).toHaveBeenCalledWith(['posts']);
  });

  it('should handle error when updating post', () => {
    spyOn(console, 'log');
    spyOn(router, 'navigate');

    component.post = { ...mockPost };
    component.updatePost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/raja/posts/${mockPost.title}`
    );
    const errorResponse = new ProgressEvent('error');
    req.error(errorResponse);

    expect(console.log).toHaveBeenCalled();
    expect(router.navigate).not.toHaveBeenCalled();
  });

  it('should transform date when updating post', () => {
    const transformedDate = '2025-06-06T10:00:00';
    spyOn(datePipe, 'transform').and.returnValue(transformedDate);

    component.post = { ...mockPost };
    component.updatePost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/raja/posts/${mockPost.title}`
    );
    expect(req.request.body.createdOn).toBe(transformedDate);
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
    component.post = { ...mockPost };
    const transformedDate = '2025-06-06T10:00:00';
    spyOn(datePipe, 'transform').and.returnValue(transformedDate);

    component.updatePost();

    const req = httpTestingController.expectOne(
      `${API_URL}/users/raja/posts/${mockPost.title}`
    );
    expect(req.request.body.comments).toEqual(mockComments);
    expect(req.request.body.tags).toEqual(mockTags);
    req.flush(null);
  });
});

import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { waitForAsync, ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, RouterModule } from '@angular/router';

import { ListPostsComponent , Post, PostList, Comment, Tag } from './list-posts.component';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { PostDataService } from '../service/data/post-data.service';

import { API_URL } from '../app.constants';

describe('ListPostsComponent', () => {
  let component: ListPostsComponent;
  let fixture: ComponentFixture<ListPostsComponent>;
  let router: Router;
  let httpTestingController: HttpTestingController;

  const mockComments = [new Comment('Test Comment')];
  const mockTags = [new Tag('Test Tag')];
  const mockPosts = new PostList([
    new Post('Test Title 1', 'Content 1', 'user1', new Date().toISOString(), mockComments, mockTags),
    new Post('Test Title 2', 'Content 2', 'user1', new Date().toISOString(), [], [])
  ]);

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [ListPostsComponent],
      imports: [RouterModule.forRoot([])],
      providers: [
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
        PostDataService
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListPostsComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    postDataService = TestBed.inject(PostDataService);
    httpTestingController = TestBed.inject(HttpTestingController);
    // Remove fixture.detectChanges() from here to prevent initial HTTP request
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty posts array and message', () => {
    expect(component.posts).toEqual([]);
    expect(component.message).toBe('');
  });

  it('should load posts with comments and tags on init', () => {
    component.ngOnInit();
    
    const req = httpTestingController.expectOne(`${API_URL}/users/raja/posts`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPosts);

    expect(component.posts).toEqual(mockPosts.postList);
    expect(component.posts[0].comments).toEqual(mockComments);
    expect(component.posts[0].tags).toEqual(mockTags);
  });

  it('should handle error when loading posts', () => {
    const consoleSpy = spyOn(console, 'log');
    component.ngOnInit();    
    const req = httpTestingController.expectOne(`${API_URL}/users/raja/posts`);
    req.error(new ErrorEvent('error'));

    expect(component.posts).toEqual([]);
    expect(consoleSpy).toHaveBeenCalled();
  });

  it('should navigate to create post page', () => {
    spyOn(router, 'navigate');
    component.createPost();
    expect(router.navigate).toHaveBeenCalledWith(['createpost']);
  });

  it('should navigate to update post page', () => {
    const testTitle = 'Test Title';
    spyOn(router, 'navigate');
    component.updatePost(testTitle);
    expect(router.navigate).toHaveBeenCalledWith(['posts', testTitle]);
  });

  it('should delete post and refresh list successfully', () => {
    const testTitle = 'Test Title';
    component.deletePost(testTitle);

    // Handle delete request
    const deleteReq = httpTestingController.expectOne(`${API_URL}/users/raja/posts/${testTitle}`);
    expect(deleteReq.request.method).toBe('DELETE');
    deleteReq.flush({});

    // Handle subsequent get request from refreshPosts
    const getReq = httpTestingController.expectOne(`${API_URL}/users/raja/posts`);
    expect(getReq.request.method).toBe('GET');
    getReq.flush(mockPosts);

    expect(component.message).toContain('Deleted');
    expect(component.posts).toEqual(mockPosts.postList);
  });

  it('should handle error when deleting post', () => {
    const consoleSpy = spyOn(console, 'log');
    const testTitle = 'Test Title';
    
    component.deletePost(testTitle);
    const req = httpTestingController.expectOne(`${API_URL}/users/raja/posts/${testTitle}`);
    req.error(new ErrorEvent('error'));

    expect(consoleSpy).toHaveBeenCalled();
  });

  it('should refresh posts list successfully', () => {
    component.refreshPosts();

    const req = httpTestingController.expectOne(`${API_URL}/users/raja/posts`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPosts);

    expect(component.posts).toEqual(mockPosts.postList);
  });
  it('should handle error when refreshing posts', () => {
    const consoleSpy = spyOn(console, 'log');
    
    component.refreshPosts();

    const req = httpTestingController.expectOne(`${API_URL}/users/raja/posts`);
    req.error(new ErrorEvent('Network error'));

    expect(consoleSpy).toHaveBeenCalled();
    expect(component.posts).toEqual([]);
  });

  it('should handle empty posts list', () => {
    component.refreshPosts();

    const req = httpTestingController.expectOne(`${API_URL}/users/raja/posts`);
    req.flush(new PostList([]));

    expect(component.posts).toEqual([]);
  });

  it('should handle posts with empty comments and tags', () => {
    const postsWithoutMetadata = new PostList([
      new Post('Test Title', 'Content', 'user1', new Date().toISOString(), [], [])
    ]);

    component.refreshPosts();

    const req = httpTestingController.expectOne(`${API_URL}/users/raja/posts`);
    req.flush(postsWithoutMetadata);

    expect(component.posts[0].comments).toEqual([]);
    expect(component.posts[0].tags).toEqual([]);
  });
});

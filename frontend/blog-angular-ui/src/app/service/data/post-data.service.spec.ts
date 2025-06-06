import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { PostDataService } from './post-data.service';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { Post, PostList } from '../../list-posts/list-posts.component';
import { API_URL } from '../../app.constants';

describe('PostDataService', () => {
  let service: PostDataService;
  let httpTestingController: HttpTestingController;

  const mockUsername = 'testUser';
  const mockPost = new Post('Test Title', 'Test Content', mockUsername, new Date().toISOString(), [], []);
  const mockPostList = new PostList([mockPost]);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(PostDataService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all posts', () => {
    service.retrieveAllPosts(mockUsername).subscribe(posts => {
      expect(posts).toEqual(mockPostList);
    });

    const req = httpTestingController.expectOne(`${API_URL}/users/${mockUsername}/posts`);
    expect(req.request.method).toBe('GET');
    req.flush(mockPostList);
  });

  it('should delete post by title and username', () => {
    service.deletePostByTitleAndUserName(mockPost.title, mockUsername).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${mockUsername}/posts/${mockPost.title}`
    );
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should retrieve post by title and username', () => {
    service.retrievePostByTitleAndUserName(mockPost.title, mockUsername).subscribe(post => {
      expect(post).toEqual(mockPost);
    });

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${mockUsername}/posts/${mockPost.title}`
    );
    expect(req.request.method).toBe('GET');
    req.flush(mockPost);
  });

  it('should update post by title and username', () => {
    service.updatePostByTitleAndUserName(mockPost.title, mockUsername, mockPost).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${mockUsername}/posts/${mockPost.title}`
    );
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(mockPost);
    req.flush(null);
  });

  it('should create post by username', () => {
    service.createPostByUserName(mockPost, mockUsername).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpTestingController.expectOne(
      `${API_URL}/users/${mockUsername}/posts/`
    );
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockPost);
    req.flush(null);
  });
});

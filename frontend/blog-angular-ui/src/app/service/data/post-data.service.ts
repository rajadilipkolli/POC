import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Post, PostList } from 'src/app/list-posts/list-posts.component';
import { API_URL } from 'src/app/app.constants';

@Injectable({
  providedIn: 'root'
})
export class PostDataService {

  constructor(private http: HttpClient) { }

  retrieveAllPosts(username: string) {
    return this.http.get<PostList>(`${API_URL}/users/${username}/posts`);
  }

  deletePostByTitleAndUserName(postTitle: string, username: string) {
    return this.http.delete(`${API_URL}/users/${username}/posts/${postTitle}`);
  }

  retrievePostByTitleAndUserName(postTitle: string, username: string) {
    return this.http.get<Post>(`${API_URL}/users/${username}/posts/${postTitle}`);
  }

  updatePostByTitleAndUserName(postTitle: string, username: string, post: Post) {
    return this.http.put(`${API_URL}/users/${username}/posts/${postTitle}`, post);
  }

  createPostByUserName(post: Post, username: string) {
    return this.http.post(`${API_URL}/users/${username}/posts/`, post);
  }

}

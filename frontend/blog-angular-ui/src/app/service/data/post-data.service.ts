import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URL } from '../../app.constants';

export interface Comment {
  review: string;
}

export interface Tag {
  name: string;
}

export interface Post {
  title: string;
  content: string;
  createdBy: string;
  createdOn: string;
  comments: Comment[];
  tags: Tag[];
}

export interface PostList {
  postList: Post[];
}

@Injectable({
  providedIn: 'root'
})
export class PostDataService {
  private readonly http = inject(HttpClient);

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

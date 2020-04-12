import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Post } from 'src/app/list-posts/list-posts.component';

@Injectable({
  providedIn: 'root'
})
export class PostDataService {

  constructor(private http: HttpClient) { }

  retrieveAllPosts(username: string) {
    return this.http.get<Post[]>(`http://localhost:8080/users/${username}/posts`);
  }

  deletePostByTitleAndUserName(postTitle: string, username: string) {
    return this.http.delete(`http://localhost:8080/users/${username}/posts/${postTitle}`);
  }

}

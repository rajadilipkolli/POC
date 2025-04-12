import { Component, OnInit } from '@angular/core';
import { PostDataService } from '../service/data/post-data.service';
import { Router } from '@angular/router';


export class PostList {
  constructor(
    public postList: Post[]
  ) {

  }
}

export class Post {
  constructor(
    public title: string,
    public content: string,
    public createdBy: string,
    public createdOn: string,
    public comments: Comment[],
    public tags: Tag[]
  ) {

  }
}

export class Comment {
  constructor(
    public review: string
  ) {

  }
}

export class Tag {
  constructor(
    public name: string  // Changed from 'review' to 'name' to match the template
  ) {

  }
}

@Component({
    selector: 'app-list-posts',
    templateUrl: './list-posts.component.html',
    styleUrls: ['./list-posts.component.css'],
    standalone: false
})
export class ListPostsComponent implements OnInit {

  // posts = [
  //   new Post(1, 'dummy Title', 'dummy Content', new Date()),
  //   new Post(2, 'new Title', 'new Content', new Date())
  // ];

  posts: Post[];

  message: string;

  constructor(
    private postDataService: PostDataService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.refreshPosts();
  }

  handleRetrieveAllPostsResponse(response: PostList): void {
    this.posts = response.postList;
  }

  deletePost(postTitle: string) {
    this.postDataService.deletePostByTitleAndUserName(postTitle, 'raja').subscribe(
      response => {
        console.log(response);
        this.message = `Successfully Deleted Post With Title ${postTitle}`;
        this.refreshPosts();
      }
    );
  }

  refreshPosts() {
    this.postDataService.retrieveAllPosts('raja').subscribe(
      response => this.handleRetrieveAllPostsResponse(response)
    );
  }

  updatePost(postTitle: string) {
    console.log(`Update ${postTitle}`);
    this.router.navigate(['posts', postTitle]);
  }

  createPost() {
    this.router.navigate(['createpost']);
  }

}

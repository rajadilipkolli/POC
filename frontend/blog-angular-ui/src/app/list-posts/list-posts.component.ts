import { Component, OnInit } from '@angular/core';
import { PostDataService } from '../service/data/post-data.service';

export class Post {
  constructor(
    public title: string,
    public content: string,
    public createdBy: string,
    public createdOn: Date,
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
    public review: string
  ) {

  }
}

@Component({
  selector: 'app-list-posts',
  templateUrl: './list-posts.component.html',
  styleUrls: ['./list-posts.component.css']
})
export class ListPostsComponent implements OnInit {

  // posts = [
  //   new Post(1, 'dummy Title', 'dummy Content', new Date()),
  //   new Post(2, 'new Title', 'new Content', new Date())
  // ];

  posts: Post[];

  constructor(
    private postDataService: PostDataService
  ) { }

  ngOnInit(): void {
    this.postDataService.retrieveAllPosts('raja').subscribe(
      response => this.handleRetrieveAllPostsResponse(response)
    )
  }

  handleRetrieveAllPostsResponse(response: Post[]): void {
    this.posts = response;
  }


}

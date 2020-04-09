import { Component, OnInit } from '@angular/core';

export class Post {
  constructor(
    public id: number,
    public title: string,
    public content: string,
    public createdOn: Date) {

  }
}

@Component({
  selector: 'app-list-posts',
  templateUrl: './list-posts.component.html',
  styleUrls: ['./list-posts.component.css']
})
export class ListPostsComponent implements OnInit {

  posts = [
    new Post(1, 'dummy Title', 'dummy Content', new Date()),
    new Post(2, 'new Title', 'new Content', new Date())
  ];

  constructor() { }

  ngOnInit(): void {
  }

}

import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-list-posts',
  templateUrl: './list-posts.component.html',
  styleUrls: ['./list-posts.component.css']
})
export class ListPostsComponent implements OnInit {

  posts = [
    { id: 1, title: 'dummy Title', content: 'dummy Content' },
    { id: 2, title: 'new Title', content: 'new Content' }
  ]

  constructor() { }

  ngOnInit(): void {
  }

}

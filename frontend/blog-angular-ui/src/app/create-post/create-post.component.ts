import { Component, OnInit } from '@angular/core';
import { Post } from '../list-posts/list-posts.component';
import { PostDataService } from '../service/data/post-data.service';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
    selector: 'app-create-post',
    templateUrl: './create-post.component.html',
    styleUrls: ['./create-post.component.css'],
    
})
export class CreatePostComponent implements OnInit {
  post: Post = new Post('', '', '', new Date().toISOString(), [], []);
  message = '';

  constructor(
    private postDataService: PostDataService,
    private router: Router,
    private datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    this.post = new Post('', '', '', new Date().toISOString(), [], []);
  }  createPost() {
    const transformedDate = this.datePipe.transform(this.post.createdOn, 'yyyy-MM-ddTHH:mm:ss');
    this.post.createdOn = transformedDate || this.post.createdOn;
    this.postDataService.createPostByUserName(this.post, 'raja')
      .subscribe({
        next: (response) => {
          console.log(response);
          this.message = `Successfully Created Post With Title ${this.post.title}`;
          this.router.navigate(['posts']);
        },
        error: (error) => {
          console.log('Error creating post:', error);
        }
      });
  }

}

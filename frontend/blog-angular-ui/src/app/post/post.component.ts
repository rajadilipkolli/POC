import { Component, OnInit } from '@angular/core';
import { Post } from '../list-posts/list-posts.component';
import { PostDataService } from '../service/data/post-data.service';
import { ActivatedRoute, Router } from '@angular/router';
import { DatePipe } from '@angular/common';

@Component({
    selector: 'app-post',
    templateUrl: './post.component.html',
    styleUrls: ['./post.component.css'],
    standalone: false
})
export class PostComponent implements OnInit {
  title: string = '';
  post: Post = new Post('', '', '', new Date().toISOString(), [], []);

  constructor(
    private postDataService: PostDataService,
    private route: ActivatedRoute,
    private router: Router,
    private datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    const newLocal = 'title';
    this.title = this.route.snapshot.params[newLocal];
    this.postDataService.retrievePostByTitleAndUserName(this.title, 'raja')
      .subscribe(
        response => this.post = response
      );
  }

  updatePost() {
    const transformedDate = this.datePipe.transform(this.post.createdOn, 'yyyy-MM-ddTHH:mm:ss');
    this.post.createdOn = transformedDate || this.post.createdOn;
    console.log(`inside update Post ${this.post}`);
    this.postDataService.updatePostByTitleAndUserName(this.title, 'raja', this.post)
      .subscribe(
        response => {
          console.log(response);
          this.router.navigate(['posts']);
        },
        error => {
          console.error('Error updating post:', error);
          // Consider adding user feedback for the error
        }
      );
  }
}

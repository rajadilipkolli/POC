import {Component, OnInit} from '@angular/core';
import {Post} from '../list-posts/list-posts.component';
import {PostDataService} from '../service/data/post-data.service';
import {ActivatedRoute, Router} from '@angular/router';
import {CommonModule, DatePipe} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule],
  providers: [DatePipe]
})
export class PostComponent implements OnInit {
  title = '';
  post: Post = new Post('', '', '', new Date().toISOString(), [], []);

  constructor(
    private postDataService: PostDataService,
    private route: ActivatedRoute,
    private router: Router,
    private datePipe: DatePipe
  ) {
  }

  ngOnInit(): void {
    const newLocal = 'title';
    this.title = this.route.snapshot.params[newLocal];
    this.postDataService.retrievePostByTitleAndUserName(this.title, 'raja')
      .subscribe({
        next: (response) => this.post = response,
        error: (error) => console.error('Error retrieving post:', error)
      });
  }
  updatePost() {
    // Use a basic format string that matches our test mock
    try {
      const dateObj = new Date(this.post.createdOn);
      // Check if the date is valid before using DatePipe
      if (!isNaN(dateObj.getTime())) {
        const transformedDate = this.datePipe.transform(dateObj, 'yyyy-MM-ddTHH:mm:ss');
        this.post.createdOn = transformedDate || this.post.createdOn;
      }
    } catch (e) {
      // Keep the original date string if there's an error
    }

    console.log(`inside update Post ${this.post}`);
    this.postDataService.updatePostByTitleAndUserName(this.title, 'raja', this.post)
      .subscribe({
        next: (response) => {
          console.log(response);
          this.router.navigate(['posts']);
        },
        error: (error) => {
          console.error('Error updating post:', error);
          // Consider adding user feedback for the error
        }
      });
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

const newLocal = 'name';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  name = '';

  // inject ActivatedRoute
  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.name = this.route.snapshot.params[newLocal];
  }

}

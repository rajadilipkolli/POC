import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { WelcomeDataService } from '../service/data/welcome-data.service';

const newLocal = 'name';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  name = '';

  // inject ActivatedRoute
  constructor(
    private route: ActivatedRoute,
    private helloWorldService: WelcomeDataService
  ) { }

  welcomeMessage: '';

  ngOnInit(): void {
    this.name = this.route.snapshot.params[newLocal];
  }

  getWelcomeMessage() {
    // to call any service we should do it asynchronously
    this.helloWorldService.executeHelloWorldBeanService().subscribe(
      res => this.handleSuccessFulResponse(res)
    );
  }

  handleSuccessFulResponse(response) {

    this.welcomeMessage = response.message;
  }

}

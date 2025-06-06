import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {HardcodedAuthenticationService} from '../service/hardcoded-authentication.service';
import {BasicAuthenticationService} from '../service/basic-authentication.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css'],
  standalone: true,
  imports: [CommonModule, RouterModule]
})
export class MenuComponent implements OnInit {

  constructor(
    public hardcodedAuthenticationService: HardcodedAuthenticationService,
    public basicAuthenticationService: BasicAuthenticationService
  ) {
  }

  ngOnInit(): void {
    // this.hardcodedAuthenticationService.isUserLoggedIn();
  }

}

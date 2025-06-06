import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HardcodedAuthenticationService} from '../service/hardcoded-authentication.service';
import {BasicAuthenticationService} from '../service/basic-authentication.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css'],
  standalone: true,
  imports: [CommonModule]
})
export class LogoutComponent implements OnInit {

  constructor(
    private hardCodedAuthenticationService: HardcodedAuthenticationService,
    private basicAuthenticationService: BasicAuthenticationService
  ) {
  }

  ngOnInit(): void {
    // this.hardCodedAuthenticationService.logout();
    this.basicAuthenticationService.logout();
  }

}

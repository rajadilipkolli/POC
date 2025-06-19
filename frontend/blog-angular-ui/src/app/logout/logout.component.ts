import { Component, ChangeDetectionStrategy, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HardcodedAuthenticationService } from '../service/hardcoded-authentication.service';
import { BasicAuthenticationService } from '../service/basic-authentication.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule]
})
export class LogoutComponent implements OnInit {
  private readonly hardCodedAuthenticationService = inject(HardcodedAuthenticationService);
  private readonly basicAuthenticationService = inject(BasicAuthenticationService);

  ngOnInit(): void {
    // this.hardCodedAuthenticationService.logout();
    this.basicAuthenticationService.logout();
  }
}

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BasicAuthenticationService } from '../service/basic-authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  userName = 'username';
  passWord = '';
  errorMessage = 'Invalid Credentials';
  invalidLogin = false;

  // Router Dependency Injection
  constructor(
    private router: Router, 
    private basicAuthenticationService: BasicAuthenticationService
    ) { }

  ngOnInit(): void {
  }

  handleLogin() {
    console.log('UserName : ', this.userName);
    this.basicAuthenticationService.executeAuthenticationService(this.userName, this.passWord)
    if (this.basicAuthenticationService.isUserLoggedIn()) {
      // Redirect to Welcome Page
      this.router.navigate(['welcome', this.basicAuthenticationService.getAuthenticatedUser()]);
      this.invalidLogin = false;
    } else {
      this.invalidLogin = true;
    }
  }

}

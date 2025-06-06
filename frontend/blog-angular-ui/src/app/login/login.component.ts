import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {BasicAuthenticationService} from '../service/basic-authentication.service';
import {HardcodedAuthenticationService} from '../service/hardcoded-authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class LoginComponent implements OnInit {

  userName = 'username';
  passWord = '';
  errorMessage = 'Invalid Credentials';
  invalidLogin = false;

  // Router Dependency Injection
  constructor(
    private router: Router,
    private hardcodedAuthenticationService: HardcodedAuthenticationService,
    private basicAuthenticationService: BasicAuthenticationService
  ) {
  }

  ngOnInit(): void {
  }

  handleLogin() {
    // console.log(this.username);
    // if(this.username==="username" && this.password === 'password') {
    if (this.hardcodedAuthenticationService.authenticate(this.userName, this.passWord)) {
      // Redirect to Welcome Page
      this.router.navigate(['welcome', this.userName]);
      this.invalidLogin = false;
    } else {
      this.invalidLogin = true;
    }
  }

  handleBasicAuthLogin() {
    // console.log(this.username);
    // if(this.username==="username" && this.password === 'password') {
    this.basicAuthenticationService.executeAuthenticationService(this.userName, this.passWord)
      .subscribe(
        () => {
          this.router.navigate(['welcome', this.userName]);
          this.invalidLogin = false;
        },
        error => {
          console.log(error);
          this.invalidLogin = true;
        }
      );
  }


}

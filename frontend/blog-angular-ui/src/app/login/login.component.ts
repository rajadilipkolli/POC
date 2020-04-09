import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

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
  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  handleLogin() {
    console.log('UserName : ', this.userName);
    if (this.userName === 'raja' && this.passWord === 'dummy') {
      // Redirect to Welcome Page
      this.router.navigate(['welcome', this.userName]);
      this.invalidLogin = false;
    } else {
      this.invalidLogin = true;
    }
  }

}

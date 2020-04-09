import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  userName = 'username'
  passWord = ''
  errorMessage = 'Invalid Credentials'
  invalidLogin = false

  constructor() { }

  ngOnInit(): void {
  }

  handleLogin() {
    console.log("UserName : ", this.userName);
    if (this.userName=== 'raja' && this.passWord==='dummy') {
      this.invalidLogin = false
    } else {
      this.invalidLogin = true
    }
  }

}

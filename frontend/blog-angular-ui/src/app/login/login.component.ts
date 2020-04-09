import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  userName = 'username'
  passWord = ''

  constructor() { }

  ngOnInit(): void {
  }

  handleLogin() {
    console.log("UserName : ", this.userName);
    console.log("Password : ", this.passWord)
  }

}

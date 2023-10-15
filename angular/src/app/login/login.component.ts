import { Component, Input, OnInit } from '@angular/core';
import { LoginService } from '../login.service';
import { User } from '../user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {


  @Input() username?: string;
  @Input() password?: string;
  private loginService: LoginService;
  loginStatus?: string

  constructor(login: LoginService) {
    this.loginService = login;
    this.loginStatus = this.loginService.getLoginStatus();
  }  


  ngOnInit(): void {  
  }
  
  /**
   * method that communicates the front end to the login service, and updates front end
   * with login feedback provided by the login service provider.
   * @param username username to be logged into
   * @param password password for requested username
   * @returns 
   */
  login(username: string | undefined, password: string | undefined) {
    // if user and/or pass is null don't allow login processing.
    if(username == undefined || password == undefined) {
      alert("You cannot have an empty username or password field.");
      return;
    }

    if(username.length == 0 || password.length == 0) {
      alert("You cannot have an empty username or password field.");
      return;
    } 
    // have login service attempt login and subscribe the result to actually have information be sent.
    const res = this.loginService.login(username, password); // logic is handled in login service
    res.subscribe()
    // update login status on a delay so that it has a chance for server response.
    setTimeout(() => {
      this.loginStatus = this.loginService.getLoginStatus(); // update login status
    }, 1000);
  }

  /**
   * Method that communicates with login service to attempt
   * sign up with the backend server and provide feedback to user based on 
   * http error.
   * @param username username to be signed up
   * @param password password to be signed up
   * @returns 
   */
  signUp(username: string | undefined, password: string | undefined) {
    // username and password 
    if(username == undefined || password == undefined) {
      alert("You cannot have an empty username or password field.");
      return;
    }

    if(username.length == 0 || password.length == 0) {
      alert("You cannot have an empty username or password field.");
      return;
    } 
    // send a sign up request to login service with the user object
    // that is wanted to be created.
    const res = this.loginService.signUp({
      id: 0, username: username, password: password,
      isAdmin: null,
      loggedIn: null
    });
    res.subscribe()
    // set a delay to update the login status.
    setTimeout(() => {
      this.loginStatus = this.loginService.getLoginStatus(); // update login status
    }, 1000);
  
  }

  /**
   * method ran to commmunicate with login service and provide
   * front end feedback for user.
   */
  logout(): void {
    if(LoginService.loggedInUser != LoginService.nullUser) {
      // send login service logout request for user
      const result = this.loginService.logout(LoginService.loggedInUser.username!)
      result.subscribe();
      // update login request
      setTimeout(() => {
        this.loginStatus = this.loginService.getLoginStatus(); // update login status
      }, 1000);
    } else {
      // alert inability to logout if user is somehow calling logout while logged out.
      // (logout functionality is hidden whilst not logged in)
      alert("You are not signed in");
    }

  }

  /**
   * simple method to determine if a user is logged in
   * @returns boolean of if someone is logged in.
   */
  isLoggedIn(): boolean {
    return LoginService.loggedInUser != LoginService.nullUser;
  }

  isAdmin(): boolean{
    return LoginService.loggedInUser.username == "admin";
  }

}

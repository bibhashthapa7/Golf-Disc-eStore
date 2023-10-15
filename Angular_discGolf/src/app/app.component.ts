import { Component } from '@angular/core';
import { LoginService } from './login.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Acadia Disc Golf'; //Acadia Disc Golf is the tile, because Acadia National Park is in Maine, and our store is based in Maine
  loginService: LoginService;
  constructor(loginService: LoginService) {
    this.loginService = loginService;
  }

  canSeeStuff(): boolean {
    const user = LoginService.loggedInUser;
    return (user != LoginService.nullUser) && (user.username != "admin")
  }

  isSignedOut() {
    return !this.loginService.isSignedIn()
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


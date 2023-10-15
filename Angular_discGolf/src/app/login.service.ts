import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpStatusCode, HttpUserEvent } from '@angular/common/http';
import { User } from './user';
import { catchError, Observable, of, tap } from 'rxjs';
import { ShoppingCartService } from './shopping-cart.service';
import { ShoppingCart } from './shopping-cart';
import { UrlSerializer } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loginStatus?: string;
  private usersUrl = 'http://localhost:8080/users';  // URL to api
  // representation of a "null" assignment of a user
  public static nullUser: User = { id: null, username: null, password: null, isAdmin: null, loggedIn: null }
  // static value determining which user is logged into the site. (done horribly in practice, but works for minimumality.)
  public static loggedInUser: User = LoginService.nullUser;
  private cartService: ShoppingCartService
  // http options used in rest calls
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient, cartService: ShoppingCartService) {
    this.loginStatus = "Not Logged In"
    this.cartService = cartService;
  }

  ngOnInit(): void {

  }


  /**
   * method to attempt login of a user
   * @param username username of user
   * @param password password of user
   * @returns observable.
   */
  login(username: string, password: string): Observable<User> {
    const url = `${this.usersUrl}/${username}/login/${password}`;
    return this.http.get<User>(url).pipe(
      tap(result =>
        this.handleLogin(result)
      ),
      catchError(this.handleLoginError<User>(`user was not able to login.`))
    );
  }

  private handleLogin(user: User) {
    this.setLoginStatus("Logged in as: " + user.username);
    LoginService.loggedInUser = user;
  }

  /**
   * error handler for rest callback relating to logging a user in  
   * @param operation operation failed string
   * @param result result returned by rest
   * @returns 
   */
  private handleLoginError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      if (error instanceof HttpErrorResponse) {
        // User not found in system
        if (error.status == HttpStatusCode.NotFound) {
          this.setLoginStatus("This user does not exist in the system, try signing up instead!");
        }

        // User not able to login (user/pass combo failure)
        if (error.status == HttpStatusCode.Unauthorized) {
          this.setLoginStatus("Invalid username or password. Please try again.");
        }


      }

      console.error(error); // log to console 

      console.log(`${operation} failed: ${error.message}`);

      return of(result as T);
    };
  }

  /**
   * attempts logging out a user from the system
   * @param username username attempted to be logged out
   * @returns observable of a user, regarding if they get logged out
   */
  logout(username: string) {
    const url = `${this.usersUrl}/${username}/logout`;
    return this.http.get<User>(url).pipe(
      tap(result =>
        this.handleLogout()
      ),
      catchError(this.handleLogoutError<User>(`user was not able to logout.`))
    );
  }

  handleLogout(): void {
    this.setLoginStatus("Not Logged In")
    LoginService.loggedInUser = LoginService.nullUser;
  }

  /**
   * error handler for rest callback relating to logging a user in  
   * @param operation operation failed string
   * @param result result returned by rest
   * @returns 
   */
  private handleLogoutError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      if (error instanceof HttpErrorResponse) {
        console.log("catching error.")

        if (error.status == HttpStatusCode.Unauthorized) {
          this.setLoginStatus("You need to be logged in to logout.");
        }
      }

      console.error(error);
      console.log(`${operation} failed: ${error.message}`);


      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /**
   * handles signing a new user up. 
   * @param username username of new user
   * @param password password of new user
   * @returns an observable of a user determining success or not
   */
  signUp(user: User): Observable<User> {
    const url = `${this.usersUrl}`;
    return this.http.post<any>(url, user).pipe(
      tap(result => {
        this.cartService.createCart(user.username).subscribe();
        this.handleSignUp(result)
      }
      ),
      catchError(this.handleSignUpError<User>(`user was not able to sign up.`, user))
    );
  }

  /**
   * a simple method to do additional logic that has to be done after sign up
   * @param user user created
   */
  handleSignUp(user: User) {
    this.setLoginStatus("Successfully created new user: " + user.username + ". Please sign in.");
  }
  /**
   * error handler for rest callback relating to logging a user in  
   * @param operation operation failed string
   * @param result result returned by rest
   * @returns 
   */
  private handleSignUpError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      if (error instanceof HttpErrorResponse) {
        if (error.status == HttpStatusCode.Conflict) {
          this.setLoginStatus("This user already exists. Please sign in.");
        }

      }

      console.error(error);
      console.log(`${operation} failed: ${error.message}`);


      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
  /**
   * gets the current login status of a user (logged in, invalid credentials etc.)
   * @returns login status
   */
  getLoginStatus() {
    return this.loginStatus;
  }

  /**
   * updates the login status to a new string
   * @param status new login status
   */
  setLoginStatus(status: string) {
    this.loginStatus = status;
  }

  isAdminLoggedIn(): boolean {
    if (LoginService.loggedInUser.isAdmin != undefined) {
      // console.log("1")
      return LoginService.loggedInUser.isAdmin;
    }
    if (LoginService.loggedInUser.isAdmin! == undefined) {
      // console.log("2")
      return false;
    }
    console.log("3")
    return LoginService.loggedInUser.isAdmin!;
  }

  isSignedIn(): boolean {
    return LoginService.loggedInUser != LoginService.nullUser;
  }

}

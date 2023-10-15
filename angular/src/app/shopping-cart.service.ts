import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpStatusCode } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, take, tap } from 'rxjs/operators';

import { Product } from './product';
import { ShoppingCart } from './shopping-cart';

@Injectable({
  providedIn: 'root'
})
export class ShoppingCartService {
  private cartsUrl = 'http://localhost:8080/carts'
  cart?: ShoppingCart
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
  ){}

  /* GET cart-products by username */
  getProducts(username: string | null): Observable<Product[]> {
    const url = `${this.cartsUrl}/${username}/contents`;
    return this.http.get<Product[]>(url);
  }

  /* GET cart-cost by username */
  getCost(username: string | null): Observable<number> {
    const url = `${this.cartsUrl}/getCost/${username}`;
    return this.http.get<number>(url);
  }

  /* GET cart-product-count by username */
  getCount(username: string | null): Observable<number> {
    const url = `${this.cartsUrl}/getCount/${username}`;
    return this.http.get<number>(url);
  }

  /* POST create a cart for user */
  createCart(username: string | null): Observable<ShoppingCart> {
    const url = `${this.cartsUrl}`;
    return this.http.post<ShoppingCart>(url, username)
  }

  /* PUT product in cart*/
  addProduct(username: string | null, id: number): Observable<Product> {
    const url = `${this.cartsUrl}/addDisc/${username}/${id}`;
    return this.http.put<Product>(url, null, this.httpOptions)
      .pipe(tap(),
        catchError(this.handleError<Product>('addProduct'))
      );
  }

  /* PUT (remove) product from cart */
  deleteProduct(username: string | null, id: number): Observable<Product> {
    const url = `${this.cartsUrl}/removeDisc/${username}/${id}`;
    return this.http.put<Product>(url, this.httpOptions)
      .pipe(tap(),
        catchError(this.handleError<any>('deleteProduct'))
      );
  }

  /* PUT cart-product quantity update */
  updateProductQuantity(username: string | null, id: number, quantity: number): Observable<Product> {
    const url = `${this.cartsUrl}/updateDiscQuantity/${username}/${id}/${quantity}/0`;
    return this.http.put<Product>(url, this.httpOptions)
      .pipe(tap(result => {
        console.log(result)
      }),
        catchError(this.handleError<any>('updateProductQuantity'))
      );
  }

  /* PUT purchase products in the cart */
  purchaseCart(username: string | null): Observable<Product[]>  {
    const url = `${this.cartsUrl}/purchase/${username}`;
    return this.http.put<Product[]>(url, this.httpOptions)
  }

  /* GET cart-purchase-conflicts by username */
  checkCart(username: string | null): Observable<Product[]>  {
    const url = `${this.cartsUrl}/checkCart/${username}`;
    return this.http.get<Product>(url)
      .pipe(tap(result => {
        console.log(result)
      }),
        catchError(this.handleError<any>('checkCart'))
      );
  }

  /* PUT purchase products in the cart */
  purchaseOne(username: string | null, id: string): Observable<Product>  {
    const url = `${this.cartsUrl}/purchaseOne/${username}/${id}`;
    return this.http.put<Product>(url, this.httpOptions)
  }

  /* GET cart-purchase-conflicts by username */
  checkOne(username: string | null, id: string): Observable<Product>  {
    const url = `${this.cartsUrl}/checkOne/${username}/${id}`;
    return this.http.get<Product>(url)
      .pipe(tap(result => {
        console.log(result)
      }),
        catchError(this.handleError<any>('checkOne'))
      );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
   private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => { 
      // Send error to console
      console.error(error);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
}
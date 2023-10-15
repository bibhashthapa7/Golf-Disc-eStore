// import { Injectable } from '@angular/core';
// import { HttpClient, HttpHeaders } from '@angular/common/http';
// import {Product} from './product';
// import {PRODUCTS} from './mock-products';
// import { Observable, of } from 'rxjs';
// import { catchError, map, tap } from 'rxjs';

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Product } from './product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsUrl = 'http://localhost:8080/discs'

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient,
  ) { }

  /* GET products from server */
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.productsUrl, this.httpOptions);
  }

  /* GET product by id */
  getProduct(id: string): Observable<Product> {
    const url = `${this.productsUrl}/${id}`;
    return this.http.get<Product>(url)
      .pipe(tap(),
        catchError(this.handleError<Product>(`getProduct id=${id}`))
      );
  }

  /* GET discs whose name contains search term */
  searchProduct(term: string): Observable<Product[]> {
    if (!term.trim()) {
      // if not search term, return empty disc array.
      return of([]);
    }
    return this.http.get<Product[]>(`${this.productsUrl}/?type=${term}`)
      .pipe(tap(), catchError(this.handleError<Product[]>('searchProduct', []))
      );
  }

  getFilteredProducts(term: string, mode: number): Observable<Product[]>{
    if (!term.trim()) {
      // if not search term, return empty disc array.
      return of([]);
    }
    return this.http.get<Product[]>(`${this.productsUrl}/filter?search=${term}&mode=${mode}`)
        .pipe(tap(), catchError(this.handleError<Product[]>('filterProduct', []))
        );
  }

  /////// SAVE METHODS ///////

  /* POST product to server*/
  addProduct(product: Product): Observable<Product> {
    const url = `${this.productsUrl}`;
    return this.http.post<Product>(url, product, this.httpOptions)
      .pipe(tap(),
        catchError(this.handleError<Product>('addProduct'))
      );
  }

  /* DELETE product from server */
  deleteProduct(id: number): Observable<Product> {
    const url = `${this.productsUrl}/${id}`;
    return this.http.delete<Product>(url, this.httpOptions)
      .pipe(tap(),
        catchError(this.handleError<any>('deleteProduct'))
      );
  }


  updateProduct(product: Product): Observable<Product> {
    const url = `${this.productsUrl}`;
    return this.http.put<Product>(url, product)
      .pipe(tap(result => {
        console.log(result)
      }),
        catchError(this.handleError<any>('updateProduct'))
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

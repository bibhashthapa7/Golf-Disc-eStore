/**
 * Lesson Service
 * @author Nikhil Patil + Coolname
 * @version 1.1
 */

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { Lesson } from './lessons';
@Injectable({
  providedIn: 'root'
})
export class LessonService {
  private lessonsUrl = 'http://localhost:8080/lessons'


  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  }

  constructor(private http: HttpClient) { }

  /////// GET METHODS ///////


  /**
   * @name getLessons
   * @purpose Gets all lessons
   * @returns Observable<Lesson[]> an array of lessons
   */
  getLessons(): Observable<Lesson[]> {
    return this.http.get<Lesson[]>(this.lessonsUrl, this.httpOptions);
  }

  /**
   * @name getLesson 
   * @purpose gets a lesson by id 
   * @param id id of the lesson
   * @returns Observable<Lesson> a lesson with the given id
   */
  getLesson(id: number): Observable<Lesson> {
    const url = `${this.lessonsUrl}/${id}`;
    return this.http.get<Lesson>(url)
      .pipe(tap(),
        catchError(this.handleError<Lesson>(`getLesson id=${id}`))
      );
  }

  /**
   * @name getLessonByDate
   * @Fuction This allows the frontend to retrieve a lesson via a passed in date parameter 
   * @param date the date of the lesson to be obtained
   * @returns a lesson with that given date
   */
  getLessonByDate(date: string): Observable<Lesson[]> {
    const url = `${this.lessonsUrl}/dates/?date=${date}`;
    return this.http.get<Lesson[]>(url).pipe(tap(), catchError(this.handleError<Lesson[]>('getLessonByDate')));
  }


  /**
   * @name getLessonByUser
   * @purpose gets a users lessons by username
   * @param username the username of the user and their associated lessons
   * @returns the lessons associated with the given username
   */
  getLessonByUser(username: string): Observable<Lesson[]> {
    const url = `${this.lessonsUrl}/user/${username}`
    return this.http.get<Lesson[]>(url).pipe(tap(), catchError(this.handleError<Lesson[]>('getLessonByUser')));
  }

  /////// SAVE METHODS ///////


  /**
   * @name addLesson
   * @purpose adds a lesson to the database/json object file
   * @param lesson the lesson to be added
   * @returns an observable of the lesson that was added
   */
  addLesson(lesson: Lesson): Observable<Lesson> {
    const url = `${this.lessonsUrl}`;
    return this.http.post<Lesson>(url, lesson, this.httpOptions)
      .pipe(tap(),
        catchError(this.handleError<Lesson>('addLesson'))
      );
  }

  /**
   * @name deleteLesson
   * @purpose deletes a lesson from the database
   * @param id id of the lesson to be deleted
   * @returns  the lesson to be deleted
   */
  deleteLesson(id: number): Observable<Lesson> {
    const url = `${this.lessonsUrl}/${id}`;
    return this.http.delete<Lesson>(url, this.httpOptions)
      .pipe(tap(),
        catchError(this.handleError<any>('deleteLesson'))
      );
  }

  /**
   * @name updateLesson
   * @purpose updates the lesson that was passed in
   * @param lesson lesson to updated
   * @returns an updated lesson
   */
  updateLesson(lesson: Lesson): Observable<Lesson> {
    const url = `${this.lessonsUrl}`;
    return this.http.put<Lesson>(url, lesson, this.httpOptions)
      .pipe(tap(),
        catchError(this.handleError<Lesson>('updateLesson'))
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

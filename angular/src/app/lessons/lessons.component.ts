import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { LoginService } from '../login.service';
import { LessonService } from '../lesson.service';
import { Lesson } from '../lessons';

@Component({
  selector: 'app-lessons',
  templateUrl: './lessons.component.html',
  styleUrls: ['./lessons.component.css']
})
export class LessonsComponent implements OnInit, OnChanges {
  private lessonsService: LessonService;
  lessons: Lesson[] = [];
  lesson: Lesson | undefined;

  @Input() date: string = '';

  constructor(lessonService: LessonService) { this.lessonsService = lessonService; }


  ngOnChanges(changes: SimpleChanges): void {
    throw new Error('Method not implemented.');
  }

  ngOnInit(): void {
    this.getLessons();
    this.getDate();
  }

  getLessons(): void {
    this.lessonsService.getLessons().subscribe(lessons => this.lessons = lessons);
  }

  getLessonsByDate(): void {
    if (this.date != "")
      this.lessonsService.getLessonByDate(this.date).subscribe(lessons => this.lessons = lessons);
    else alert("Please select a date first!")
  }

  getDate(): void {
    const date = document.getElementById('selectedDate') as HTMLInputElement;
    this.date = date.value;
    //Make Date Format MM-DD-YYYY
    let dateArray = this.date.split("-");
    let month = dateArray[1];
    let day = dateArray[2];
    let year = dateArray[0];
    this.date = month + "/" + day + "/" + year;
    this.date = String(this.date);
  }

  updateDateAndLoadLessons(): void {
    this.getDate()
    this.getLessonsByDate()
  }

  purchaseLesson(lesson: Lesson): void {
    if (LoginService.loggedInUser != LoginService.nullUser) {
      let lesson_info = lesson.title + " every " + lesson.days + " from " + lesson.startDate + " to " + lesson.endDate;
      let warning = "Are you sure you would like to subscribe to:\n" + lesson_info + "?\n\n----------\nTotal: " + lesson.price + "\n----------"
      if (confirm(warning)) {
        lesson.username = LoginService.loggedInUser.username;
        this.lessonsService.updateLesson(lesson).subscribe(_ => {
          let receipt = "Thank you for subscribing to:";
          alert(receipt + "\n" + lesson_info + "\n\n----------\nTotal: " + lesson.price + "$\nWe hope to see you again soon!\n----------");
          this.getLessons();
        });
      }
    }
  }

  /**

  OnChanges() {
    if (this.date) {
      this.getLessonsByDate(this.date);
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    for (const propName in changes) {
      if (changes.hasOwnProperty(propName)) {
        switch (propName) {
          case 'date': {
            this.getLessonsByDate(this.date);
          }
        }
      }
    }
  }
  */
}


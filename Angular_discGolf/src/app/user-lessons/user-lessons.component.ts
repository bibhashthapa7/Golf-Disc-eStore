import { Component, OnInit } from '@angular/core';
import { Lesson } from '../lessons';
import { LessonService } from '../lesson.service';
import { LoginService } from '../login.service';
import { User } from '../user';
@Component({
  selector: 'app-user-lessons',
  templateUrl: './user-lessons.component.html',
  styleUrls: ['./user-lessons.component.css']
})
export class UserLessonsComponent implements OnInit {

  private lessonsService: LessonService
  lesson: Lesson | undefined;
  lessons: Lesson[] = [];
  user: User = LoginService.loggedInUser;
  canSeeLessons: boolean = false;

  constructor(lessonService: LessonService) {
    this.lessonsService = lessonService;
  }

  ngOnInit(): void {
    this.update();
  }

  update(): void {
    this.lessonsService.getLessonByUser(this.user.username as string).subscribe(lessons => {
      this.lessons = lessons;
      this.canSeeLessons = (lessons.length > 0);
      console.log("REASSESSED " + this.canSeeLessons);
    });
  }

  removeLesson(lesson: Lesson): void {
    if (this.user != LoginService.nullUser) {
      let warning = "Are you sure you'd like to unsubscribe from:\n";
      let lesson_info = lesson.title + " every " + lesson.days + " from " + lesson.startDate + " to " + lesson.endDate;

      if (confirm(warning + lesson_info + "?")) {
        lesson.username = null;
        this.lessonsService.updateLesson(lesson).subscribe(_ => {
          let receipt = "We're sorry to see you go... You've unsubscribed from:";
          alert(receipt + "\n" + lesson_info + "\n\n----------\nTotal refund: " + lesson.price*0.15 + "$\nBut we hope to see you again soon!\n----------");
          this.update();
        });
      }
    }
  }
}
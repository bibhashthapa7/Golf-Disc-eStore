import { Component, OnInit, Input } from '@angular/core';
import { Lesson } from '../lessons';
import { LessonService } from '../lesson.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-view-lessons',
  templateUrl: './view-lessons.component.html',
  styleUrls: ['./view-lessons.component.css']
})
export class ViewLessonsComponent implements OnInit {

  lessons: Array<Lesson> = []
  lessonsService: LessonService;
  router: Router

  /*
    input values
  */
    @Input() lessonTitle?: string;
    @Input() lessonDescription?: string;
    @Input() lessonDays?: string;
    @Input() lessonStartDate?: string;
    @Input() lessonEndDate?: string;
    @Input() lessonPrice?: number;

  constructor(lessonsService: LessonService, router: Router) {
    this.lessonsService = lessonsService;
    this.router = router;
  }

  ngOnInit(): void {
    this.update();
  }

  update(): void {
    this.lessonsService.getLessons().subscribe(result => {
      let a: Array<Lesson> = [];
      result.forEach(e => {
        a.push({
          id:e.id,
          username:e.username,
          title:e.title,
          description:e.description,
          days:e.days,
          startDate:this.convertDate(e.startDate),
          endDate:this.convertDate(e.endDate),
          price:e.price
        })
      })
      this.lessons = a;
    });
  }

  convertDate(date: string): string {
    let sDate = date.split("/");
    return sDate[2] + "-" + sDate[0] + "-" + sDate[1];
  }
  
  /**
   * Method to update a lesson in the inventory. 
   * @param lessonId lesson Id being updated
   */
   updateLesson(lessonId: number) {
    // access information from html input field id's
    console.log("updating lesson id: " + lessonId)
    const title = document.getElementById('lessonTitle' + lessonId) as HTMLInputElement;
    const description = document.getElementById('lessonDescription' + lessonId) as HTMLInputElement;
    const days = document.getElementById('lessonDays' + lessonId) as HTMLSelectElement;
    const startDate = document.getElementById('lessonStartDate' + lessonId) as HTMLInputElement;
    const endDate = document.getElementById('lessonEndDate' + lessonId) as HTMLInputElement;
    const price = document.getElementById('lessonPrice' + lessonId) as HTMLInputElement;

    let sDate = startDate.value;
    let eDate = endDate.value;
    let sDateArray = sDate.split("-");
    let eDateArray = eDate.split("-");
    let sMonth = sDateArray[1];
    let sDay = sDateArray[2];
    let sYear = sDateArray[0];
    let eMonth = eDateArray[1];
    let eDay = eDateArray[2];
    let eYear = eDateArray[0];
    sDate = sMonth + "/" + sDay + "/" + sYear;
    eDate = eMonth + "/" + eDay + "/" + eYear;
    sDate = String(sDate);
    eDate = String(eDate);
    
    const updatedLesson: Lesson = {id:lessonId, username:null, title:title.value, description:description.value, 
      days:days.value, startDate:sDate, endDate:eDate, price:Number(price.value)}
    this.lessonsService.updateLesson(updatedLesson).subscribe(_ => alert("Lesson updated successfully."))
  }

  deleteLesson(lessonId: number) {
    if (confirm("Are you sure you want to delete this lesson?")) {
      this.lessonsService.deleteLesson(Number(lessonId)).subscribe(_ => this.update());
    }
  }

  checkDate(startDate: string | undefined, endDate: string | undefined): boolean {
    if (startDate == undefined || endDate == undefined) {
      return false;
    }
    if (startDate.length == 0 || endDate.length == 0) {
      return false;
    }

    let start = startDate.toString().split("-");
    let end = endDate.toString().split("-");

    if (start.length != 3 || end.length != 3) {
      return false;
    }
    if (start[2].length !=2 || end[2].length != 2) {
      return false;
    }
    if (start[1].length != 2 || end[1].length != 2) {
      return false;
    }
    if (start[0].length != 4 || end[0].length != 4) {
      return false;
    }

    let startYear = parseInt(start[0]);
    let endYear = parseInt(end[0]);
    let startMonth = parseInt(start[1]);
    let endMonth = parseInt(end[1]);
    let startDay = parseInt(start[2]);
    let endDay = parseInt(end[2]);
    if (startYear > endYear) {
      return false;
    }
    if (startYear == endYear) {
      if (startMonth > endMonth) {
        return false;
      }
      if (startMonth == endMonth) {
        if (startDay > endDay) {
          return false;
        }
      }
    }
    return true;
  }

  createLesson(title: string | undefined, description: string | undefined, days: string | undefined, startDate: string | undefined, endDate: string | undefined, price: number | undefined) {
    if (title == undefined || description == undefined || days == undefined || startDate == undefined || endDate == undefined || price == undefined) {
      alert("You cannot have an empty field.");
      return;
    }
    if (title.length == 0 || description.length == 0 || days.length == 0 || startDate.length == 0 || endDate.length == 0 || price.toString().length == 0) {
      alert("You cannot have an empty field.");
      return;
    }
    if (price < 0) {
      alert("You must enter a valid price.");
      return;
    }
    if(!this.checkDate(startDate,endDate)){
      alert("You must enter a valid date.");
      return;
    }

    let sDateArray = startDate.split("-");
    let eDateArray = endDate.split("-");
    let sMonth = sDateArray[1];
    let sDay = sDateArray[2];
    let sYear = sDateArray[0];
    let eMonth = eDateArray[1];
    let eDay = eDateArray[2];
    let eYear = eDateArray[0];
    startDate = sMonth + "/" + sDay + "/" + sYear;
    endDate = eMonth + "/" + eDay + "/" + eYear;
    
    this.lessonsService.addLesson({ id: 0, username: null, title: title, description: description, days: days, startDate: startDate, endDate: endDate, price: price }).subscribe(_ => {
      this.update();
      alert("Lesson created successfully.");
    });
  }
}

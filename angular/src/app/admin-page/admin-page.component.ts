import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from '../login.service';
import { LoginComponent } from '../login/login.component';
import { Location } from '@angular/common';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.css']
})
export class AdminPageComponent implements OnInit {

  constructor(private location:Location) { 
    
  }
  

  ngOnInit(): void {
  }
  goBack(): void {
    this.location.back();
  }

}

  



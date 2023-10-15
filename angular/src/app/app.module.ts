import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ProductComponent } from './product/product.component';
import { FormsModule } from '@angular/forms';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { LoginComponent } from './login/login.component'; // <-- NgModel lives here
import { HttpClientModule } from '@angular/common/http'
import { HomePageComponent } from './home-page/home-page.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { CreateproductComponent } from './createproduct/createproduct.component';
import { InventoryComponent } from './inventory/inventory.component';
import { ProductSearchComponent } from './product-search/product-search.component';
import { LessonsComponent } from './lessons/lessons.component';
import { ViewLessonsComponent } from './view-lessons/view-lessons.component';
import { UserLessonsComponent } from './user-lessons/user-lessons.component'; // <-- NgModel lives here

@NgModule({
  declarations: [
    AppComponent,
    ProductComponent,
    ProductDetailComponent,
    LoginComponent,
    HomePageComponent,
    ShoppingCartComponent,
    AdminPageComponent,
    CreateproductComponent,
    InventoryComponent,
    ProductSearchComponent,
    LessonsComponent,
    ViewLessonsComponent,
    UserLessonsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

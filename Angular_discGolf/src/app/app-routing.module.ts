import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ProductComponent } from './product/product.component';
import { HomePageComponent } from './home-page/home-page.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { AdminPageComponent } from './admin-page/admin-page.component';
import { CreateproductComponent } from './createproduct/createproduct.component';
import { InventoryComponent } from './inventory/inventory.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { LessonsComponent } from './lessons/lessons.component';
import { ViewLessonsComponent } from './view-lessons/view-lessons.component';
import { UserLessonsComponent } from './user-lessons/user-lessons.component';
const routes: Routes =[
  {path: 'products', component: ProductComponent},
  {path: 'login', component: LoginComponent},
  {path: 'home', component: HomePageComponent},
  {path: 'product/:id', component: ProductDetailComponent},
  {path: 'admin', component: AdminPageComponent},
  {path: 'createproduct', component: CreateproductComponent},
  {path: 'inventory', component: InventoryComponent},
  {path: 'cart', component: ShoppingCartComponent},
  {path: 'lessons', component: LessonsComponent},
  {path: 'viewlessons', component: ViewLessonsComponent},
  {path: 'mylessons',component:UserLessonsComponent},
  {path: '', redirectTo: '/home', pathMatch: 'full'},
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
  
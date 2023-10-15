import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../product';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ProductService } from '../product.service';
import { LoginService } from '../login.service';
import { User } from '../user';
import { ShoppingCartService } from '../shopping-cart.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {

  product: Product | undefined;
  private cartService: ShoppingCartService;

  constructor(private route:ActivatedRoute,
    private productService: ProductService,
    private location:Location,
    cartService: ShoppingCartService) {
      this.cartService =cartService;
     }

  ngOnInit(): void {
   this.getProduct();
  }

  getProduct(): void {
    const id = String(this.route.snapshot.paramMap.get('id'));
    this.productService.getProduct(id).subscribe(product => this.product = product);
  }
  goBack(): void {
    this.location.back();
  }
  getImagePath(color: string): string {
    return "assets/images/discs/" + color.toLowerCase() + ".png";
  }

  canPurchase(): boolean {
    return LoginService.loggedInUser != LoginService.nullUser
  }

  addToCart(id: string | null): void {
    const user: User = LoginService.loggedInUser;
    if (user != LoginService.nullUser)
      this.cartService.addProduct(user.username, Number(id)).subscribe();
  }
}

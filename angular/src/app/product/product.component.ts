import { Component, OnInit } from '@angular/core';
import { ProductService } from '../product.service';
import { ShoppingCartService } from '../shopping-cart.service';
import { LoginService } from '../login.service';
import { Product } from '../product';
import { User } from '../user';
import { Router } from '@angular/router';

/**
 * @name ProductComponent
 * @description This component is used to display a list of products
 * @author Nikhil Patil + Bibhash Thapa + Coolname
 */
@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {
  private productService: ProductService;
  private cartService: ShoppingCartService;
  private router: Router;
  products: Product[] = [];

  // Constructor
  constructor(productService: ProductService, cartService: ShoppingCartService, router: Router) {
    this.productService = productService;
    this.cartService = cartService;
    this.router = router;

  }

  // Intialization
  ngOnInit(): void {
    this.getProducts();
  }


  /**
   * @name getProducts
   * @purpose gets all products from the database
   */
  getProducts(): void {
    this.productService.getProducts().subscribe(products => this.products = products);
  }

  /**
   * @name addToCart
   * @purpose Adds a product to the cart.
   * @param id the id of the product to be added to the cart
   */
  addToCart(id: string | null): void {
    const user: User = LoginService.loggedInUser;
    if (user != LoginService.nullUser)
      this.cartService.addProduct(user.username, Number(id)).subscribe();
  }

  /**
   * @name canPurchase
   * @purpose determines if a user can purchase a product
   * @returns true if the user is logged in, false if the user is null
   */
  canPurchase(): boolean {
    return LoginService.loggedInUser != LoginService.nullUser
  }


  /**
   * @name getImagePath 
   * @purpose gets the image path of a product
   * @param color the color of the product
   * @returns a string path to the disc image
   */
  getImagePath(color: string): string {
    return "assets/images/discs/" + color.toLowerCase() + ".png";
  }

  /**
   * @name viewDetails
   * @purpose routerlinks to product's detail page
   * @param id the id of the product
   */
  viewDetails(id: string): void {
    this.router.navigateByUrl('/product/' + id)
  }

}
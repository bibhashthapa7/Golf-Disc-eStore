import { Component, Input, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';

import { ShoppingCartService } from '../shopping-cart.service';
import { ProductService } from '../product.service';
import { LoginService } from '../login.service';
import { Product } from '../product';
import { User } from '../user';


@Component(
  {
    selector: 'app-shopping-cart',
    templateUrl: './shopping-cart.component.html',
    styleUrls: ['./shopping-cart.component.css']
  }
)

export class ShoppingCartComponent implements OnInit {
  cartService: ShoppingCartService;
  productService: ProductService;
  products: Array<Product> = [];
  router: Router;
  user: User = LoginService.nullUser;
  someSubscription: any;
  canPurchase: Boolean = false;
  count: number = 0;
  cost: number = 0;

  // @Input() productQuantity?: number;

  constructor(cartService: ShoppingCartService, productService: ProductService, router: Router, loginService: LoginService) {
    this.cartService = cartService;
    this.productService = productService;
    this.router = router;

    this.router.events.subscribe((e) => {
      if (e instanceof NavigationEnd) {
         this.update();
      }
    });

    this.router.routeReuseStrategy.shouldReuseRoute = function () {
      return false;
    };

    this.someSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        // Tell the router that, you didn't visit or load the page previously, so mark the navigated flag to false as below.
        this.router.navigated = false;
      }
    });
  }

  ngOnInit(): void { }

  ngOnDestroy() {
    if (this.someSubscription) {
      this.someSubscription.unsubscribe();
    }
  }

  getImagePath(color: string): string {
    return "assets/images/discs/" + color.toLowerCase() + ".png";
  }

  /**
   * Updates the cart.
   */
  update(): void {
    this.user = LoginService.loggedInUser;
    // Update the cart contents:
    if (this.user != LoginService.nullUser) {
      this.cartService.getProducts(this.user.username).subscribe(result => {
        this.products = result;
        this.canPurchase = (result.length > 0);
        console.log("REASSESSED " + this.canPurchase)
      });

      // Update the cost:
      this.cartService.getCost(this.user.username).subscribe(result => {
        this.cost = result;
      });

      // Update the item count:
      this.cartService.getCount(this.user.username).subscribe(result => {
        this.count = result;
      });

    } else {
      this.canPurchase = false;
    }
  }

  /**
   * Updates a product's quantity in the cart.
   */
  updateProduct(id: string | null, quantity: number): void {
    if (quantity > 0 || confirm("This item will be removed, are you sure you'd like to proceed?"))
      this.cartService.updateProductQuantity(this.user.username, Number(id), quantity).subscribe(_ => this.update());
  }

  /**
   * Removes a product from the cart.
   */
  removeProduct(id: string | null): void {
    if (confirm("Are you sure?")) {
      this.cartService.deleteProduct(this.user.username, Number(id)).subscribe(_ => this.update());
    }
  }

  /**
   * Purchases the cart.
   */
  purchaseCart(): void {
    if (confirm("Are you sure?")) {
      this.cartService.checkCart(this.user.username).subscribe(result => {
        if (result.length > 0) {
          let warning = "There are only:";
          result.forEach(p => warning += "\n- " + p.quantity + " " + p.color + " " + p.type + "'s");
          
          if (confirm(warning + "\nWould you like to buy all that is available?")) {
            this.handlePurchase();
            console.log(this.user.username + " purchased cart despite conflict.");
          } else return

        } else {
          this.handlePurchase();
          console.log(this.user.username + " purchased cart without conflict.");
        }
      });
    }
  }

  private handlePurchase(): void {
    this.cartService.purchaseCart(this.user.username).subscribe(result => {
      let receipt = "Thank you for purchasing:"
      let total = 0
      result.forEach(p => {
        receipt += "\n- " + p.quantity + " " + p.color + " " + p.type + (p.quantity > 1 ? "'s" : "")
        total += (p.price * p.quantity)
      });
      alert(receipt + "\n----------\nTotal: " + total + "$\nWe hope to see you again soon!\n----------")
      this.update();
    });
  }

  purchaseOne(id: string): void {
    if (confirm("Are you sure?")) {
      this.cartService.checkOne(this.user.username, id).subscribe(result => {
        if (result) {
          let warning = "There is only " + result.quantity + " " + result.color + " " + result.type + "'s";
          
          if (confirm(warning + "\nWould you like to buy all that is available?")) {
            this.handleOnePurchase(id);
            console.log(this.user.username + " purchased item despite conflict.");
          } else return

        } else {
          this.handleOnePurchase(id);
          console.log(this.user.username + " purchased item without conflict.");
        }
      });
    }
  }

  private handleOnePurchase(id: string): void {
    this.cartService.purchaseOne(this.user.username, id).subscribe(result => {
      let receipt = "Thank you for purchasing:\n" + result.quantity + " " + result.color + " " + result.type + (result.quantity > 1 ? "'s" : "")
      let total = (result.price * result.quantity)
      alert(receipt + "\n----------\nTotal: " + total + "$\nWe hope to see you again soon!\n----------")
      this.update();
    });
  }
}
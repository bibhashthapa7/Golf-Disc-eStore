import { Component, Inject, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from '../product';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css']
})

export class InventoryComponent implements OnInit {

  productService: ProductService;
  products: Array<Product> = []
  router: Router
  
  /*
    input values
  */
  @Input() productColor?: string;
  @Input() productWeight?: number;
  @Input() productType?: string;
  @Input() productPrice?: number;
  @Input() productQuantity?: number;

  constructor(productService: ProductService, router: Router) {
    this.productService = productService;
    this.router = router;
  }
  
  ngOnInit(): void {
    // on initialization subscribe to product service and set product array.
    this.update();
  }

  update(): void {
    this.productService.getProducts().subscribe(result => {
      this.products = result
    });
  }

  /**
   * Method to update a product in the inventory. 
   * @param productId product Id being updated
   */
  updateProduct(productId: string) {
    // access information from html input field id's
    console.log("updating product id: " + productId)
    const color = document.getElementById('productColor' + productId) as HTMLInputElement;
    const weight = document.getElementById('productWeight' + productId) as HTMLInputElement;
    const type = document.getElementById('productType' + productId) as HTMLSelectElement;
    const price = document.getElementById('productPrice' + productId) as HTMLInputElement;
    const quantity = document.getElementById('productQuantity' + productId) as HTMLInputElement;
    const updatedProduct: Product = {id:productId, color:color.value, weight:Number(weight.value), 
                      type:type.options[type.selectedIndex].text, price:Number(price.value), quantity:Number(quantity.value)}
    this.productService.updateProduct(updatedProduct).subscribe(_ => {
      alert("Product updated successfully.");
      this.update()
    });
  }

  /**
   * delete product request
   * @param productId product id needing deletion
   */
  deleteProduct(productId: string) {
    // prompt user to ensure no accidental deletions.
    if(confirm("Are you sure you want to delete this product?")) {
      this.productService.deleteProduct(Number(productId)).subscribe(_ => this.update());
    }
  }

  checkType(type: string): boolean {
    type = type.toLowerCase();
    if (type == "distance driver" || type == "midrange" || type == "putter" || type == "fairway driver") {
      return true;
    } else {
      return false;
    }
  }

  createProduct(type: string | undefined, quantity: number | undefined, price: number | undefined, weight: number | undefined, color: string | undefined) {
    if (type == undefined || quantity == undefined || price == undefined || weight == undefined || color == undefined) {
      alert("You cannot have an empty field.");
      return;
    }
    if (type.length == 0 || quantity.toString().length == 0 || price.toString().length == 0 || weight.toString().length == 0 || color.length == 0) {
      alert("You cannot have an empty field.");
      return;
    }
    if (!this.checkType(type)) {
      alert("You must enter a valid type.");
      return;
    }
    if (quantity < 0) {
      alert("You must enter a valid quantity.");
      return;
    }
    if (price < 0) {
      alert("You must enter a valid price.");
      return;
    }
    if (weight < 0) {
      alert("You must enter a valid weight.");
      return;
    }
    if (color.length > 20) {
      alert("You must enter a valid color.");
      return;
    }
    this.productService.addProduct({ id: "0", type: type, quantity: quantity, price: price, weight: weight, color: color }).subscribe(_ => {
      this.update();
      alert("Product created successfully.");
    });
  }
}

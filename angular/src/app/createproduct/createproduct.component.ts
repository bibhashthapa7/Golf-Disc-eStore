import { Component, Input, OnInit } from '@angular/core';
import { Product } from '../product';
import { ProductService } from '../product.service';

import { Location } from '@angular/common';
@Component({
  selector: 'app-createproduct',
  templateUrl: './createproduct.component.html',
  styleUrls: ['./createproduct.component.css']
})
export class CreateproductComponent implements OnInit {
  @Input() type?: string;
  @Input() quantity?: number;
  @Input() price?: number;
  @Input() weight?: number;
  @Input() color?: string;


  private productService: ProductService;
  constructor(service: ProductService,private location:Location) {
    this.productService = service;
  }




  ngOnInit(): void {
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
    const res = this.productService.addProduct({ id: "0", type: type, quantity: quantity, price: price, weight: weight, color: color });
    res.subscribe();
    setTimeout(() => {
      alert("Product created successfully.");
    }, 1000);
  }

  goBack(): void {
    this.location.back();
  }
}

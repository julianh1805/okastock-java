import { WishlistService } from './../../../services/wishlist.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.scss']
})
export class WishlistComponent implements OnInit, OnDestroy {
  isLoading = false;
  products: any[];
  pageSize = 25;
  productsLength = 0;
  page = 1;
  user: any;
  id = null;
  token = null;
  editing = false;
  notProducts = false;

  constructor(private wishlistService: WishlistService, private router: Router) { }

  ngOnInit() {
    if (window.innerWidth <= 767) {
      this.pageSize = 14
    }
    this.isLoading = true;
    this.id = JSON.parse(localStorage.getItem('user')).id;
    this.wishlistService.getWishlist(this.id).subscribe(products => {
      this.products = products;
      this.productsLength = this.products.length;
      this.products.map(product => {
        product.price = product.price.toFixed(2);
        product.price = product.price.replace('.', ',');
      })
      if (products.length < 1) {
        this.notProducts = true;
      }
      this.isLoading = false;
    }, error => {
      console.log(error);
    })
  }

  updateOnWishlist(i) {
    this.products.splice(i, 1);
    if (this.products.length < 1) {
      this.notProducts = true;
    }
    this.wishlistService.updateOnWishlist(this.id, this.products).subscribe(res => {
    }, error => {
      console.log(error)
    })
  }

  ngOnDestroy() {
    this.wishlistService.updateOnWishlist(this.id, this.products);
  }

}

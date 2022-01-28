import { CartService } from './../../../services/cart.service';
import { ConexionModalComponent } from './../../../globals/conexion-modal/conexion-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NotificationService } from './../../../services/notification.service';
import { WishlistService } from './../../../services/wishlist.service';
import { ProductsService } from './../../../services/products.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {
  product;
  user;
  id;
  quantity = 1;
  isLoading = false;
  cartProductsLength: number;
  maxProducts = 6;
  bootstrap = false

  constructor(
    private route: ActivatedRoute,
    private productsService: ProductsService,
    private router: Router,
    private wishlistService: WishlistService,
    private notificationService: NotificationService,
    private modalService: NgbModal,
    private cartService: CartService
  ) { }

  ngOnInit() {
    this.isLoading = true;
    this.getId();
    const id = this.route.snapshot.params['id'];
    this.getProduct(id);
    if (window.innerWidth <= 1139 && window.innerWidth >= 960) {
      this.maxProducts = 4;
    } else if (window.innerWidth <= 1240 && window.innerWidth >= 1140) {
      this.maxProducts = 5;
    }
    else {
      this.maxProducts = 6;
    }
    this.isLoading = false;
  }

  changeProduct(id) {
    this.router.navigate(['/produits', id])
      .then(() => {
        this.ngOnInit();
      })
  }

  getProduct(id) {
    this.productsService.getProduct(id).subscribe(product => {
      this.product = product;
      if (product.price.toString().includes('.')) {
        product.price = product.price.toFixed(2).replace('.', ',')
      }
      this.product.user.logo = product.user.logo;
      const otherProducts = this.product.user.products.filter(product => product._id != id);
      this.product.user.products = otherProducts;
      this.product.user.products.map(product => {
        if (product.price.toString().includes('.')) {
          product.price = product.price.toFixed(2).replace('.', ',')
        }
      })
    })
  }

  addToWishlist() {
    this.getId();
    let product = [this.product._id]
    if (!this.id) {
      const modalRef = this.modalService.open(ConexionModalComponent);
      modalRef.result.then(boolean => {
        if (boolean) {
          this.getId();
          this.wishlistFunction(this.id, product)
        }
      });
    } else {
      this.wishlistFunction(this.id, product)
    }
  }

  addToCart(quantity) {
    this.getId();
    let product = [this.product._id, quantity]
    if (!this.id) {
      const modalRef = this.modalService.open(ConexionModalComponent);
      modalRef.result.then(boolean => {
        if (boolean) {
          this.getId();
          this.cartFunction(this.id, product, quantity)
        }
      });
    } else {
      this.cartFunction(this.id, product, quantity);
    }
  }

  wishlistFunction(userId, product) {
    this.wishlistService.addToWishlist(userId, product).subscribe(res => {
      this.notificationService.sendNotification({ message: res.message, type: res.type });
    }, error => {
      this.notificationService.sendNotification({ message: error.error.message, type: error.error.type });
    })
  }

  cartFunction(userId, product, quantity) {
    this.cartService.addToCart(userId, product).subscribe(res => {
      this.notificationService.sendNotification({ message: res.message, type: res.type });
      this.quantity = 1;
      this.product.quantity = this.product.quantity - quantity;
      this.cartService.getCartProductsLength(this.id);
    }, error => {
      this.notificationService.sendNotification({ message: error.error.message, type: error.error.type });
    })
  }

  getId() {
    let ls = JSON.parse(localStorage.getItem('user'));
    if (ls) {
      this.id = ls.id;
    };
  }

}

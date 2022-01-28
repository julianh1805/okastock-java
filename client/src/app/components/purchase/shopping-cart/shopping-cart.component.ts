import { Router } from '@angular/router';
import { ValidationModalComponent } from './../../../globals/validation-modal/validation-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CartService } from './../../../services/cart.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.scss']
})
export class ShoppingCartComponent implements OnInit {
  isLoading = false;
  id;
  products = [];
  notProducts = false;
  cartProductsLength: number;
  totalPrice: any = 0;
  totalCartPrice: any = 0;

  deliveryOptions = [
    { type: "24h", value: "7,99" },
    { type: "72h", value: "3,99" }
  ]
  deliveryCost = "3,99";
  initDeliveryCost = 3.99

  constructor(
    private cartService: CartService,
    private modalService: NgbModal,
    private router: Router
  ) { }

  ngOnInit() {
    this.isLoading = true;
    this.id = JSON.parse(localStorage.getItem('user')).id;
    this.cartService.getProductsCart(this.id).subscribe(products => {
      this.products = products;
      this.products.map(product => {
        product.totalPrice = product.price * product.quantity;
        this.totalPrice = this.totalPrice + product.totalPrice;
        if (product.totalPrice.toString().includes('.')) {
          product.totalPrice = product.totalPrice.toFixed(2).replace('.', ',');
        }
        if (product.price.toString().includes('.')) {
          product.unityPrice = product.price.toFixed(2).replace('.', ',');
        } else {
          product.unityPrice = product.price
        }
      })
      this.totalCartPrice = (this.totalPrice + this.initDeliveryCost).toFixed(2).replace('.', ',');
      this.totalPrice = this.totalPrice.toFixed(2).replace('.', ',')
      if (products.length < 1) {
        this.notProducts = true;
      }
      this.isLoading = false;
    }, error => {
      console.log(error);
    })
  }

  onChange(deliveryValue) {
    this.totalCartPrice = Number(this.totalPrice.replace(',', '.')) + Number(deliveryValue.replace(',', '.'));
    this.totalCartPrice = this.totalCartPrice.toFixed(2).replace('.', ',');
  }

  deleteProduct(productTitle, productId, index, quantity, price, deliveryValue) {
    const modalRef = this.modalService.open(ValidationModalComponent);
    modalRef.componentInstance.productCartTitle = productTitle;
    modalRef.result.then(boolean => {
      if (boolean) {
        this.cartService.deleteProduct(this.id, productId, quantity).subscribe(res => {
          this.products.splice(index, 1);
          this.products.map(product => {
            product.totalPrice = product.price * product.quantity;
            this.totalPrice = 0;
            this.totalPrice = this.totalPrice + product.totalPrice;
            if (product.totalPrice.toString().includes('.')) {
              product.totalPrice = product.totalPrice.toFixed(2).replace('.', ',');
            }

          })
          if (deliveryValue === "3,99") {
            this.totalCartPrice = this.totalPrice + 3.99;
          } else {
            this.totalCartPrice = this.totalPrice + 7.99;
          }
          this.cartService.productsLength.next(this.products.length);
          if (this.products.length < 1) {
            let shoppingCart = {
              totalPrice: 0,
              deliveryCost: 0
            }
            this.cartService.setCart(this.id, shoppingCart).subscribe(res => {
            }, error => {
              console.log(error);
            })
            this.notProducts = true
          }
          else {
            this.totalPrice = this.totalPrice.toFixed(2).replace('.', ',')
            this.totalCartPrice = this.totalCartPrice.toFixed(2).replace('.', ',')
          }
        }, error => {
          console.log(error)
        })
      }
    }
    )
  }

  validCart(deliveryValue) {
    let shoppingCart = {
      totalPrice: Number(this.totalPrice.toString().replace(',', '.')),
      deliveryCost: Number(deliveryValue.toString().replace(',', '.')),
    }
    this.cartService.setCart(this.id, shoppingCart).subscribe(res => {
      this.router.navigate(['/panier/livraison'])
    }, error => {
      console.log(error);
    })
  }
}

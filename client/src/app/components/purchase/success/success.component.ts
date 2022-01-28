import { Router } from '@angular/router';
import { CartService } from './../../../services/cart.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-success',
  templateUrl: './success.component.html',
  styleUrls: ['./success.component.scss']
})
export class SuccessComponent implements OnInit {
  cartProductsLength = 4;
  id;
  isLoading = false;

  constructor(private cartService: CartService, private router: Router) { }

  ngOnInit() {
    this.isLoading = true;
    this.id = JSON.parse(localStorage.getItem('user')).id;
    this.cartService.getCart(this.id).subscribe(cart => {
      if (!cart.paid) {
        if (cart.deliveryCost === 0 || cart.totalCost === 0) {
          this.router.navigate(['/panier/recapitulatif'])
        } else if (!cart.address || !cart.city || !cart.postalCode || !cart.telephone || !cart.recipient) {
          this.router.navigate(['/panier/livraison'])
        }
        else {
          this.router.navigate(['/panier/paiement'])
        }
      } else {
        this.cartService.emptyCart(this.id).subscribe(res => {
          this.cartService.productsLength.next(0);
        }, error => {
          console.log(error);
        })
      }
    }, error => {
      console.log(error)
    })
    this.isLoading = false;
  }

}

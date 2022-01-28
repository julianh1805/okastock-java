import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { CartService } from './../../../services/cart.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-delivery',
  templateUrl: './delivery.component.html',
  styleUrls: ['./delivery.component.scss']
})
export class DeliveryComponent implements OnInit {
  id;
  isLoading = false;
  submitted = false;
  cartPrice: number;

  deliveryForm = new FormGroup({
    recipient: new FormControl('', Validators.required),
    address: new FormControl('', Validators.required),
    complementAddress: new FormControl(''),
    city: new FormControl('', [Validators.required, Validators.pattern('^[a-zA-Z \-\']+')]),
    postalCode: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(5), Validators.pattern("^(([0-9][1-9])|(9[0-5]))[0-9]{3}$")]),
    telephone: new FormControl('', [Validators.required, Validators.minLength(10), Validators.maxLength(10), Validators.pattern("^((06)|(07)|(02))[0-9]{8}$")]),
  })


  constructor(private cartService: CartService, private router: Router) {
  }

  ngOnInit() {
    this.isLoading = true
    this.id = JSON.parse(localStorage.getItem('user')).id;
    this.cartService.getCart(this.id).subscribe(cart => {
      if (cart.totalPrice != 0 || cart.totalPrice != 0) {
        this.cartPrice = (cart.totalPrice + cart.deliveryCost).toFixed(2).replace('.', ',');;
        this.deliveryForm.patchValue({
          recipient: cart.recipient,
          address: cart.address,
          complementAddress: cart.complementAddress,
          city: cart.city,
        })
        if (cart.postalCode) {
          this.deliveryForm.patchValue({
            postalCode: cart.postalCode,
          })
        }
        if (cart.telephone) {
          this.deliveryForm.patchValue({
            telephone: cart.telephone,
          })
        }
      } else {
        this.router.navigate(['/panier/recapitulatif'])
      }
    }, error => {
      console.log(error)
    })
    this.isLoading = false
  }

  onSubmit(form) {
    this.submitted = true;
    if (form) {
      if (!form.valid) {
        this.submitted = true;
      }
      else {
        let deliveryInfo = {
          recipient: form.value.recipient,
          address: form.value.address,
          complementAddress: form.value.complementAddress,
          city: form.value.city,
          postalCode: form.value.postalCode,
          telephone: form.value.telephone,
        }
        this.cartService.setCart(this.id, deliveryInfo).subscribe(res => {
          this.router.navigate(['/panier/paiement'])
        }, error => {
          console.log(error)
        })
      }
    }
  }

}

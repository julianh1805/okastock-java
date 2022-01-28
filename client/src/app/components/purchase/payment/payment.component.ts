import { CartService } from './../../../services/cart.service';
import { Router } from '@angular/router';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent implements OnInit {
  submitted = false;
  isLoading = false;
  id;
  cartPrice = 0;
  ngCartNumber;

  payementForm = new FormGroup({
    holder: new FormControl('', [Validators.required, Validators.pattern("^[a-zA-Z ]*$")]),
    cartNumber: new FormControl('', [Validators.required, Validators.minLength(16), Validators.maxLength(16), Validators.pattern("[0-9]+")]),
    cartExpirationMonth: new FormControl('', Validators.required),
    cartExpirationYear: new FormControl('', Validators.required),
    cartCode: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(3)]),
  })

  expirationMonth = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12'];
  expirationYear = ['2021', '2022', '2023', '2024', '2025', '2026', '2O27', '2028', '2029', '2030'];

  cartExpirationMonthValue = '01';
  cartExpirationYearValue = '2021';

  constructor(private router: Router, private cartService: CartService) { }

  ngOnInit() {
    this.isLoading = true;
    this.id = JSON.parse(localStorage.getItem('user')).id;
    this.cartService.getCart(this.id).subscribe(cart => {
      this.cartPrice = (cart.totalPrice + cart.deliveryCost).toFixed(2).replace('.', ',');
      if (!cart.address || !cart.city || !cart.postalCode || !cart.telephone || !cart.recipient) {
        if (cart.deliveryCost === 0 || cart.totalCost === 0) {
          this.router.navigate(['/panier/recapitulatif'])
        } else {
          this.router.navigate(['/panier/livraison'])
        }
      } else {
        return
      }
    }, error => {
      console.log(error)
    })
    this.isLoading = false;
  }

  cartSpaces(event) {
    event.target.value = event.target.value.replace(/[^\dA-Z]/g, '').replace(/(.{4})/g, '$1 ').trim();
    this.ngCartNumber = event.target.value
  }

  onSubmit(form) {
    if (form) {
      if (!form.valid) {
        this.submitted = true;
      }
      else {
        let paiementInfo = {
          paid: true
        }
        this.cartService.setCart(this.id, paiementInfo).subscribe(cart => {
          this.router.navigate(['/panier'])
        }, error => {
          console.log(error)
        })
      }
    }
  }

}

import { Subject } from 'rxjs';
import { environment } from './../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const path = '/cart';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  productsLength = new Subject<number>();

  constructor(private http: HttpClient) {

  }

  getCart(id) {
    return this.http.get<any>(environment.API + path + `/${id}`);
  }

  getProductsCart(id) {
    return this.http.get<any>(environment.API + path + `/products/${id}`);
  }

  addToCart(id, product) {
    return this.http.post<any>(environment.API + path + `/add/${id}`, product)
  }

  setCart(id, cartInfo) {
    return this.http.post<any>(environment.API + path + `/set/${id}`, cartInfo)
  }

  deleteProduct(id, product, quantity) {
    return this.http.delete<any>(environment.API + path + `/${id}/${product}/${quantity}`);
  }

  emptyCart(id) {
    return this.http.get<any>(environment.API + path + `/empty/${id}`)
  }

  getCartProductsLength(id) {
    this.getProductsCart(id).subscribe(products => {
      this.productsLength.next(products.length)
    }, error => {
      console.log(error)
    })
  }
}

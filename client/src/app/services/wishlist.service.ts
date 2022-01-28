import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const path = '/wishlist';

@Injectable({
  providedIn: 'root'
})
export class WishlistService {

  constructor(private http: HttpClient) { }

  getWishlist(id) {
    return this.http.get<any>(environment.API + path + `/${id}`);
  }

  updateOnWishlist(id, products) {
    return this.http.post<any>(environment.API + path + `/update/${id}`, products);
  }

  addToWishlist(id, product) {
    return this.http.post<any>(environment.API + path + `/add/${id}`, product);
  }
}
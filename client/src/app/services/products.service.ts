import { Subject } from 'rxjs';
import { environment } from './../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

const path = '/api/v1/products';

@Injectable({
  providedIn: 'root'
})

export class ProductsService {

  productsLength = new Subject<number>();

  constructor(private http: HttpClient) { }

  getProducts() {
    return this.http.get<any>(environment.API + path);
  }

  getProductsByCategory(category) {
    console.log(category)
    return this.http.get<any>(environment.API + `/api/v1/categories/${category}`);
  }

  getProductsByQuery(query) {
    return this.http.get<any>(environment.API + path + `/query?${query}`);
  }

  getProductsById(id) {
    return this.http.get<any>(environment.API + path + `/user/${id}`);
  }

  getProduct(id: string) {
    return this.http.get<any>(environment.API + path + `/${id}`);
  }

  addProduct(product) {
    return this.http.post<any>(environment.API + path, product);
  }

  updateProduct(id, product) {
    return this.http.post<any>(environment.API + path + `/${id}`, product);
  }

  deleteProduct(id: string) {
    return this.http.delete<any>(environment.API + path + `/${id}`)
  }

  deletedProductLength(number) {
    this.productsLength.next(number);
  }
}

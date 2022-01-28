import { Router } from '@angular/router';
import { ProductsService } from './../../../services/products.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ValidationModalComponent } from './../../../globals/validation-modal/validation-modal.component';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {
  isLoading = false;
  pageSize = 25;
  page = 1;
  productsLength = 0;
  products: any[];
  id = null;
  token = null;
  editing = false;
  notProducts = false
  environmentAPI;

  constructor(
    private modalService: NgbModal,
    private productsService: ProductsService,
    private router: Router
  ) { }

  ngOnInit() {
    if (window.innerWidth <= 767) {
      this.pageSize = 14
    }
    this.isLoading = true;
    this.id = JSON.parse(localStorage.getItem('user')).id;
    this.productsService.getProductsById(this.id).subscribe(products => {
      this.products = products;
      this.productsLength = this.products.length;
      this.productsService.deletedProductLength(this.products.length);
      if (products.length < 1) {
        this.notProducts = true
      }
      this.isLoading = false;
    })
  }

  deleteProduct(product) {
    const modalRef = this.modalService.open(ValidationModalComponent);
    modalRef.componentInstance.productTitle = product.title;
    modalRef.result.then(boolean => {
      if (boolean) {
        this.productsService.deleteProduct(product._id).subscribe(() => {
          const index = this.products.indexOf(product);
          if (index !== -1) {
            this.products.splice(index, 1);
            this.productsService.deletedProductLength(this.products.length);
            if (this.products.length < 1) {
              this.notProducts = true
              this.productsLength = 0;
            }
          }
        })
      }
    })
  }

  editProduct(productId) {
    this.router.navigate(['editer-une-annonce', productId])
  }

}

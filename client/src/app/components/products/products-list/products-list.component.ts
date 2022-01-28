import { environment } from 'src/environments/environment';
import { CategoriesService } from './../../../services/categories.service';
import { ProductsService } from './../../../services/products.service';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-products-list',
  templateUrl: './products-list.component.html',
  styleUrls: ['./products-list.component.scss']
})
export class ProductsListComponent implements OnInit, OnDestroy {
  products = [];
  subscriptions: Subscription[];
  filteredSort = false;
  filteredPrice = false;
  categorySelected;
  isLoading = false;
  searchTerm = '';
  showMore = true;
  limitChange = false;
  limitClicked = false;
  categoryDefault = {
    nom: 'Toutes les catégories',
    value: 'categorie',
    description: 'Découvrez toutes les catégories de Okastock. Explorez nos multiples catégories tel que vêtements, ' +
        'bricolage, maison & jardin et plein d\'autres catégories.'
  };

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private productsService: ProductsService,
    private categoriesService: CategoriesService
  ) {
    this.subscriptions = new Array<Subscription>();
  }

  ngOnInit() {
    if (window.innerWidth <= 1239 && window.innerWidth >= 1139) {
      this.limitChange = true;
      this.router.navigate(['./'], { relativeTo: this.route, queryParams: { limit: 9 }, queryParamsHandling: 'merge' });
    }
    this.subscriptions.push(
      this.route.queryParams.subscribe(param => {
        this.categorySelected = this.categoryDefault;
        if (this.route.snapshot.queryParams.category) {
          this.categorySelected = this.categoriesService.getByValue(param.category);
        }
        if (this.route.snapshot.queryParams.term) {
          this.searchTerm = this.route.snapshot.queryParams.term;
        }
        if (this.categorySelected.value !== 'categorie') {
          this.query(this.categorySelected);
        } else {
          this.getProducts();
        }
      })
    );
  }

  getProducts() {
    this.isLoading = true;
    this.productsService.getProducts().subscribe(products => {
      this.products = products;
      /*this.products.map(product => {
        if (product.price.toString().includes('.')) {
          product.price = product.price.toFixed(2).replace('.', ',')
        }
      })
      */
      this.products.length < 11 ? this.showMore = false : true;
      this.isLoading = false;
    });
  }

  limit() {
    this.limitClicked = true;
    if (!this.route.snapshot.queryParams.limit) {
      this.router.navigate(['./'], { relativeTo: this.route, queryParams: { limit: 23 }, queryParamsHandling: 'merge' });
    } else {
      if (this.limitChange) {
        this.router.navigate(['./'], { relativeTo: this.route, queryParams: { limit: +this.route.snapshot.queryParams.limit + 10 }, queryParamsHandling: 'merge' });
      } else {
        this.router.navigate(['./'], { relativeTo: this.route, queryParams: { limit: +this.route.snapshot.queryParams.limit + 12 }, queryParamsHandling: 'merge' });
      }
    }
  }

  sort(param: string = '') {
    this.filteredSort = true;
    this.router.navigate(['./'], { relativeTo: this.route, queryParams: { sort: param }, queryParamsHandling: 'merge' });
  }

  removeSort() {
    this.filteredSort = false;
    this.router.navigate([], { queryParams: { sort: null, }, queryParamsHandling: 'merge' });
  }

  sortInterval(minValue, maxValue) {
    this.filteredPrice = true;
    this.router.navigate(['./'],
        { relativeTo: this.route, queryParams: { min_price: minValue.value, max_price: maxValue.value }, queryParamsHandling: 'merge' });
  }

  removePrice() {
    this.filteredPrice = false;
    this.router.navigate([], { queryParams: { min_price: null, max_price: null }, queryParamsHandling: 'merge' });
  }

  private query(q) {
    console.log(q);
    this.productsService.getProductsByCategory(q.value).subscribe(categorie => {
      console.log(categorie);
      this.products = categorie.produits;
    });
      /*
      // this.isLoading = true;
      this.productsService.getProductsByQuery(q).subscribe(products => {
        if (this.limitChange && !this.limitClicked) {
          (products.length - this.products.length) != 9 ? this.showMore = false : null;
        }
        else if (this.limitChange && this.limitClicked) {
          (products.length - this.products.length) != 10 ? this.showMore = false : null;
        } else if (!this.limitChange && !this.limitClicked) {
          (products.length - this.products.length) != 11 ? this.showMore = false : null;
        } else {
          (products.length - this.products.length) != 12 ? this.showMore = false : null;
        }
        this.products = products;
        this.products.map(product => {
          if (product.price.toString().includes('.')) {
            product.price = product.price.toFixed(2).replace('.', ',')
          }
        })
        // this.isLoading = false;
      })
       */
  }

  /*
  private createQuery() {
    let query = [];
    if (!this.route) return '';
    if (this.route.snapshot.queryParams.sort) query.push(`sort=${this.route.snapshot.queryParams.sort}`);
    if (this.route.snapshot.queryParams.min_price) query.push(`min_price=${this.route.snapshot.queryParams.min_price}`);
    if (this.route.snapshot.queryParams.max_price) query.push(`max_price=${this.route.snapshot.queryParams.max_price}`);
    if (this.route.snapshot.queryParams.category) query.push(`category=${this.route.snapshot.queryParams.category}`);
    if (this.route.snapshot.queryParams.limit) query.push(`limit=${this.route.snapshot.queryParams.limit}`);
    if (this.route.snapshot.queryParams.term) query.push(`term=${this.route.snapshot.queryParams.term}`);
    return query.length === 1 ? query[0] : query.reduce((a, b) => `${a}&${b}`, '');
  }
   */

  ngOnDestroy() {
    this.subscriptions.map(subs => subs.unsubscribe());
  }

}

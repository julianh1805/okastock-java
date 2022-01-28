import { CategoriesService } from './../../../services/categories.service';

import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  categoriesArray;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private categoriesService: CategoriesService
  ) { }

  ngOnInit() {
    this.categoriesArray = this.categoriesService.get();
  }

  changeCategorie(category) {
    if (category) {
      this.router.navigate(['/produits'], { relativeTo: this.route, queryParams: { 'category': category } });
    } else {
      this.router.navigate(['/produits']);
    }
  }

  searchBy(term) {
    if (term) {
      this.router.navigate(['/produits'], { relativeTo: this.route, queryParams: { 'term': term } });
    } else {
      this.router.navigate(['/produits']);
    }
  }

}

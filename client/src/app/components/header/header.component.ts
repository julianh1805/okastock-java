import { CartService } from './../../services/cart.service';
import { NotificationService } from './../../services/notification.service';
import { User } from './../../models/user.model';
import { Subscription } from 'rxjs';
import { AuthService } from './../../services/auth.service';
import { CategoriesService } from './../../services/categories.service';
import { InscriptionModalComponent } from '../../globals/inscription-modal/inscription-modal.component';
import { ConexionModalComponent } from '../../globals/conexion-modal/conexion-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { environment } from 'src/environments/environment';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {

  private userSub: Subscription;
  private cart: Subscription;
  private notification: Subscription;
  categoriesArray;
  userConnected = null;
  notifications = [];
  cartProductsLength: number;
  selectValue = "";
  searchBarInput = '';

  constructor(
    private modalService: NgbModal,
    private router: Router,
    private route: ActivatedRoute,
    private categoriesService: CategoriesService,
    private authService: AuthService,
    private notificationService: NotificationService,
    private cartService: CartService
  ) {
    this.notification = this.notificationService.getNotification().subscribe(res => {
      this.notifications.push(res);
      setTimeout(() => this.notifications.splice(0, 1), 4000);
    });
    this.cart = this.cartService.productsLength.subscribe(cartProductsLength => {
      this.cartProductsLength = cartProductsLength
    })
  }

  ngOnInit() {
    this.userSub = this.authService.user.subscribe(user => {
      if (user) {
        this.userConnected = {
          email: user.email,
          id: user.id,
          name: user.name,
          logo: user.logo
        };
        this.cartService.getCartProductsLength(this.userConnected.id);
      } else {
        this.userConnected = null;
      }
    })
    this.categoriesArray = this.categoriesService.get();
  }

  conexion() {
    this.modalService.open(ConexionModalComponent);
  }

  inscription() {
    this.modalService.open(InscriptionModalComponent);
  }

  changeCategorie(category) {
    this.searchBarInput = '';
    if (category) {
      this.router.navigate(['/produits'], { relativeTo: this.route, queryParams: { 'category': category } });
    } else {
      this.router.navigate(['/produits']);
    }
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/produits']);
  }

  searchBy(term) {
    if (term) {
      this.selectValue = "";
      this.router.navigate(['/produits'], { relativeTo: this.route, queryParams: { 'term': term } });
    } else {
      this.router.navigate(['/produits']);
    }
  }

  goHome() {
    if (this.router.url === ('/produits')) {
      this.router.navigate(['/produits'])
        .then(() => {
          window.location.reload();
        });
    } else {
      this.router.navigate(['/produits'])
    }
  }

  ngOnDestroy() {
    this.userSub.unsubscribe();
    this.notification.unsubscribe();
    this.cart.unsubscribe();
  }

}

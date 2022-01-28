import { PasswordValidation } from './globals/password-validation';
import { CarouselComponent } from './globals/carousel/carousel.component';
import { AuthInterceptorService } from './services/auth-interceptor.service';
import { LoadingSpinnerComponent } from './globals/loading-spinner/loading-spinner.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NgbModule, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { AppRoutingModule } from './app-routing.module';
import { WishlistComponent } from './components/account/wishlist/wishlist.component';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { SearchComponent } from './components/header/search/search.component';
import { FooterComponent } from './components/footer/footer.component';
import { ProductsListComponent } from './components/products/products-list/products-list.component';
import { ProductDetailComponent } from './components/products/product-detail/product-detail.component';
import { ContactComponent } from './components/contact/contact.component';
import { ProductsComponent } from './components/account/products/products.component';
import { AddComponent } from './components/add/add.component';
import { ConexionModalComponent } from './globals/conexion-modal/conexion-modal.component';
import { InscriptionModalComponent } from './globals/inscription-modal/inscription-modal.component';
import { DeliveryComponent } from './components/purchase/delivery/delivery.component';
import { PaymentComponent } from './components/purchase/payment/payment.component';
import { ShoppingCartComponent } from './components/purchase/shopping-cart/shopping-cart.component';
import { CgvComponent } from './components/about/cgv/cgv.component';
import { LegalComponent } from './components/about/legal/legal.component';
import { CompanyComponent } from './components/about/company/company.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UserComponent } from './components/user/user.component';
import { BanniereComponent } from './globals/banniere/banniere.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { ValidationModalComponent } from './globals/validation-modal/validation-modal.component';
import { AccountComponent } from './components/account/account.component';
import { SuccessComponent } from './components/purchase/success/success.component';
import { PwStrengthComponent } from './globals/inscription-modal/pw-strength/pw-strength.component';
import { PasswordComponent } from './components/password/password.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SearchComponent,
    FooterComponent,
    ProductsListComponent,
    ProductDetailComponent,
    ContactComponent,
    ProductsComponent,
    WishlistComponent,
    AddComponent,
    ConexionModalComponent,
    InscriptionModalComponent,
    DeliveryComponent,
    PaymentComponent,
    ShoppingCartComponent,
    CgvComponent,
    LegalComponent,
    CompanyComponent,
    LoadingSpinnerComponent,
    UserComponent,
    BanniereComponent,
    NotFoundComponent,
    ValidationModalComponent,
    AccountComponent,
    CarouselComponent,
    SuccessComponent,
    PwStrengthComponent,
    PasswordComponent,
  ],
  imports: [
    BrowserAnimationsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    NgbModule,
    FormsModule,
    AngularFontAwesomeModule,
    HttpClientModule,
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production }),
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: AuthInterceptorService,
    multi: true
  }, HeaderComponent, NgbActiveModal, PasswordValidation
  ],
  bootstrap: [AppComponent],
  entryComponents: [ValidationModalComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule { }

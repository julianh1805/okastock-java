import { PasswordComponent } from './components/password/password.component';
import { SuccessComponent } from './components/purchase/success/success.component';
import { AccountComponent } from './components/account/account.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { UserComponent } from './components/user/user.component';
import { AuthGuard } from './globals/auth.guard';
import { CompanyComponent } from './components/about/company/company.component';
import { LegalComponent } from './components/about/legal/legal.component';
import { CgvComponent } from './components/about/cgv/cgv.component';
import { ShoppingCartComponent } from './components/purchase/shopping-cart/shopping-cart.component';
import { PaymentComponent } from './components/purchase/payment/payment.component';
import { DeliveryComponent } from './components/purchase/delivery/delivery.component';
import { InscriptionModalComponent } from './globals/inscription-modal/inscription-modal.component';
import { ConexionModalComponent } from './globals/conexion-modal/conexion-modal.component';
import { AddComponent } from './components/add/add.component';
import { WishlistComponent } from './components/account/wishlist/wishlist.component';
import { ContactComponent } from './components/contact/contact.component';
import { ProductDetailComponent } from './components/products/product-detail/product-detail.component';
import { ProductsListComponent } from './components/products/products-list/products-list.component';
import { SearchComponent } from './components/header/search/search.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ProductsComponent } from './components/account/products/products.component';


const routes: Routes = [
  { path: '', redirectTo: '/produits', pathMatch: 'full' },
  { path: 'categorie', component: ProductsListComponent },
  { path: 'recherche', component: SearchComponent },
  { path: 'produits', component: ProductsListComponent },
  { path: 'produits/:id', component: ProductDetailComponent },
  { path: 'mon-compte', redirectTo: '/mon-compte/produits', pathMatch: 'full' },
  {
    path: 'mon-compte', canActivate: [AuthGuard], component: AccountComponent, children: [
      { path: 'produits', canActivate: [AuthGuard], component: ProductsComponent },
      { path: 'favoris', canActivate: [AuthGuard], component: WishlistComponent },
    ]
  },
  { path: 'compte/:id', component: UserComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'mon-compte', canActivate: [AuthGuard], component: ProductsComponent },
  { path: 'mon-compte/favoris', canActivate: [AuthGuard], component: WishlistComponent },
  { path: 'deposer-une-annonce', canActivate: [AuthGuard], component: AddComponent },
  { path: 'editer-une-annonce/:productId', canActivate: [AuthGuard], component: AddComponent },
  { path: 'connexion', component: ConexionModalComponent },
  { path: 'inscription', component: InscriptionModalComponent },
  { path: 'mot-de-passe-oublie', component: PasswordComponent },
  { path: 'panier/recapitulatif', canActivate: [AuthGuard], component: ShoppingCartComponent },
  { path: 'panier/livraison', canActivate: [AuthGuard], component: DeliveryComponent },
  { path: 'panier/paiement', canActivate: [AuthGuard], component: PaymentComponent },
  { path: 'panier', canActivate: [AuthGuard], component: SuccessComponent },
  { path: 'a-propos/condition-generales-de-vente', component: CgvComponent },
  { path: 'a-propos/mentions-legales', component: LegalComponent },
  { path: 'a-propos/qui-sommes-nous', component: CompanyComponent },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
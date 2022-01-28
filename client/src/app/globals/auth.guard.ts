import { HeaderComponent } from './../components/header/header.component';
import { ConexionModalComponent } from './conexion-modal/conexion-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from './../services/auth.service';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })

export class AuthGuard implements CanActivate {

    constructor(
        private authService: AuthService,
        private router: Router,
        private modalService: NgbModal,
        private headerComponent: HeaderComponent) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot, nextState?: RouterStateSnapshot): boolean | UrlTree | Promise<boolean | UrlTree> | Observable<boolean | UrlTree> {
        const href = '/' + route.url[0].path;
        return this.authService.user.pipe(take(1), map(user => {
            const isAuth = !!user;
            if (isAuth) {
                return true;
            }
            const modalRef = this.modalService.open(ConexionModalComponent);
            modalRef.result.then((boolean) => {
                if (boolean) {
                    return this.router.navigate([href]);
                }
            });
        }))
    }
}
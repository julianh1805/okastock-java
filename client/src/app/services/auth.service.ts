import { User } from './../models/user.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { environment } from '../../environments/environment';
import { Subject, BehaviorSubject } from 'rxjs';
import { tap, ignoreElements } from 'rxjs/operators';

const path = '/users'

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  headers = new HttpHeaders({
    'Authorization': 'my-auth-token',
    'Content-Type': 'application/json',
    'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, Accept, Authorization'
  })

  user = new BehaviorSubject<User>(null);
  private tokenExpirationTimer: any;

  constructor(private http: HttpClient) { }

  signup(user) {
    return this.http.post<any>(environment.API + path + '/signup', user).pipe(tap(user => {
      this.handleAuthentication(user.email, user.id, user.name, user.logo, user.token, user.tokenExpirationDate);
    }))
  }

  signin(user) {
    return this.http.post<any>(environment.API + path + '/signin', user).pipe(tap(user => {
      this.handleAuthentication(user.email, user.id, user.name, user.logo, user.token, user.tokenExpirationDate);
    }));
  }

  private handleAuthentication(email: string, id: string, name: string, logo: string, token: string, expiresIn) {
    const expirationDate = new Date(new Date().getTime() + (expiresIn * 1000))
    const user = new User(email, id, name, logo, token, expirationDate);
    this.user.next(user);
    this.autoLogout(expiresIn * 1000)
    localStorage.setItem('user', JSON.stringify(user));
  }

  autoLogin(){
    const user = JSON.parse(localStorage.getItem('user'));
    if(!user){
      return;
    }
    const loadedUser = new User(user.email, user.id, user.name, user.logo, user._token, new Date(user._tokenExpirationDate));
    if(loadedUser.token){
      this.user.next(loadedUser);
      const expirationDuration = new Date(user._tokenExpirationDate).getTime() - new Date().getTime();
      this.autoLogout(expirationDuration);
    }
  }

  autoLogout(expirationDuration : number){
      this.tokenExpirationTimer = setTimeout(() => {
      this.logout()
    }, expirationDuration)
  }

  logout(){
    this.user.next(null);
    localStorage.removeItem('user');
    if(this.tokenExpirationTimer){
      clearTimeout(this.tokenExpirationTimer);
    }
    this.tokenExpirationTimer = null;
  }
}

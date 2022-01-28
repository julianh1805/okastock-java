import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

const path = '/users';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(private http: HttpClient) { }

  getUser(id) {
    return this.http.get<any>(environment.API + path + `/${id}`);
  }

  deleteUser(id) {
    return this.http.delete<any>(environment.API + path + `/${id}`);
  }

  updateUser(id, user) {
    return this.http.post<any>(environment.API + path + `/update/${id}`, user);
  }
}

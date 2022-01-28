import { Subject } from 'rxjs';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notification = new Subject<any>();

  constructor() { }

  sendNotification(notification) {
    this.notification.next(notification);
  }

  getNotification() {
    return this.notification.asObservable();
  }


}

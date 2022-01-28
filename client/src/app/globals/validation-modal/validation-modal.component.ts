import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-validation-modal',
  templateUrl: './validation-modal.component.html',
  styleUrls: ['./validation-modal.component.scss']
})
export class ValidationModalComponent implements OnInit {
  userName;
  productTitle;
  productCartTitle;
  constructor(public activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

  delete(boolean) {
    this.activeModal.close(boolean);
  }

}

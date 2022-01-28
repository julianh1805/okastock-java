import { NotificationService } from './../../services/notification.service';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { InscriptionModalComponent } from '../inscription-modal/inscription-modal.component';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-conexion-modal',
  templateUrl: './conexion-modal.component.html',
  styleUrls: ['./conexion-modal.component.scss']
})
export class ConexionModalComponent implements OnInit {

  submitted = false;
  errorResponse = {
    display: false,
    message: ''
  }
  isLoading = false;


  conexionForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required),
  });

  constructor(
    public activeModal: NgbActiveModal,
    private modalService: NgbModal,
    private authService: AuthService,
    private router: Router,
    private notificationService: NotificationService,
  ) { }

  ngOnInit() {
  }

  close() {
    this.activeModal.close()
  }

  switchToInscription() {
    this.activeModal.close()
    this.modalService.open(InscriptionModalComponent);
  }

  contact() {
    this.activeModal.close();
    this.router.navigate(['/contact'])
  }

  onSubmit(form) {
    if (form) {
      if (form.invalid) {
        this.submitted = true;
      } else {
        this.isLoading = true;
        var user = {
          email: form.value.email,
          password: form.value.password
        }
        this.authService.signin(user).subscribe(res => {
          this.notificationService.sendNotification({ message: 'Vous êtes connecté en tant que ' + res.name + '.', type: 'success' })
          this.activeModal.close(true);
          this.isLoading = false;
        }, error => {
          this.errorResponse.display = true;
          this.errorResponse.message = error.error.message;
          this.isLoading = false;
        })
      }
    }
  }


}

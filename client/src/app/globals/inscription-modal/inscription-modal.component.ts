import { NotificationService } from './../../services/notification.service';
import { AuthService } from '../../services/auth.service';
import { PasswordValidation } from '../password-validation';
import { ConexionModalComponent } from '../conexion-modal/conexion-modal.component';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-inscription-modal',
  templateUrl: './inscription-modal.component.html',
  styleUrls: ['./inscription-modal.component.scss']
})
export class InscriptionModalComponent implements OnInit {

  submitted = false;
  selectedFile: File = null;
  emailError;
  siretError;
  logoError;
  telephoneError;
  imageToPush = [];
  isLoading = false;

  userImage = '';

  inscriptionForm = new FormGroup({
    name: new FormControl('', Validators.required),
    siret: new FormControl('', [Validators.required, Validators.minLength(14), Validators.maxLength(14), Validators.pattern("^[1-9][0-9]*$")]),
    city: new FormControl('', [Validators.required, Validators.pattern('^[a-zA-Z \-\']+')]),
    postalCode: new FormControl('', [Validators.required, Validators.minLength(5), Validators.maxLength(5), Validators.pattern("^(([0-9][1-9])|(9[0-5]))[0-9]{3}$")]),
    telephone: new FormControl('', [Validators.required, Validators.minLength(10), Validators.maxLength(10), Validators.pattern("^((06)|(07)|(02))[0-9]{8}$")]),
    website: new FormControl('', [Validators.required, Validators.pattern('^(http[s]?:\\/\\/){0,1}(www\\.){0,1}[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,5}[\\.]{0,1}$')]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    confirmPassword: new FormControl('', Validators.required),
    logo: new FormControl('', [Validators.required]),
    rgpd: new FormControl(false, Validators.requiredTrue),
  }, PasswordValidation.MatchPassword)

  constructor(
    public activeModal: NgbActiveModal,
    private authService: AuthService,
    private modalService: NgbModal,
    private notificationService: NotificationService
  ) {
    this.inscriptionForm.controls['siret'].valueChanges.subscribe(value => {
      this.siretError = '';
    });
    this.inscriptionForm.controls['email'].valueChanges.subscribe(value => {
      this.emailError = '';
    });
  }


  ngOnInit() {
  }

  close() {
    this.activeModal.close()
  }

  switchToConnexion() {
    this.activeModal.close()
    this.modalService.open(ConexionModalComponent);
  }

  checkPasswords(group: FormGroup) {
    let pass = group.get('password').value;
    let confirmPass = group.get('confirmPassword').value;

    return pass === confirmPass ? null : { notSame: true }
  }

  onSelectedFile(event) {
    this.logoError = '';
    this.imageToPush = [];
    if (event.target.files[0]) {
      this.imageToPush.push(<File>event.target.files[0])
      if (event.target.files[0].size >= 2097152) {
        this.logoError = "Le fichier " + event.target.files[0].name + " est trop lourd. Maximum 2MB par fichier."
      }
      else {
        var reader = new FileReader();

        reader.onload = (event: any) => {
          this.userImage = event.target.result;
        }
        this.inscriptionForm.patchValue({
          logo: this.imageToPush
        });
        reader.readAsDataURL(event.target.files[0]);
      }
    }
  }


  onSubmit(form) {
    if (form) {
      if (form.invalid) {
        this.isLoading = true;
        this.submitted = true;
        const user = new FormData();
        user.append('siret', form.value.siret);
        user.append('email', form.value.email.toLowerCase());
        this.sendUserRequest(user);
      } else {
        this.isLoading = true;
        if (form.value.website.startsWith('https://')) {
          form.value.website = form.value.website.slice(8)
        } else if (form.value.website.startsWith('http://')) {
          form.value.website = form.value.website.slice(7)
        }
        const user = new FormData();
        user.append('name', form.value.name.toLowerCase());
        user.append('siret', form.value.siret);
        user.append('city', form.value.city.toLowerCase());
        user.append('postalCode', form.value.postalCode);
        user.append('telephone', form.value.telephone);
        user.append('website', form.value.website.toLowerCase());
        user.append('email', form.value.email.toLowerCase());
        user.append('logo', this.imageToPush[0]);
        user.append('password', form.value.password);
        this.sendUserRequest(user);
      }
    }
  }

  sendUserRequest(user) {
    this.authService.signup(user).subscribe(res => {
      this.isLoading = false;
      this.notificationService.sendNotification({ message: 'Vous êtes connecté en tant que ' + res.name + '.', type: 'success' })
      this.activeModal.close();
    }, error => {
      console.log('error')
      this.siretError = '';
      this.emailError = '';
      for (let i = 0; i < error.error.length; i++) {
        if (error.error[i].type === 'email') {
          this.emailError = error.error[i].message
        }
        else if (error.error[i].type === 'siret') {
          this.siretError = error.error[i].message
        }
        else {
          this.logoError = error.error[i].message
        }
      }
      this.isLoading = false;
    }
    )
  }

}

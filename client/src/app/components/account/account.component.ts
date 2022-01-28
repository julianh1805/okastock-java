import { NotificationService } from './../../services/notification.service';
import { Subscription } from 'rxjs';
import { User } from './../../models/user.model';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { ValidationModalComponent } from './../../globals/validation-modal/validation-modal.component';
import { UsersService } from './../../services/users.service';
import { ProductsService } from './../../services/products.service';
import { AuthService } from './../../services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit, OnDestroy {
  isLoading = false;
  editIsLoading = false;
  user: any;
  id = null;
  editing = false;
  submitted = false;
  imageToPush = [];
  newUserImage = '';
  userPath;
  newLogo = null;

  private productsLengthSub: Subscription;
  productsLength: number;

  editForm = new FormGroup({
    name: new FormControl(null, Validators.required),
    city: new FormControl(null, [Validators.required, Validators.pattern('^[a-zA-Z \-\']+')]),
    postalCode: new FormControl(null, [Validators.required, Validators.minLength(5), Validators.maxLength(5), Validators.pattern("^(([0-9][1-9])|(9[0-5]))[0-9]{3}$")]),
    telephone: new FormControl(null, [Validators.required, Validators.minLength(10), Validators.maxLength(10), Validators.pattern("^((06)|(07)|(02))[0-9]{8}$")]),
    website: new FormControl(null, [Validators.required, Validators.pattern('^(http[s]?:\\/\\/){0,1}(www\\.){0,1}[a-zA-Z0-9\\.\\-]+\\.[a-zA-Z]{2,5}[\\.]{0,1}$')]),
    logo: new FormControl(null, [Validators.required]),
    rgpd: new FormControl(true, Validators.requiredTrue),
  })

  constructor(
    private router: Router,
    private usersService: UsersService,
    private modalService: NgbModal,
    private authService: AuthService,
    private productsService: ProductsService,
    private notificationService: NotificationService
  ) {
    this.productsLengthSub = this.productsService.productsLength.subscribe(number => {
      this.productsLength = number;
    })
  }

  ngOnInit() {
    this.isLoading = true;
    this.id = JSON.parse(localStorage.getItem('user')).id;
    this.usersService.getUser(this.id).subscribe(user => {
      if (user.postalCode.toString().length === 4) {
        user.postalCode = '0' + user.postalCode;
      }
      user.telephone = '0' + user.telephone;
      this.user = user;
      this.userPath = user.logo;
      this.editForm.patchValue({
        name: user.name,
        city: user.city,
        postalCode: user.postalCode,
        telephone: user.telephone,
        website: user.website,
        logo: user.logo
      })
      this.isLoading = false;
    })
  }

  deleteUser() {
    const modalRef = this.modalService.open(ValidationModalComponent);
    modalRef.componentInstance.userName = this.user.name;
    modalRef.result.then(boolean => {
      if (boolean) {
        this.isLoading = true;
        this.usersService.deleteUser(this.id).subscribe(() => {
          this.authService.logout();
          this.isLoading = false;
          this.router.navigate(['/produits']);

        })
      }
    })
  }

  edit() {
    this.editForm.patchValue({
      name: this.user.name,
      city: this.user.city,
      postalCode: this.user.postalCode,
      telephone: this.user.telephone,
      website: this.user.website,
      rgpd: true
    })
    this.editing = !this.editing;
  }

  onSubmit(form) {
    this.editIsLoading = true;
    if (form) {
      if (!form.valid) {
        this.submitted = true;
        this.editIsLoading = false;
      }
      else {
        if (form.value.website.startsWith('https://')) {
          form.value.website = form.value.website.slice(8)
        } else if (form.value.website.startsWith('http://')) {
          form.value.website = form.value.website.slice(7)
        }
        const user = new FormData();
        user.append('name', form.value.name);
        user.append('city', form.value.city);
        user.append('postalCode', form.value.postalCode);
        user.append('telephone', form.value.telephone);
        user.append('website', form.value.website);
        user.append('userPath', this.userPath);
        user.append('logo', this.imageToPush[0]);
        this.usersService.updateUser(this.id, user).subscribe(user => {
          let _token = JSON.parse(localStorage.getItem('user'))._token;
          let _tokenExpirationDate = JSON.parse(localStorage.getItem('user'))._tokenExpirationDate;
          let userData = new User(user.email, user._id, user.name, user.logo, _token, _tokenExpirationDate);
          this.authService.user.next(userData);
          localStorage.setItem('user', JSON.stringify(userData));
          this.user = user;
          if (user.postalCode.toString().length === 4) {
            user.postalCode = '0' + user.postalCode;
          }
          user.telephone = '0' + user.telephone;
          this.newLogo = null;
          this.editForm.reset();
          this.editing = false;
          this.submitted = false;
          this.editIsLoading = false;
          this.notificationService.sendNotification({ message: "Votre profil a bien été mis à jour.", type: "success" });
        }), error => {
          console.log(error);
        }
      }
    }
  }

  onSelectedFile(event) {
    this.imageToPush = [];
    if (event.target.files[0]) {
      if (event.target.files[0].size >= 2097152) {
        this.notificationService.sendNotification({ message: "Le fichier " + event.target.files[0].name + " est trop lourd. Maximum 2MB par fichier.", type: "danger" });
      }
      else {
        this.imageToPush.push(<File>event.target.files[0])
        var reader = new FileReader();

        reader.onload = (event: any) => {
          this.newLogo = event.target.result;
        }
        this.editForm.patchValue({
          logo: this.imageToPush
        });
        reader.readAsDataURL(event.target.files[0]);
      }
    }
  }

  ngOnDestroy() {
    this.productsLengthSub.unsubscribe();
  }

}

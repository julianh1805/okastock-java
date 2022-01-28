import { HttpClient, HttpHandler } from '@angular/common/http';
import { NgbActiveModal, NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { PwStrengthComponent } from './pw-strength/pw-strength.component';
import { PasswordValidation } from './../password-validation';
import { ReactiveFormsModule, FormGroup, FormBuilder } from '@angular/forms';
import { LoadingSpinnerComponent } from './../loading-spinner/loading-spinner.component';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionModalComponent } from './inscription-modal.component';

describe('InscriptionModalComponent', () => {
  let component: InscriptionModalComponent;
  let fixture: ComponentFixture<InscriptionModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [InscriptionModalComponent, LoadingSpinnerComponent, PwStrengthComponent],
      imports: [ReactiveFormsModule, NgbModule],
      providers: [PasswordValidation, NgbActiveModal, HttpClient, HttpHandler]
    })
    // .compileComponents().then(() => {
    //   const fixture = TestBed.createComponent(InscriptionModalComponent);
    //   component = fixture.componentInstance;
    //   component.ngOnInit();
    //   fixture.detectChanges();
    // });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InscriptionModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Should create the component', () => {
    const fixture = TestBed.createComponent(InscriptionModalComponent);
    const component = fixture.debugElement.componentInstance;
    expect(component).toBeTruthy();
  });

  it('Form value should be empty at starting', () => {
    expect(component.inscriptionForm.valid).toBeFalsy();
  });

  it('Valid form should be created and valid', () => {
    component.inscriptionForm.setValue({
      name: 'Okastock',
      siret: 12345678910111,
      city: 'Nantes',
      postalCode: 44200,
      telephone: '0685487966',
      website: 'okastock.com',
      email: 'husson.j@yahoo.comaaa',
      password: 'admin_1805',
      confirmPassword: 'admin_1805',
      logo: true,
      rgpd: true
    });
    fixture.detectChanges();
    expect(component.inscriptionForm.valid).toBeTruthy();
  });

  it('Invalid form should not be created and invalid', () => {
    component.inscriptionForm.setValue({
      name: '',
      siret: 12345678910111,
      city: 'Nantes',
      postalCode: 44200,
      telephone: '0685487966',
      website: 'okastock.com',
      email: 'husson.j@yahoo.comaaa',
      password: 'admin_1805',
      confirmPassword: 'admin_1805',
      logo: true,
      rgpd: true
    });
    fixture.detectChanges();
    expect(component.inscriptionForm.valid).toBeFalsy();
  });

  it('Name field validity', () => {
    let name = component.inscriptionForm.controls['name'];
    expect(name.valid).toBeFalsy();

    name.setValue("");
    expect(name.hasError('required')).toBeTruthy();
  });

  it('Siret field validity', () => {
    let siret = component.inscriptionForm.controls['siret'];
    expect(siret.valid).toBeFalsy();

    siret.setValue("XXX");
    expect(siret.hasError('pattern')).toBeTruthy();

    siret.setValue("");
    expect(siret.hasError('required')).toBeTruthy();

    siret.setValue("12345");
    expect(siret.hasError('minlength')).toBeTruthy();

    siret.setValue("123456789101112");
    expect(siret.hasError('maxlength')).toBeTruthy();
  });

  it('City field validity', () => {
    let city = component.inscriptionForm.controls['city'];
    expect(city.valid).toBeFalsy();

    city.setValue("453");
    expect(city.hasError('pattern')).toBeTruthy();

    city.setValue("");
    expect(city.hasError('required')).toBeTruthy();
  });

  it('Postal code field validity', () => {
    let postalCode = component.inscriptionForm.controls['postalCode'];
    expect(postalCode.valid).toBeFalsy();

    postalCode.setValue("XXX");
    expect(postalCode.hasError('pattern')).toBeTruthy();

    postalCode.setValue("");
    expect(postalCode.hasError('required')).toBeTruthy();

    postalCode.setValue("1234");
    expect(postalCode.hasError('minlength')).toBeTruthy();

    postalCode.setValue("123456");
    expect(postalCode.hasError('maxlength')).toBeTruthy();
  });

  it('Website field validity', () => {
    let website = component.inscriptionForm.controls['website'];
    expect(website.valid).toBeFalsy();

    website.setValue("453");
    expect(website.hasError('pattern')).toBeTruthy();

    website.setValue("okastock.commmm");
    expect(website.hasError('pattern')).toBeTruthy();

    website.setValue("");
    expect(website.hasError('required')).toBeTruthy();
  });

  it('Email field validity', () => {
    let email = component.inscriptionForm.controls['email'];
    expect(email.valid).toBeFalsy();

    email.setValue("okastock@gmail.");
    expect(email.hasError('email')).toBeTruthy();

    email.setValue("");
    expect(email.hasError('required')).toBeTruthy();
  });

  it('Password field validity', () => {
    let password = component.inscriptionForm.controls['password'];
    let confirmPassword = component.inscriptionForm.controls['confirmPassword'];
    expect(password.valid).toBeFalsy();

    password.setValue("admin");
    expect(password.hasError('minlength')).toBeTruthy();

    password.setValue("");
    expect(password.hasError('required')).toBeTruthy();

    password.setValue("azer");
    confirmPassword.setValue("azerrr");
    expect(password.valid).toBeFalsy();
  });
});

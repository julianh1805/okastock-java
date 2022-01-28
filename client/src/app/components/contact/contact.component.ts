import { FormGroup, FormControl, Validators } from '@angular/forms';
import { environment } from './../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.scss']
})
export class ContactComponent implements OnInit {
  contactForm = new FormGroup({
    name: new FormControl('', Validators.required),
    entreprise: new FormControl(''),
    email: new FormControl('', [Validators.required, Validators.email]),
    subject: new FormControl('', Validators.required),
    message: new FormControl('', Validators.required),
  })

  submitted = false;
  id;
  responseText;
  isLoading = false;


  constructor(private http: HttpClient) { }

  ngOnInit() {
    if (JSON.parse(localStorage.getItem('user'))) {
      this.contactForm.patchValue({
        entreprise: JSON.parse(localStorage.getItem('user')).name,
        email: JSON.parse(localStorage.getItem('user')).email,
      })
    }
  }

  onSubmit(form) {
    if (form) {
      if (!form.valid) {
        this.submitted = true;
      } else {
        let mailBody = {
          name: form.value.name,
          entreprise: form.value.entreprise,
          email: form.value.email,
          subject: form.value.subject,
          message: form.value.message
        }
        this.isLoading = true;
        this.http.post(environment.API + '/mail', mailBody).subscribe(res => {
          this.responseText = res;
          this.isLoading = false;
        }, error => {
          console.log(error);
        })
      }
    }
  }

}

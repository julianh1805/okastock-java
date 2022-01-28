import { environment } from 'src/environments/environment';
import { NotificationService } from './../../services/notification.service';
import { ProductsService } from './../../services/products.service';
import { CategoriesService } from './../../services/categories.service';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.scss']
})
export class AddComponent implements OnInit {

  categoriesArray;
  submitted = false;
  images = []
  imagesToPush: File[] = []
  editing = false;
  productId;
  createdAt;
  isLoading = false;

  productForm = new FormGroup({
    title: new FormControl(null, [Validators.required, Validators.maxLength(120)]),
    description: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    category: new FormControl(null, Validators.required),
    price: new FormControl(null, [Validators.required, Validators.pattern("^[0-9]+(\.[0-9]{1,2})?$")]),
    quantity: new FormControl(null, [Validators.required, Validators.pattern("^[0-9]*$")]),
    images: new FormControl('', [Validators.required, Validators.maxLength(5)])
  })

  constructor(
    private router: Router,
    private categoriesService: CategoriesService,
    private productsService: ProductsService,
    private notificationService: NotificationService
  ) {
  }

  ngOnInit() {
    if (this.router.url.slice(1, 5) === 'edit') {
      this.isLoading = true;
      let length = this.router.url.length;
      this.editing = true;
      this.productId = this.router.url.slice(20, length);
      this.productsService.getProduct(this.productId).subscribe(product => {
        for (let i = 0; i < product.images.length; i++) {
          this.images.push(product.images[i]);
        }
        product.price = product.price.toFixed(2);
        product.price = product.price.replace('.', ',');
        this.productForm.patchValue({
          title: product.title,
          description: product.description,
          category: product.category,
          price: product.price,
          quantity: product.quantity,
          images: this.images
        });
        this.createdAt = product.createdAt
        this.isLoading = false;
      }, error => {
        console.log(error)
      })
    }
    this.categoriesArray = this.categoriesService.get();
  }

  onFileChange(event) {
    if (event.target.files && event.target.files[0]) {
      for (let i = 0; i < event.target.files.length; i++) {
        if (event.target.files[i].size >= 2097152) {
          this.notificationService.sendNotification({ message: "Le fichier " + event.target.files[i].name + " est trop lourd. Maximum 2MB par fichier.", type: "danger" });
        }
        else {
          this.imagesToPush.push(<File>event.target.files[i])
          var reader = new FileReader();

          reader.onload = (event: any) => {
            this.images.push(event.target.result);
          }
          this.productForm.patchValue({
            images: this.imagesToPush
          });
          reader.readAsDataURL(event.target.files[i]);
        }
      }
    }
  }

  priceChange() {
    if (this.productForm.value.price) {
      this.productForm.value.price = this.productForm.value.price.toString();
      if (this.productForm.value.price.includes('.')) {
        let decimals = this.productForm.value.price.split('.');
        if (decimals[1].length === 0) {
          this.productForm.value.price = this.productForm.value.price + '00';
        } else if (decimals[1].length === 1) {
          this.productForm.value.price = this.productForm.value.price + '0';
        }
      }
      this.productForm.patchValue({
        price: this.productForm.value.price
      })
    }

  }

  onSubmit(form) {
    if (form) {
      if (!form.valid) {
        this.submitted = true;
      } else {
        this.isLoading = true;
        let id = JSON.parse(localStorage.getItem('user')).id;
        if (form.value.price.toString().includes(',')) {
          form.value.price = +form.value.price.replace(',', '.');
        }
        const product = new FormData();
        product.append('title', form.value.title);
        product.append('description', form.value.description);
        product.append('price', form.value.price);
        product.append('quantity', form.value.quantity);
        product.append('category', form.value.category);
        product.append('user', id);
        if (this.editing) {
          this.images = this.images.filter(imagePath => {
            return imagePath.indexOf("data:") !== 0;
          });
          for (var image of this.images) {
            product.append('imagesPath', image)
          }
          for (let i = 0; i < this.imagesToPush.length; i++) {
            product.append('images', this.imagesToPush[i]);
          }
          product.append('createdAt', this.createdAt);
          this.productsService.updateProduct(this.productId, product).subscribe(res => {
            this.notificationService.sendNotification({ message: res.message, type: res.type });
            this.isLoading = false;
            this.router.navigate(['/mon-compte/produits']);
          }, error => {
            console.log(error);
          })
        }
        else {
          for (let i = 0; i < this.imagesToPush.length; i++) {
            product.append('images', this.imagesToPush[i]);
          }
          this.productsService.addProduct(product).subscribe(res => {
            this.notificationService.sendNotification({ message: res.message, type: res.type });
            this.isLoading = false;
            this.router.navigate(['/mon-compte/produits'])
          }, error => {
            console.log(error);
          })
        }
      }

    }
    this.submitted = true
  }

  deleteImage(index) {
    this.images.splice(index, 1);
    this.imagesToPush.splice(index, 1);
    this.productForm.patchValue({
      images: this.images
    });
  }
}

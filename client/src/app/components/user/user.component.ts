import { UsersService } from './../../services/users.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
  user: any;
  isLoading = false;
  environmentAPI;

  constructor(private route: ActivatedRoute, private usersService: UsersService) { }

  ngOnInit() {
    this.isLoading = true;
    let id = this.route.snapshot.paramMap.get("id");
    this.usersService.getUser(id).subscribe(user => {
      this.user = user;
      this.isLoading = false;
    })
  }

}

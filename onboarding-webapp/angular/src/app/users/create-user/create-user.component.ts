import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { Validators, FormArray, ValidatorFn, AbstractControl } from '@angular/forms';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html',
  styleUrls: ['./create-user.component.scss']
})
export class CreateUserComponent implements OnInit {

  newUser: User;
  errors: any;

  newUserForm = this.fb.group({
    username: [''],
    firstName: [''],
    lastName: [''],
    phoneList: this.fb.array([this.fb.group({
      phoneNumber: [''],
      primary:['true']
  })])
  });



  constructor(private route: ActivatedRoute, private router: Router, private userService: UserService, private fb : FormBuilder) {
  }

  onSubmit() {
    this.newUser = this.newUserForm.getRawValue() as User;
    console.log(this.newUser);
    this.userService.save(this.newUser).subscribe(data => {
      console.log(data);
      this.userCreated();
    },
    err => {
      this.errors=err.error;
      console.log(this.errors);
    });
  }

  ngOnInit() {
  }

  userCreated() {
    alert("User successfully created!");
    this.router.navigate(['/']);
  }

  get phoneList() {
    return this.newUserForm.get('phoneList') as FormArray;
  }

  get firstName() {
    return this.newUserForm.get('firstName');
  }

  get lastName() {
    return this.newUserForm.get('lastName');
  }

  get username() {
    return this.newUserForm.get('username');
  }

  addPhone() {
    this.phoneList.push(
      this.fb.group({
        phoneNumber: [''],
        primary:['false']
    }))
  }

  removePhone() {
    if (this.phoneList.length > 1) {
      this.phoneList.removeAt(this.phoneList.length - 1);
    }
  }

}

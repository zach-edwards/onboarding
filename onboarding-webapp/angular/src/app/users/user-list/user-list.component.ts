import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user';
import { Router } from '@angular/router';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { DeleteUserComponent } from '../delete-user/delete-user.component';
import { EditUserComponent } from '../edit-user/edit-user.component';
import { FormControl, FormBuilder } from '@angular/forms';
import { debounceTime, distinctUntilChanged, debounce } from 'rxjs/operators';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.scss']
})
export class UserListComponent implements OnInit {

  constructor(private dialog: MatDialog, private userService: UserService, private router: Router, private fb: FormBuilder) { }

  users: User[];
  searchInput:string;

  ngOnInit() {
    this.getAllUsers();
  }

  refreshUserList() {
    this.onSearchChange(this.searchInput);
  }

  getAllUsers(){
    this.userService.findAll().subscribe(data => {
      this.users = data;
    });
  }

  onSearchChange(searchValue: string) {
    if (!searchValue) {
      this.getAllUsers();
    } else {
      this.userService.findAllBySearch(searchValue).subscribe(data => {
        this.users = data;
      });
    }
  }

  navigateToUser(user: User) {
    this.router.navigateByUrl('/users/' + user.userId);
  };

  confirmDelete(user: User) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = { user: user }

    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;

    let dialogRef = this.dialog.open(DeleteUserComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => {console.log(data),
      this.refreshUserList()}
    );
  }

  editUser(user: User) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = { user: user }

    dialogConfig.disableClose = false;
    dialogConfig.autoFocus = false;

    let dialogRef = this.dialog.open(EditUserComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => {
        if (data && data.userEdited) {
          this.refreshUserList()
        }
      });
  }
}
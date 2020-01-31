import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MustMatch } from 'src/app/shared/helpers/must-match-validator';
import { UsersService } from 'src/app/shared/services';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-user-details-form',
  templateUrl: './user-details-form.component.html',
  styleUrls: ['./user-details-form.component.scss']
})
export class UserDetailsFormComponent implements OnInit {
  addNewUserForm: FormGroup;
  submitted = false;

  constructor(private formBuilder: FormBuilder, 
    private usersService: UsersService,
    public activeModal: NgbActiveModal) { }

  ngOnInit() {
    this.addNewUserForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      middleInitial: [''],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      loginId: ['', Validators.required],
      loginPwd: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required]
    }, {
        validator: MustMatch('loginPwd', 'confirmPassword')
      });
  }

  get formValue() {
    return this.addNewUserForm.controls;
  }

  addUser() {
    this.submitted = true;
    if (this.addNewUserForm.invalid) {
      console.log("Invalid form!!");
      return;
    }
    console.log("New user Json : " + JSON.stringify(this.addNewUserForm.value));
    console.log("Get Raw value : " + this.addNewUserForm.getRawValue());
    let newUserFormData = JSON.stringify(this.addNewUserForm.getRawValue());
    this.usersService.addNewUser(newUserFormData);
    this.activeModal.close();
  }

}

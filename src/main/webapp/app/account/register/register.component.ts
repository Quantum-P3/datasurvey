import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/config/error.constants';
import { RegisterService } from './register.service';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements AfterViewInit {
  // @ViewChild('name', { static: false })
  // name?: ElementRef;

  profileIcon: number = 1;
  profileIcons: any[] = [
    { name: 'C1', class: 'active' },
    { name: 'C2' },
    { name: 'C3' },
    { name: 'C4' },
    { name: 'C5' },
    { name: 'C6' },
    { name: 'C7' },
    { name: 'C8' },
    { name: 'C9' },
    { name: 'C10' },
    { name: 'C11' },
    { name: 'C12' },
    { name: 'C13' },
    { name: 'C14' },
    { name: 'C15' },
    { name: 'C16' },
    { name: 'C17' },
    { name: 'C18' },
    { name: 'C19' },
    { name: 'C20' },
    { name: 'C21' },
    { name: 'C22' },
    { name: 'C23' },
    { name: 'C24' },
    { name: 'C25' },
    { name: 'C26' },
    { name: 'C27' },
    { name: 'C28' },
  ];

  doNotMatch = false;
  error = false;
  errorEmailExists = false;
  errorUserExists = false;
  success = false;

  // Login will be used to store the email as well.
  // login: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]]
  registerForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(254)]],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
  });

  constructor(private translateService: TranslateService, private registerService: RegisterService, private fb: FormBuilder) {}

  ngAfterViewInit(): void {
    //   if (this.name) {
    //     this.name.nativeElement.focus();
    //   }
  }

  register(): void {
    this.doNotMatch = false;
    this.error = false;
    this.errorEmailExists = false;
    this.errorUserExists = false;

    const password = this.registerForm.get(['password'])!.value;
    if (password !== this.registerForm.get(['confirmPassword'])!.value) {
      this.doNotMatch = true;
    } else {
      const login = this.registerForm.get(['email'])!.value;
      const email = this.registerForm.get(['email'])!.value;
      const name = this.registerForm.get(['name'])!.value;
      console.log(name);

      this.registerService
        .save({ login, email, password, langKey: this.translateService.currentLang, name, profileIcon: this.profileIcon })
        .subscribe(
          () => (this.success = true),
          response => this.processError(response)
        );
    }
  }

  processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = true;
    } else {
      this.error = true;
    }
  }

  selectIcon(event: MouseEvent): void {
    if (event.target instanceof Element) {
      document.querySelectorAll('.active').forEach(e => e.classList.remove('active'));
      event.target.classList.add('active');
      this.profileIcon = +event.target.getAttribute('id')! + 1;
    }
  }
}

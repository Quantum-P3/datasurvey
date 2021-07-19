import { Component, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { PasswordResetInitService } from './password-reset-init.service';
import { HttpErrorResponse } from '@angular/common/http';
import { EMAIL_NOT_EXISTS_TYPE } from '../../../config/error.constants';

@Component({
  selector: 'jhi-password-reset-init',
  templateUrl: './password-reset-init.component.html',
})
export class PasswordResetInitComponent implements AfterViewInit {
  @ViewChild('email', { static: false })
  email?: ElementRef;
  errorEmailNotExists = false;
  error = false;
  success = false;
  resetRequestForm = this.fb.group({
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
  });

  constructor(private passwordResetInitService: PasswordResetInitService, private fb: FormBuilder) {}

  ngAfterViewInit(): void {
    if (this.email) {
      this.email.nativeElement.focus();
    }
  }

  requestReset(): void {
    this.errorEmailNotExists = false;
    this.passwordResetInitService.save(this.resetRequestForm.get(['email'])!.value).subscribe(
      () => (this.success = true),
      response => this.processError(response)
    );
  }

  previousState(): void {
    window.history.back();
  }

  processError(response: HttpErrorResponse): void {
    if (response.status === 400 && response.error.type === EMAIL_NOT_EXISTS_TYPE) {
      this.errorEmailNotExists = true;
    } else {
      this.error = true;
    }
  }
}

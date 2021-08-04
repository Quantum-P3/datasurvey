import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IEncuesta } from '../encuesta.model';

@Component({
  selector: 'jhi-encuesta-password-dialog',
  templateUrl: './encuesta-password-dialog.component.html',
  styleUrls: ['./encuesta-password-dialog.component.scss'],
})
export class EncuestaPasswordDialogComponent implements OnInit {
  passwordForm = this.fb.group({
    password: [null, [Validators.required]],
  });
  encuesta?: IEncuesta;
  isWrong?: boolean;
  passwordInput?: string;

  constructor(protected activeModal: NgbActiveModal, protected fb: FormBuilder) {}

  ngOnInit(): void {}

  submitPassword() {
    const password = this.passwordForm.get(['password'])!.value;

    if (this.passwordForm.valid && password === this.encuesta!.contrasenna) {
      this.activeModal.close('success');
    } else {
      this.isWrong = true;
    }
  }

  cancel(): void {
    this.activeModal.close('cancel');
  }
}

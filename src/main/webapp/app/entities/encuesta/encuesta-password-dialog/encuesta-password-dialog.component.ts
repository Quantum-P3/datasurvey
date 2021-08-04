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
  isEval: boolean = false;
  encuesta?: IEncuesta;

  constructor(protected activeModal: NgbActiveModal, protected fb: FormBuilder) {}

  ngOnInit(): void {}

  submitPassword() {
    this.isEval = true;
    const password = this.passwordForm.get(['password'])!.value;

    if (this.passwordForm.valid && password === this.encuesta!.contrasenna) {
      this.isEval = false;
      //navegar a la vara
    }
  }

  cancel(): void {
    this.activeModal.dismiss();
  }
}

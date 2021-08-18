import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IEncuesta } from '../encuesta.model';

@Component({
  selector: 'jhi-encuesta-password-dialog',
  templateUrl: './encuesta-password-dialog.component.html',
  styleUrls: ['./encuesta-password-dialog.component.scss'],
})
export class EncuestaPasswordDialogComponent implements OnInit {
  encuesta?: IEncuesta;
  isWrong?: boolean;
  passwordInput?: string;

  constructor(protected activeModal: NgbActiveModal) {}

  ngOnInit(): void {}

  submitPassword() {
    if (this.passwordInput != undefined && this.passwordInput === this.encuesta!.contrasenna) {
      this.activeModal.close('success');
    } else {
      this.isWrong = true;
    }
  }

  cancel(): void {
    this.activeModal.close('cancel');
  }
}

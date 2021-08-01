import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-encuesta-password-dialog',
  templateUrl: './encuesta-password-dialog.component.html',
  styleUrls: ['./encuesta-password-dialog.component.scss'],
})
export class EncuestaPasswordDialogComponent implements OnInit {
  passwordForm = this.fb.group({
    password: [null, [Validators.required]],
  });

  constructor(protected activeModal: NgbActiveModal, protected fb: FormBuilder) {}

  ngOnInit(): void {}

  submitPassword() {}

  cancel(): void {
    this.activeModal.dismiss();
  }
}

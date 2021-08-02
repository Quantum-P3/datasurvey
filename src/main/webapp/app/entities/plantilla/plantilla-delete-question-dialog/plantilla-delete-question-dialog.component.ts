import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  templateUrl: './plantilla-delete-question-dialog.component.html',
})
export class PlantillaDeleteQuestionDialogComponent {
  constructor(protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(): void {
    this.activeModal.close('confirm');
  }
}

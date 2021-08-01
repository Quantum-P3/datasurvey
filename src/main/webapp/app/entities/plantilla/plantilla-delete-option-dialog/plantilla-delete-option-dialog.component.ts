import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  templateUrl: './plantilla-delete-option-dialog.component.html',
})
export class PlantillaDeleteOptionDialogComponent {
  constructor(protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(): void {
    this.activeModal.close('confirm');
  }
}

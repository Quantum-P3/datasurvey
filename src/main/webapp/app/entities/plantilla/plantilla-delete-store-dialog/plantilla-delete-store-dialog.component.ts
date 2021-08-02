import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  templateUrl: './plantilla-delete-store-dialog.component.html',
})
export class PlantillaDeleteStoreDialogComponent {
  constructor(protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDeleteFromStore(): void {
    this.activeModal.close('confirm');
  }
}

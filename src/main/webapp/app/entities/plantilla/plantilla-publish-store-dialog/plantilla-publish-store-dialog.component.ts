import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { faStore } from '@fortawesome/free-solid-svg-icons';

@Component({
  templateUrl: './plantilla-publish-store-dialog.component.html',
})
export class PlantillaPublishStoreDialogComponent {
  faStore = faStore;

  constructor(protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmPublishToStore(): void {
    this.activeModal.close('confirm');
  }
}

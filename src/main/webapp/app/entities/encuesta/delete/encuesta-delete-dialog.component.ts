import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEncuesta } from '../encuesta.model';
import { EncuestaService } from '../service/encuesta.service';

@Component({
  templateUrl: './encuesta-delete-dialog.component.html',
})
export class EncuestaDeleteDialogComponent {
  encuesta?: IEncuesta;

  constructor(protected encuestaService: EncuestaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.encuestaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

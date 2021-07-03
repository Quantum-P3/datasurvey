import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPPreguntaCerrada } from '../p-pregunta-cerrada.model';
import { PPreguntaCerradaService } from '../service/p-pregunta-cerrada.service';

@Component({
  templateUrl: './p-pregunta-cerrada-delete-dialog.component.html',
})
export class PPreguntaCerradaDeleteDialogComponent {
  pPreguntaCerrada?: IPPreguntaCerrada;

  constructor(protected pPreguntaCerradaService: PPreguntaCerradaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pPreguntaCerradaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

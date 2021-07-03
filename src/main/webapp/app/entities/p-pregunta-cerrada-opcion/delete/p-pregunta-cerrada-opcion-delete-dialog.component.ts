import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPPreguntaCerradaOpcion } from '../p-pregunta-cerrada-opcion.model';
import { PPreguntaCerradaOpcionService } from '../service/p-pregunta-cerrada-opcion.service';

@Component({
  templateUrl: './p-pregunta-cerrada-opcion-delete-dialog.component.html',
})
export class PPreguntaCerradaOpcionDeleteDialogComponent {
  pPreguntaCerradaOpcion?: IPPreguntaCerradaOpcion;

  constructor(protected pPreguntaCerradaOpcionService: PPreguntaCerradaOpcionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pPreguntaCerradaOpcionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

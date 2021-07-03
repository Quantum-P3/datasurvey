import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEPreguntaCerradaOpcion } from '../e-pregunta-cerrada-opcion.model';
import { EPreguntaCerradaOpcionService } from '../service/e-pregunta-cerrada-opcion.service';

@Component({
  templateUrl: './e-pregunta-cerrada-opcion-delete-dialog.component.html',
})
export class EPreguntaCerradaOpcionDeleteDialogComponent {
  ePreguntaCerradaOpcion?: IEPreguntaCerradaOpcion;

  constructor(protected ePreguntaCerradaOpcionService: EPreguntaCerradaOpcionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ePreguntaCerradaOpcionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEPreguntaCerrada } from '../e-pregunta-cerrada.model';
import { EPreguntaCerradaService } from '../service/e-pregunta-cerrada.service';

@Component({
  templateUrl: './e-pregunta-cerrada-delete-dialog.component.html',
})
export class EPreguntaCerradaDeleteDialogComponent {
  ePreguntaCerrada?: IEPreguntaCerrada;

  constructor(protected ePreguntaCerradaService: EPreguntaCerradaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ePreguntaCerradaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

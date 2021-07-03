import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEPreguntaAbierta } from '../e-pregunta-abierta.model';
import { EPreguntaAbiertaService } from '../service/e-pregunta-abierta.service';

@Component({
  templateUrl: './e-pregunta-abierta-delete-dialog.component.html',
})
export class EPreguntaAbiertaDeleteDialogComponent {
  ePreguntaAbierta?: IEPreguntaAbierta;

  constructor(protected ePreguntaAbiertaService: EPreguntaAbiertaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ePreguntaAbiertaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

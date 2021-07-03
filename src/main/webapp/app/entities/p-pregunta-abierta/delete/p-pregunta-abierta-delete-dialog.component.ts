import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPPreguntaAbierta } from '../p-pregunta-abierta.model';
import { PPreguntaAbiertaService } from '../service/p-pregunta-abierta.service';

@Component({
  templateUrl: './p-pregunta-abierta-delete-dialog.component.html',
})
export class PPreguntaAbiertaDeleteDialogComponent {
  pPreguntaAbierta?: IPPreguntaAbierta;

  constructor(protected pPreguntaAbiertaService: PPreguntaAbiertaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pPreguntaAbiertaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

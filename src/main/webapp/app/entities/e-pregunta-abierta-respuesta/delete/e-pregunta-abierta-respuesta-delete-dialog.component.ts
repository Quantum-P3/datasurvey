import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEPreguntaAbiertaRespuesta } from '../e-pregunta-abierta-respuesta.model';
import { EPreguntaAbiertaRespuestaService } from '../service/e-pregunta-abierta-respuesta.service';

@Component({
  templateUrl: './e-pregunta-abierta-respuesta-delete-dialog.component.html',
})
export class EPreguntaAbiertaRespuestaDeleteDialogComponent {
  ePreguntaAbiertaRespuesta?: IEPreguntaAbiertaRespuesta;

  constructor(protected ePreguntaAbiertaRespuestaService: EPreguntaAbiertaRespuestaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ePreguntaAbiertaRespuestaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

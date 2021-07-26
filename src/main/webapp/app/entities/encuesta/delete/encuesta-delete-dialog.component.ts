import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IEncuesta } from '../encuesta.model';
import { EncuestaService } from '../service/encuesta.service';
import { EstadoEncuesta } from 'app/entities/enumerations/estado-encuesta.model';

@Component({
  templateUrl: './encuesta-delete-dialog.component.html',
})
export class EncuestaDeleteDialogComponent {
  encuesta?: IEncuesta;

  constructor(protected encuestaService: EncuestaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(encuesta: IEncuesta): void {
    encuesta.estado = EstadoEncuesta.DELETED;
    this.encuestaService.deleteEncuesta(encuesta).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

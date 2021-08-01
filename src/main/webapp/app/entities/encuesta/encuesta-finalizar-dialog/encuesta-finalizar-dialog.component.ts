import { Component, OnInit } from '@angular/core';
import { IEncuesta } from '../encuesta.model';
import { EstadoEncuesta } from '../../enumerations/estado-encuesta.model';
import { EncuestaService } from '../service/encuesta.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from '../../../config/input.constants';

@Component({
  selector: 'jhi-encuesta-finalizar-dialog',
  templateUrl: './encuesta-finalizar-dialog.component.html',
  styleUrls: ['./encuesta-finalizar-dialog.component.scss'],
})
export class EncuestaFinalizarDialogComponent implements OnInit {
  encuesta?: IEncuesta;

  constructor(protected encuestaService: EncuestaService, protected activeModal: NgbActiveModal) {}

  ngOnInit(): void {}

  confirmFinalizar(encuesta: IEncuesta): void {
    debugger;
    const now = dayjs();

    encuesta.estado = EstadoEncuesta.FINISHED;
    encuesta.fechaFinalizada = dayjs(now, DATE_TIME_FORMAT);

    this.encuestaService.updateSurvey(encuesta).subscribe(() => {
      this.activeModal.close('finalized');
    });
  }

  cancel(): void {
    this.activeModal.dismiss();
  }
}

import { Component, OnInit } from '@angular/core';
import { IEncuesta } from '../encuesta.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EncuestaService } from '../service/encuesta.service';
import { EstadoEncuesta } from '../../enumerations/estado-encuesta.model';

@Component({
  selector: 'jhi-encuesta-publish-dialog',
  templateUrl: './encuesta-publish-dialog.component.html',
  styleUrls: ['./encuesta-publish-dialog.component.scss'],
})
export class EncuestaPublishDialogComponent implements OnInit {
  encuesta?: IEncuesta;

  constructor(protected encuestaService: EncuestaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmPublish(encuesta: IEncuesta): void {
    debugger;
    if (encuesta.estado === 'DRAFT') {
      encuesta.estado = EstadoEncuesta.ACTIVE;
    }

    this.encuestaService.update(encuesta).subscribe(() => {
      this.activeModal.close('published');
    });
  }

  ngOnInit(): void {}
}

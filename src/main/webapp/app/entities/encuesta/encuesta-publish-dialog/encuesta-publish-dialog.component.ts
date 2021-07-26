import { Component, OnInit } from '@angular/core';
import { IEncuesta } from '../encuesta.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EncuestaService } from '../service/encuesta.service';
import { EstadoEncuesta } from '../../enumerations/estado-encuesta.model';
import { AccesoEncuesta } from '../../enumerations/acceso-encuesta.model';
import { passwordResetFinishRoute } from '../../../account/password-reset/finish/password-reset-finish.route';

@Component({
  selector: 'jhi-encuesta-publish-dialog',
  templateUrl: './encuesta-publish-dialog.component.html',
  styleUrls: ['./encuesta-publish-dialog.component.scss'],
})
export class EncuestaPublishDialogComponent implements OnInit {
  encuesta?: IEncuesta;
  fechaFinalizar?: Date;
  fechaFinalizarInvalid?: boolean;

  constructor(protected encuestaService: EncuestaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmPublish(encuesta: IEncuesta): void {
    debugger;
    if (encuesta.estado === 'DRAFT') {
      encuesta.estado = EstadoEncuesta.ACTIVE;
    }

    if (encuesta.acceso === AccesoEncuesta.PRIVATE) {
      encuesta.contrasenna = this.generatePassword();
    }

    this.encuestaService.update(encuesta).subscribe(() => {
      this.activeModal.close('published');
    });
  }

  fechaFinalizarIsInvalid(): void {
    const now = new Date();
    debugger;
    this.fechaFinalizarInvalid = now < this.fechaFinalizar!;
  }

  generatePassword(): string {
    debugger;
    const alpha = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';

    let password = '';
    for (let i = 0; i < 5; i++) {
      password += alpha.charAt(Math.floor(Math.random() * alpha.length));
    }
    return password;
  }

  ngOnInit(): void {
    this.fechaFinalizar = new Date();
    this.fechaFinalizarInvalid = false;
  }
}

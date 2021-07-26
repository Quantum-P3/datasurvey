import { Component, OnInit } from '@angular/core';
import { IEncuesta } from '../encuesta.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EncuestaService } from '../service/encuesta.service';
import { EstadoEncuesta } from '../../enumerations/estado-encuesta.model';
import { AccesoEncuesta } from '../../enumerations/acceso-encuesta.model';
import { passwordResetFinishRoute } from '../../../account/password-reset/finish/password-reset-finish.route';
import { FormBuilder, Validators } from '@angular/forms';
import { IParametroAplicacion } from 'app/entities/parametro-aplicacion/parametro-aplicacion.model';
import { ParametroAplicacionService } from 'app/entities/parametro-aplicacion/service/parametro-aplicacion.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-encuesta-publish-dialog',
  templateUrl: './encuesta-publish-dialog.component.html',
  styleUrls: ['./encuesta-publish-dialog.component.scss'],
})
export class EncuestaPublishDialogComponent implements OnInit {
  encuesta?: IEncuesta;
  fechaFinalizacion?: Date;
  fechaFinalizarInvalid?: boolean;
  isLoading?: boolean;
  parametroAplicacions?: IParametroAplicacion[];
  isMin = false;
  isMax = false;
  fechaForm = this.fb.group({
    fechaFinalizacion: [null, [Validators.required]],
  });

  constructor(
    protected parametroAplicacionService: ParametroAplicacionService,
    protected encuestaService: EncuestaService,
    protected fb: FormBuilder,
    protected activeModal: NgbActiveModal
  ) {}

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

  loadAll(): void {
    this.isLoading = true;
    this.parametroAplicacionService.query().subscribe(
      (res: HttpResponse<IParametroAplicacion[]>) => {
        this.isLoading = false;
        this.parametroAplicacions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  fechaFinalizacionIsInvalid(): void {
    const now = new Date();
    const timeDiff = now.valueOf() - this.fechaFinalizacion!.valueOf();
    debugger;
    this.fechaFinalizarInvalid = now < this.fechaFinalizacion!;
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
    this.loadAll();
    this.fechaFinalizacion = new Date();
    this.fechaFinalizarInvalid = false;
    this.fechaFinalizacionIsInvalid();
  }
}

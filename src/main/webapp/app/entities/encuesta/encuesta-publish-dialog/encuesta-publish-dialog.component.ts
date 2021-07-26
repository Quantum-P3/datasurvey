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
import { DATE_FORMAT, DATE_TIME_FORMAT } from '../../../config/input.constants';
import * as dayjs from 'dayjs';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'jhi-encuesta-publish-dialog',
  templateUrl: './encuesta-publish-dialog.component.html',
  styleUrls: ['./encuesta-publish-dialog.component.scss'],
})
export class EncuestaPublishDialogComponent implements OnInit {
  encuesta?: IEncuesta;
  //fechaFinalizacion?: Date;
  fechaFinalizarInvalid?: boolean = false;
  fechaFinalizarInvalidMax?: boolean = false;
  isLoading?: boolean;
  parametroAplicacions?: IParametroAplicacion[];
  isMin = false;
  isMax = false;
  datoMin?: number;
  datoMax?: number;
  now = new Date();
  fechaForm = this.fb.group({
    fechaFinalizacion: [null, [Validators.required]],
  });

  constructor(
    protected parametroAplicacionService: ParametroAplicacionService,
    protected encuestaService: EncuestaService,
    protected fb: FormBuilder,
    protected activeModal: NgbActiveModal
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmPublish(encuesta: IEncuesta): void {
    this.fechaFinalizarInvalid = false;
    this.fechaFinalizarInvalidMax = false;

    const now = dayjs();
    debugger;

    /*this.loadAll()

    this.parametroAplicacions?.forEach(datos => {
      this.datoMin = datos.minDiasEncuesta;
      this.datoMax = datos.maxDiasEncuesta;
    });*/

    encuesta.fechaFinalizar = dayjs(this.fechaForm.get(['fechaFinalizacion'])!.value);
    encuesta.fechaPublicacion = dayjs(now, DATE_TIME_FORMAT);

    if (this.fechaFinalizacionIsInvalid(encuesta.fechaFinalizar, encuesta.fechaPublicacion)) {
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
  }

  loadAll(): void {
    this.isLoading = true;

    debugger;
    this.parametroAplicacionService
      .query()
      .pipe(finalize(() => this.onLoadFinalize()))
      .subscribe(
        (res: HttpResponse<IParametroAplicacion[]>) => {
          this.isLoading = false;
          this.parametroAplicacions = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  onLoadFinalize() {
    this.parametroAplicacions?.forEach(datos => {
      this.datoMin = datos.minDiasEncuesta;
      this.datoMax = datos.maxDiasEncuesta;
    });
    this.isLoading = false;
  }

  fechaFinalizacionIsInvalid(fechaFinalizar: dayjs.Dayjs, fechaPublicacion: dayjs.Dayjs): boolean {
    let numberDays: number;
    debugger;

    numberDays = fechaFinalizar?.diff(fechaPublicacion, 'days');

    if (numberDays <= this.datoMin!) {
      this.fechaFinalizarInvalid = true;
      return false;
    } else if (numberDays >= this.datoMax!) {
      this.fechaFinalizarInvalidMax = true;
      return false;
    } else {
      return true;
    }
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
}

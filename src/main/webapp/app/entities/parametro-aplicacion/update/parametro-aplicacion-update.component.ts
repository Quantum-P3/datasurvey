import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IParametroAplicacion, ParametroAplicacion } from '../parametro-aplicacion.model';
import { ParametroAplicacionService } from '../service/parametro-aplicacion.service';

@Component({
  selector: 'jhi-parametro-aplicacion-update',
  templateUrl: './parametro-aplicacion-update.component.html',
  styleUrls: ['./parametro-aplicacion-update.component.scss'],
})
export class ParametroAplicacionUpdateComponent implements OnInit {
  isSaving = false;
  minDiasIncorrect = false;
  minPreguntasIncorrect = false;
  notNumbers = false;
  notPositive = false;
  error = false;
  success = false;

  editForm = this.fb.group({
    id: [],
    maxDiasEncuesta: [null, [Validators.required, Validators.pattern(/^[0-9]\d*$/), Validators.min(1), Validators.max(14)]],
    minDiasEncuesta: [null, [Validators.required, Validators.pattern(/^[0-9]\d*$/), Validators.min(1), Validators.max(14)]],
    maxCantidadPreguntas: [null, [Validators.required, Validators.pattern(/^[0-9]\d*$/), Validators.min(1), Validators.max(40)]],
    minCantidadPreguntas: [null, [Validators.required, Validators.pattern(/^[0-9]\d*$/), Validators.min(1), Validators.max(40)]],
  });

  constructor(
    protected parametroAplicacionService: ParametroAplicacionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parametroAplicacion }) => {
      this.updateForm(parametroAplicacion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.minDiasIncorrect = false;
    this.minPreguntasIncorrect = false;
    this.notNumbers = false;
    this.notPositive = false;

    const minCantDias = this.editForm.get(['minDiasEncuesta'])!.value;
    const maxCantDias = this.editForm.get(['maxDiasEncuesta'])!.value;
    const minCantPreguntas = this.editForm.get(['minCantidadPreguntas'])!.value;
    const maxCantPreguntas = this.editForm.get(['maxCantidadPreguntas'])!.value;

    if (minCantDias > maxCantDias) {
      this.minDiasIncorrect = true;
    } else if (minCantPreguntas > maxCantPreguntas) {
      this.minPreguntasIncorrect = true;
    } else if (
      !Number.isInteger(minCantDias) ||
      !Number.isInteger(maxCantDias) ||
      !Number.isInteger(minCantPreguntas) ||
      !Number.isInteger(maxCantPreguntas)
    ) {
      this.notNumbers = true;
    } else if (minCantDias < 1 || maxCantDias < 1 || minCantPreguntas < 1 || maxCantPreguntas < 1) {
      this.notPositive = true;
    } else {
      this.isSaving = true;
      const parametroAplicacion = this.createFromForm();
      if (parametroAplicacion.id !== undefined) {
        this.subscribeToSaveResponse(this.parametroAplicacionService.update(parametroAplicacion));
      } else {
        this.subscribeToSaveResponse(this.parametroAplicacionService.create(parametroAplicacion));
      }
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParametroAplicacion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    (this.success = true), this.windowReload();
  }

  windowReload() {
    this.router.navigate(['parametro-aplicacion/1/edit']).then(() => {
      window.location.reload();
    });
  }
  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(parametroAplicacion: IParametroAplicacion): void {
    this.editForm.patchValue({
      id: parametroAplicacion.id,
      maxDiasEncuesta: parametroAplicacion.maxDiasEncuesta,
      minDiasEncuesta: parametroAplicacion.minDiasEncuesta,
      maxCantidadPreguntas: parametroAplicacion.maxCantidadPreguntas,
      minCantidadPreguntas: parametroAplicacion.minCantidadPreguntas,
    });
  }

  protected createFromForm(): IParametroAplicacion {
    return {
      ...new ParametroAplicacion(),
      id: this.editForm.get(['id'])!.value,
      maxDiasEncuesta: this.editForm.get(['maxDiasEncuesta'])!.value,
      minDiasEncuesta: this.editForm.get(['minDiasEncuesta'])!.value,
      maxCantidadPreguntas: this.editForm.get(['maxCantidadPreguntas'])!.value,
      minCantidadPreguntas: this.editForm.get(['minCantidadPreguntas'])!.value,
    };
  }

  private validations() {}
}

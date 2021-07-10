import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
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

  editForm = this.fb.group({
    id: [],
    maxDiasEncuesta: [null, [Validators.required]],
    minDiasEncuesta: [null, [Validators.required]],
    maxCantidadPreguntas: [null, [Validators.required]],
    minCantidadPreguntas: [null, [Validators.required]],
  });

  constructor(
    protected parametroAplicacionService: ParametroAplicacionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
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
    this.isSaving = true;
    const parametroAplicacion = this.createFromForm();
    if (parametroAplicacion.id !== undefined) {
      this.subscribeToSaveResponse(this.parametroAplicacionService.update(parametroAplicacion));
    } else {
      this.subscribeToSaveResponse(this.parametroAplicacionService.create(parametroAplicacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParametroAplicacion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
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
}

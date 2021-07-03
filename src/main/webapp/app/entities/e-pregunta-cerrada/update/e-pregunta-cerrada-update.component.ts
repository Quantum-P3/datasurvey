import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEPreguntaCerrada, EPreguntaCerrada } from '../e-pregunta-cerrada.model';
import { EPreguntaCerradaService } from '../service/e-pregunta-cerrada.service';
import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { EncuestaService } from 'app/entities/encuesta/service/encuesta.service';

@Component({
  selector: 'jhi-e-pregunta-cerrada-update',
  templateUrl: './e-pregunta-cerrada-update.component.html',
})
export class EPreguntaCerradaUpdateComponent implements OnInit {
  isSaving = false;

  encuestasSharedCollection: IEncuesta[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    tipo: [null, [Validators.required]],
    opcional: [null, [Validators.required]],
    orden: [null, [Validators.required]],
    encuesta: [],
  });

  constructor(
    protected ePreguntaCerradaService: EPreguntaCerradaService,
    protected encuestaService: EncuestaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ePreguntaCerrada }) => {
      this.updateForm(ePreguntaCerrada);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ePreguntaCerrada = this.createFromForm();
    if (ePreguntaCerrada.id !== undefined) {
      this.subscribeToSaveResponse(this.ePreguntaCerradaService.update(ePreguntaCerrada));
    } else {
      this.subscribeToSaveResponse(this.ePreguntaCerradaService.create(ePreguntaCerrada));
    }
  }

  trackEncuestaById(index: number, item: IEncuesta): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEPreguntaCerrada>>): void {
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

  protected updateForm(ePreguntaCerrada: IEPreguntaCerrada): void {
    this.editForm.patchValue({
      id: ePreguntaCerrada.id,
      nombre: ePreguntaCerrada.nombre,
      tipo: ePreguntaCerrada.tipo,
      opcional: ePreguntaCerrada.opcional,
      orden: ePreguntaCerrada.orden,
      encuesta: ePreguntaCerrada.encuesta,
    });

    this.encuestasSharedCollection = this.encuestaService.addEncuestaToCollectionIfMissing(
      this.encuestasSharedCollection,
      ePreguntaCerrada.encuesta
    );
  }

  protected loadRelationshipsOptions(): void {
    this.encuestaService
      .query()
      .pipe(map((res: HttpResponse<IEncuesta[]>) => res.body ?? []))
      .pipe(
        map((encuestas: IEncuesta[]) =>
          this.encuestaService.addEncuestaToCollectionIfMissing(encuestas, this.editForm.get('encuesta')!.value)
        )
      )
      .subscribe((encuestas: IEncuesta[]) => (this.encuestasSharedCollection = encuestas));
  }

  protected createFromForm(): IEPreguntaCerrada {
    return {
      ...new EPreguntaCerrada(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      opcional: this.editForm.get(['opcional'])!.value,
      orden: this.editForm.get(['orden'])!.value,
      encuesta: this.editForm.get(['encuesta'])!.value,
    };
  }
}

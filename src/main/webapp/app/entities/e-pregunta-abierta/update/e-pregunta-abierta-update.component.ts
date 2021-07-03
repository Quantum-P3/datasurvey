import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEPreguntaAbierta, EPreguntaAbierta } from '../e-pregunta-abierta.model';
import { EPreguntaAbiertaService } from '../service/e-pregunta-abierta.service';
import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { EncuestaService } from 'app/entities/encuesta/service/encuesta.service';

@Component({
  selector: 'jhi-e-pregunta-abierta-update',
  templateUrl: './e-pregunta-abierta-update.component.html',
})
export class EPreguntaAbiertaUpdateComponent implements OnInit {
  isSaving = false;

  encuestasSharedCollection: IEncuesta[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    opcional: [null, [Validators.required]],
    orden: [null, [Validators.required]],
    encuesta: [],
  });

  constructor(
    protected ePreguntaAbiertaService: EPreguntaAbiertaService,
    protected encuestaService: EncuestaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ePreguntaAbierta }) => {
      this.updateForm(ePreguntaAbierta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ePreguntaAbierta = this.createFromForm();
    if (ePreguntaAbierta.id !== undefined) {
      this.subscribeToSaveResponse(this.ePreguntaAbiertaService.update(ePreguntaAbierta));
    } else {
      this.subscribeToSaveResponse(this.ePreguntaAbiertaService.create(ePreguntaAbierta));
    }
  }

  trackEncuestaById(index: number, item: IEncuesta): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEPreguntaAbierta>>): void {
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

  protected updateForm(ePreguntaAbierta: IEPreguntaAbierta): void {
    this.editForm.patchValue({
      id: ePreguntaAbierta.id,
      nombre: ePreguntaAbierta.nombre,
      opcional: ePreguntaAbierta.opcional,
      orden: ePreguntaAbierta.orden,
      encuesta: ePreguntaAbierta.encuesta,
    });

    this.encuestasSharedCollection = this.encuestaService.addEncuestaToCollectionIfMissing(
      this.encuestasSharedCollection,
      ePreguntaAbierta.encuesta
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

  protected createFromForm(): IEPreguntaAbierta {
    return {
      ...new EPreguntaAbierta(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      opcional: this.editForm.get(['opcional'])!.value,
      orden: this.editForm.get(['orden'])!.value,
      encuesta: this.editForm.get(['encuesta'])!.value,
    };
  }
}

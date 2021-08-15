import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEPreguntaAbiertaRespuesta, EPreguntaAbiertaRespuesta } from '../e-pregunta-abierta-respuesta.model';
import { EPreguntaAbiertaRespuestaService } from '../service/e-pregunta-abierta-respuesta.service';
import { IEPreguntaAbierta } from 'app/entities/e-pregunta-abierta/e-pregunta-abierta.model';
import { EPreguntaAbiertaService } from 'app/entities/e-pregunta-abierta/service/e-pregunta-abierta.service';

@Component({
  selector: 'jhi-e-pregunta-abierta-respuesta-update',
  templateUrl: './e-pregunta-abierta-respuesta-update.component.html',
})
export class EPreguntaAbiertaRespuestaUpdateComponent implements OnInit {
  isSaving = false;

  ePreguntaAbiertasSharedCollection: IEPreguntaAbierta[] = [];

  editForm = this.fb.group({
    id: [],
    respuesta: [null, [Validators.required]],
    ePreguntaAbierta: [],
  });

  constructor(
    protected ePreguntaAbiertaRespuestaService: EPreguntaAbiertaRespuestaService,
    protected ePreguntaAbiertaService: EPreguntaAbiertaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ePreguntaAbiertaRespuesta }) => {
      this.updateForm(ePreguntaAbiertaRespuesta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ePreguntaAbiertaRespuesta = this.createFromForm();
    if (ePreguntaAbiertaRespuesta.id !== undefined) {
      this.subscribeToSaveResponse(this.ePreguntaAbiertaRespuestaService.update(ePreguntaAbiertaRespuesta));
    } else {
      this.subscribeToSaveResponse(this.ePreguntaAbiertaRespuestaService.create(ePreguntaAbiertaRespuesta));
    }
  }

  trackEPreguntaAbiertaById(index: number, item: IEPreguntaAbierta): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEPreguntaAbiertaRespuesta>>): void {
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

  protected updateForm(ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta): void {
    this.editForm.patchValue({
      id: ePreguntaAbiertaRespuesta.id,
      respuesta: ePreguntaAbiertaRespuesta.respuesta,
      ePreguntaAbierta: ePreguntaAbiertaRespuesta.epreguntaAbierta,
    });

    this.ePreguntaAbiertasSharedCollection = this.ePreguntaAbiertaService.addEPreguntaAbiertaToCollectionIfMissing(
      this.ePreguntaAbiertasSharedCollection,
      ePreguntaAbiertaRespuesta.epreguntaAbierta
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ePreguntaAbiertaService
      .query()
      .pipe(map((res: HttpResponse<IEPreguntaAbierta[]>) => res.body ?? []))
      .pipe(
        map((ePreguntaAbiertas: IEPreguntaAbierta[]) =>
          this.ePreguntaAbiertaService.addEPreguntaAbiertaToCollectionIfMissing(
            ePreguntaAbiertas,
            this.editForm.get('ePreguntaAbierta')!.value
          )
        )
      )
      .subscribe((ePreguntaAbiertas: IEPreguntaAbierta[]) => (this.ePreguntaAbiertasSharedCollection = ePreguntaAbiertas));
  }

  protected createFromForm(): IEPreguntaAbiertaRespuesta {
    return {
      ...new EPreguntaAbiertaRespuesta(),
      id: this.editForm.get(['id'])!.value,
      respuesta: this.editForm.get(['respuesta'])!.value,
      epreguntaAbierta: this.editForm.get(['ePreguntaAbierta'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEPreguntaCerradaOpcion, EPreguntaCerradaOpcion } from '../e-pregunta-cerrada-opcion.model';
import { EPreguntaCerradaOpcionService } from '../service/e-pregunta-cerrada-opcion.service';
import { IEPreguntaCerrada } from 'app/entities/e-pregunta-cerrada/e-pregunta-cerrada.model';
import { EPreguntaCerradaService } from 'app/entities/e-pregunta-cerrada/service/e-pregunta-cerrada.service';

@Component({
  selector: 'jhi-e-pregunta-cerrada-opcion-update',
  templateUrl: './e-pregunta-cerrada-opcion-update.component.html',
})
export class EPreguntaCerradaOpcionUpdateComponent implements OnInit {
  isSaving = false;

  ePreguntaCerradasSharedCollection: IEPreguntaCerrada[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    orden: [null, [Validators.required]],
    cantidad: [null, [Validators.required]],
    ePreguntaCerrada: [],
  });

  constructor(
    protected ePreguntaCerradaOpcionService: EPreguntaCerradaOpcionService,
    protected ePreguntaCerradaService: EPreguntaCerradaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ePreguntaCerradaOpcion }) => {
      this.updateForm(ePreguntaCerradaOpcion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ePreguntaCerradaOpcion = this.createFromForm();
    if (ePreguntaCerradaOpcion.id !== undefined) {
      this.subscribeToSaveResponse(this.ePreguntaCerradaOpcionService.update(ePreguntaCerradaOpcion));
    } else {
      this.subscribeToSaveResponse(this.ePreguntaCerradaOpcionService.create(ePreguntaCerradaOpcion));
    }
  }

  trackEPreguntaCerradaById(index: number, item: IEPreguntaCerrada): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEPreguntaCerradaOpcion>>): void {
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

  protected updateForm(ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion): void {
    this.editForm.patchValue({
      id: ePreguntaCerradaOpcion.id,
      nombre: ePreguntaCerradaOpcion.nombre,
      orden: ePreguntaCerradaOpcion.orden,
      cantidad: ePreguntaCerradaOpcion.cantidad,
      ePreguntaCerrada: ePreguntaCerradaOpcion.ePreguntaCerrada,
    });

    this.ePreguntaCerradasSharedCollection = this.ePreguntaCerradaService.addEPreguntaCerradaToCollectionIfMissing(
      this.ePreguntaCerradasSharedCollection,
      ePreguntaCerradaOpcion.ePreguntaCerrada
    );
  }

  protected loadRelationshipsOptions(): void {
    this.ePreguntaCerradaService
      .query()
      .pipe(map((res: HttpResponse<IEPreguntaCerrada[]>) => res.body ?? []))
      .pipe(
        map((ePreguntaCerradas: IEPreguntaCerrada[]) =>
          this.ePreguntaCerradaService.addEPreguntaCerradaToCollectionIfMissing(
            ePreguntaCerradas,
            this.editForm.get('ePreguntaCerrada')!.value
          )
        )
      )
      .subscribe((ePreguntaCerradas: IEPreguntaCerrada[]) => (this.ePreguntaCerradasSharedCollection = ePreguntaCerradas));
  }

  protected createFromForm(): IEPreguntaCerradaOpcion {
    return {
      ...new EPreguntaCerradaOpcion(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      orden: this.editForm.get(['orden'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      ePreguntaCerrada: this.editForm.get(['ePreguntaCerrada'])!.value,
    };
  }
}

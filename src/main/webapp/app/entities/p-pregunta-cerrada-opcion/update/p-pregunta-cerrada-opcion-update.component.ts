import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPPreguntaCerradaOpcion, PPreguntaCerradaOpcion } from '../p-pregunta-cerrada-opcion.model';
import { PPreguntaCerradaOpcionService } from '../service/p-pregunta-cerrada-opcion.service';
import { IPPreguntaCerrada } from 'app/entities/p-pregunta-cerrada/p-pregunta-cerrada.model';
import { PPreguntaCerradaService } from 'app/entities/p-pregunta-cerrada/service/p-pregunta-cerrada.service';

@Component({
  selector: 'jhi-p-pregunta-cerrada-opcion-update',
  templateUrl: './p-pregunta-cerrada-opcion-update.component.html',
})
export class PPreguntaCerradaOpcionUpdateComponent implements OnInit {
  isSaving = false;

  pPreguntaCerradasSharedCollection: IPPreguntaCerrada[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    orden: [null, [Validators.required]],
    pPreguntaCerrada: [],
  });

  constructor(
    protected pPreguntaCerradaOpcionService: PPreguntaCerradaOpcionService,
    protected pPreguntaCerradaService: PPreguntaCerradaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pPreguntaCerradaOpcion }) => {
      this.updateForm(pPreguntaCerradaOpcion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pPreguntaCerradaOpcion = this.createFromForm();
    if (pPreguntaCerradaOpcion.id !== undefined) {
      this.subscribeToSaveResponse(this.pPreguntaCerradaOpcionService.update(pPreguntaCerradaOpcion));
    } else {
      this.subscribeToSaveResponse(this.pPreguntaCerradaOpcionService.create(pPreguntaCerradaOpcion));
    }
  }

  trackPPreguntaCerradaById(index: number, item: IPPreguntaCerrada): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPPreguntaCerradaOpcion>>): void {
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

  protected updateForm(pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion): void {
    this.editForm.patchValue({
      id: pPreguntaCerradaOpcion.id,
      nombre: pPreguntaCerradaOpcion.nombre,
      orden: pPreguntaCerradaOpcion.orden,
      pPreguntaCerrada: pPreguntaCerradaOpcion.pPreguntaCerrada,
    });

    this.pPreguntaCerradasSharedCollection = this.pPreguntaCerradaService.addPPreguntaCerradaToCollectionIfMissing(
      this.pPreguntaCerradasSharedCollection,
      pPreguntaCerradaOpcion.pPreguntaCerrada
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pPreguntaCerradaService
      .query()
      .pipe(map((res: HttpResponse<IPPreguntaCerrada[]>) => res.body ?? []))
      .pipe(
        map((pPreguntaCerradas: IPPreguntaCerrada[]) =>
          this.pPreguntaCerradaService.addPPreguntaCerradaToCollectionIfMissing(
            pPreguntaCerradas,
            this.editForm.get('pPreguntaCerrada')!.value
          )
        )
      )
      .subscribe((pPreguntaCerradas: IPPreguntaCerrada[]) => (this.pPreguntaCerradasSharedCollection = pPreguntaCerradas));
  }

  protected createFromForm(): IPPreguntaCerradaOpcion {
    return {
      ...new PPreguntaCerradaOpcion(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      orden: this.editForm.get(['orden'])!.value,
      pPreguntaCerrada: this.editForm.get(['pPreguntaCerrada'])!.value,
    };
  }
}

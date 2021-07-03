import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPPreguntaCerrada, PPreguntaCerrada } from '../p-pregunta-cerrada.model';
import { PPreguntaCerradaService } from '../service/p-pregunta-cerrada.service';
import { IPlantilla } from 'app/entities/plantilla/plantilla.model';
import { PlantillaService } from 'app/entities/plantilla/service/plantilla.service';

@Component({
  selector: 'jhi-p-pregunta-cerrada-update',
  templateUrl: './p-pregunta-cerrada-update.component.html',
})
export class PPreguntaCerradaUpdateComponent implements OnInit {
  isSaving = false;

  plantillasSharedCollection: IPlantilla[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    tipo: [null, [Validators.required]],
    opcional: [null, [Validators.required]],
    orden: [null, [Validators.required]],
    plantilla: [],
  });

  constructor(
    protected pPreguntaCerradaService: PPreguntaCerradaService,
    protected plantillaService: PlantillaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pPreguntaCerrada }) => {
      this.updateForm(pPreguntaCerrada);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pPreguntaCerrada = this.createFromForm();
    if (pPreguntaCerrada.id !== undefined) {
      this.subscribeToSaveResponse(this.pPreguntaCerradaService.update(pPreguntaCerrada));
    } else {
      this.subscribeToSaveResponse(this.pPreguntaCerradaService.create(pPreguntaCerrada));
    }
  }

  trackPlantillaById(index: number, item: IPlantilla): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPPreguntaCerrada>>): void {
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

  protected updateForm(pPreguntaCerrada: IPPreguntaCerrada): void {
    this.editForm.patchValue({
      id: pPreguntaCerrada.id,
      nombre: pPreguntaCerrada.nombre,
      tipo: pPreguntaCerrada.tipo,
      opcional: pPreguntaCerrada.opcional,
      orden: pPreguntaCerrada.orden,
      plantilla: pPreguntaCerrada.plantilla,
    });

    this.plantillasSharedCollection = this.plantillaService.addPlantillaToCollectionIfMissing(
      this.plantillasSharedCollection,
      pPreguntaCerrada.plantilla
    );
  }

  protected loadRelationshipsOptions(): void {
    this.plantillaService
      .query()
      .pipe(map((res: HttpResponse<IPlantilla[]>) => res.body ?? []))
      .pipe(
        map((plantillas: IPlantilla[]) =>
          this.plantillaService.addPlantillaToCollectionIfMissing(plantillas, this.editForm.get('plantilla')!.value)
        )
      )
      .subscribe((plantillas: IPlantilla[]) => (this.plantillasSharedCollection = plantillas));
  }

  protected createFromForm(): IPPreguntaCerrada {
    return {
      ...new PPreguntaCerrada(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      opcional: this.editForm.get(['opcional'])!.value,
      orden: this.editForm.get(['orden'])!.value,
      plantilla: this.editForm.get(['plantilla'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPPreguntaAbierta, PPreguntaAbierta } from '../p-pregunta-abierta.model';
import { PPreguntaAbiertaService } from '../service/p-pregunta-abierta.service';
import { IPlantilla } from 'app/entities/plantilla/plantilla.model';
import { PlantillaService } from 'app/entities/plantilla/service/plantilla.service';

@Component({
  selector: 'jhi-p-pregunta-abierta-update',
  templateUrl: './p-pregunta-abierta-update.component.html',
})
export class PPreguntaAbiertaUpdateComponent implements OnInit {
  isSaving = false;

  plantillasSharedCollection: IPlantilla[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    opcional: [null, [Validators.required]],
    orden: [null, [Validators.required]],
    plantilla: [],
  });

  constructor(
    protected pPreguntaAbiertaService: PPreguntaAbiertaService,
    protected plantillaService: PlantillaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pPreguntaAbierta }) => {
      this.updateForm(pPreguntaAbierta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pPreguntaAbierta = this.createFromForm();
    if (pPreguntaAbierta.id !== undefined) {
      this.subscribeToSaveResponse(this.pPreguntaAbiertaService.update(pPreguntaAbierta));
    } else {
      this.subscribeToSaveResponse(this.pPreguntaAbiertaService.create(pPreguntaAbierta));
    }
  }

  trackPlantillaById(index: number, item: IPlantilla): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPPreguntaAbierta>>): void {
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

  protected updateForm(pPreguntaAbierta: IPPreguntaAbierta): void {
    this.editForm.patchValue({
      id: pPreguntaAbierta.id,
      nombre: pPreguntaAbierta.nombre,
      opcional: pPreguntaAbierta.opcional,
      orden: pPreguntaAbierta.orden,
      plantilla: pPreguntaAbierta.plantilla,
    });

    this.plantillasSharedCollection = this.plantillaService.addPlantillaToCollectionIfMissing(
      this.plantillasSharedCollection,
      pPreguntaAbierta.plantilla
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

  protected createFromForm(): IPPreguntaAbierta {
    return {
      ...new PPreguntaAbierta(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      opcional: this.editForm.get(['opcional'])!.value,
      orden: this.editForm.get(['orden'])!.value,
      plantilla: this.editForm.get(['plantilla'])!.value,
    };
  }
}

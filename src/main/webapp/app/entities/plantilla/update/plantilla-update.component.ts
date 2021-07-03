import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPlantilla, Plantilla } from '../plantilla.model';
import { PlantillaService } from '../service/plantilla.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';

@Component({
  selector: 'jhi-plantilla-update',
  templateUrl: './plantilla-update.component.html',
})
export class PlantillaUpdateComponent implements OnInit {
  isSaving = false;

  categoriasSharedCollection: ICategoria[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.minLength(1), Validators.maxLength(50)]],
    descripcion: [],
    fechaCreacion: [null, [Validators.required]],
    fechaPublicacionTienda: [],
    estado: [null, [Validators.required]],
    precio: [null, [Validators.required]],
    categoria: [],
  });

  constructor(
    protected plantillaService: PlantillaService,
    protected categoriaService: CategoriaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plantilla }) => {
      if (plantilla.id === undefined) {
        const today = dayjs().startOf('day');
        plantilla.fechaCreacion = today;
        plantilla.fechaPublicacionTienda = today;
      }

      this.updateForm(plantilla);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plantilla = this.createFromForm();
    if (plantilla.id !== undefined) {
      this.subscribeToSaveResponse(this.plantillaService.update(plantilla));
    } else {
      this.subscribeToSaveResponse(this.plantillaService.create(plantilla));
    }
  }

  trackCategoriaById(index: number, item: ICategoria): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlantilla>>): void {
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

  protected updateForm(plantilla: IPlantilla): void {
    this.editForm.patchValue({
      id: plantilla.id,
      nombre: plantilla.nombre,
      descripcion: plantilla.descripcion,
      fechaCreacion: plantilla.fechaCreacion ? plantilla.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      fechaPublicacionTienda: plantilla.fechaPublicacionTienda ? plantilla.fechaPublicacionTienda.format(DATE_TIME_FORMAT) : null,
      estado: plantilla.estado,
      precio: plantilla.precio,
      categoria: plantilla.categoria,
    });

    this.categoriasSharedCollection = this.categoriaService.addCategoriaToCollectionIfMissing(
      this.categoriasSharedCollection,
      plantilla.categoria
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaService
      .query()
      .pipe(map((res: HttpResponse<ICategoria[]>) => res.body ?? []))
      .pipe(
        map((categorias: ICategoria[]) =>
          this.categoriaService.addCategoriaToCollectionIfMissing(categorias, this.editForm.get('categoria')!.value)
        )
      )
      .subscribe((categorias: ICategoria[]) => (this.categoriasSharedCollection = categorias));
  }

  protected createFromForm(): IPlantilla {
    return {
      ...new Plantilla(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaPublicacionTienda: this.editForm.get(['fechaPublicacionTienda'])!.value
        ? dayjs(this.editForm.get(['fechaPublicacionTienda'])!.value, DATE_TIME_FORMAT)
        : undefined,
      estado: this.editForm.get(['estado'])!.value,
      precio: this.editForm.get(['precio'])!.value,
      categoria: this.editForm.get(['categoria'])!.value,
    };
  }
}

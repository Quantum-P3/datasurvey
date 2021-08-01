import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlantilla, Plantilla } from '../plantilla.model';
import { PlantillaService } from '../service/plantilla.service';
import { PlantillaDeleteDialogComponent } from '../delete/plantilla-delete-dialog.component';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { FormBuilder, Validators } from '@angular/forms';
import { EstadoPlantilla } from 'app/entities/enumerations/estado-plantilla.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

@Component({
  selector: 'jhi-plantilla',
  templateUrl: './plantilla.component.html',
})
export class PlantillaComponent implements OnInit {
  plantillas?: IPlantilla[];
  isLoading = false;
  isSaving = false;
  createAnotherTemplate: Boolean = false;

  account: Account | null = null;
  categoriasSharedCollection: ICategoria[] = [];

  templateCreateForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.minLength(1), Validators.maxLength(50)]],
    descripcion: [],
    precio: [null, [Validators.required]],
    categoria: [],
  });

  constructor(
    protected plantillaService: PlantillaService,
    protected modalService: NgbModal,
    protected accountService: AccountService,
    protected fb: FormBuilder,
    protected categoriaService: CategoriaService
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.plantillaService.query().subscribe(
      (res: HttpResponse<IPlantilla[]>) => {
        this.isLoading = false;
        this.plantillas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
    this.loadRelationshipsOptions();
  }

  trackId(_index: number, item: IPlantilla): number {
    return item.id!;
  }

  delete(plantilla: IPlantilla): void {
    const modalRef = this.modalService.open(PlantillaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plantilla = plantilla;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  isAdmin(): boolean {
    return this.accountService.hasAnyAuthority('ROLE_ADMIN');
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  resetCreateTemplateForm(): void {
    this.templateCreateForm.reset();
  }

  createAnotherTemplateChange(event: any): void {
    // ID: #crearPlantilla
    this.createAnotherTemplate = event.target.checked;
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
    this.templateCreateForm.reset();
    this.plantillas = [];
    this.loadAll();
    if (!this.createAnotherTemplate) {
      $('#cancelBtn').click();
    }
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected loadRelationshipsOptions(): void {
    this.categoriaService
      .query()
      .pipe(map((res: HttpResponse<ICategoria[]>) => res.body ?? []))
      .pipe(
        map((categorias: ICategoria[]) =>
          this.categoriaService.addCategoriaToCollectionIfMissing(categorias, this.templateCreateForm.get('categoria')!.value)
        )
      )
      .subscribe((categorias: ICategoria[]) => (this.categoriasSharedCollection = categorias));
  }

  protected createFromForm(): IPlantilla {
    const now = dayjs();

    return {
      ...new Plantilla(),
      id: undefined,
      nombre: this.templateCreateForm.get(['nombre'])!.value,
      descripcion: this.templateCreateForm.get(['descripcion'])!.value,
      fechaCreacion: dayjs(now, DATE_TIME_FORMAT),
      estado: EstadoPlantilla.DRAFT,
      precio: this.templateCreateForm.get(['precio'])!.value,
      categoria: this.templateCreateForm.get(['categoria'])!.value,
    };
  }
}

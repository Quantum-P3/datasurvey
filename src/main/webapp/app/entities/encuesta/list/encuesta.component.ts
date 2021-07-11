import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEncuesta, Encuesta } from '../encuesta.model';
import { EncuestaService } from '../service/encuesta.service';
import { EncuestaDeleteDialogComponent } from '../delete/encuesta-delete-dialog.component';

import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IUsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';

@Component({
  selector: 'jhi-encuesta',
  templateUrl: './encuesta.component.html',
})
export class EncuestaComponent implements OnInit {
  encuestas?: IEncuesta[];
  isLoading = false;

  isSaving = false;

  categoriasSharedCollection: ICategoria[] = [];
  usuarioExtrasSharedCollection: IUsuarioExtra[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    descripcion: [],
    fechaCreacion: [null, [Validators.required]],
    fechaPublicacion: [],
    fechaFinalizar: [],
    fechaFinalizada: [],
    calificacion: [null, [Validators.required]],
    acceso: [null, [Validators.required]],
    contrasenna: [],
    estado: [null, [Validators.required]],
    categoria: [],
    usuarioExtra: [],
  });

  constructor(
    protected encuestaService: EncuestaService,
    protected modalService: NgbModal,
    protected categoriaService: CategoriaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.encuestaService.query().subscribe(
      (res: HttpResponse<IEncuesta[]>) => {
        this.isLoading = false;
        this.encuestas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();

    // Call upon selecting a survey to edit
    // this.updateForm(encuesta);

    this.loadRelationshipsOptions();

    // this.activatedRoute.data.subscribe(({ encuesta }) => {
    //   if (encuesta.id === undefined) {
    //     const today = dayjs().startOf('day');
    //     encuesta.fechaCreacion = today;
    //     encuesta.fechaPublicacion = today;
    //     encuesta.fechaFinalizar = today;
    //     encuesta.fechaFinalizada = today;
    //   }

    //   this.updateForm(encuesta);

    // });
  }

  trackId(index: number, item: IEncuesta): number {
    return item.id!;
  }

  delete(encuesta: IEncuesta): void {
    const modalRef = this.modalService.open(EncuestaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.encuesta = encuesta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const encuesta = this.createFromForm();
    console.log(encuesta);

    if (encuesta.id !== null) {
      this.subscribeToSaveResponse(this.encuestaService.update(encuesta));
    } else {
      this.subscribeToSaveResponse(this.encuestaService.create(encuesta));
    }
  }

  trackCategoriaById(index: number, item: ICategoria): number {
    return item.id!;
  }

  trackUsuarioExtraById(index: number, item: IUsuarioExtra): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEncuesta>>): void {
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

  protected updateForm(encuesta: IEncuesta): void {
    this.editForm.patchValue({
      id: encuesta.id,
      nombre: encuesta.nombre,
      descripcion: encuesta.descripcion,
      fechaCreacion: encuesta.fechaCreacion ? encuesta.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      fechaPublicacion: encuesta.fechaPublicacion ? encuesta.fechaPublicacion.format(DATE_TIME_FORMAT) : null,
      fechaFinalizar: encuesta.fechaFinalizar ? encuesta.fechaFinalizar.format(DATE_TIME_FORMAT) : null,
      fechaFinalizada: encuesta.fechaFinalizada ? encuesta.fechaFinalizada.format(DATE_TIME_FORMAT) : null,
      calificacion: encuesta.calificacion,
      acceso: encuesta.acceso,
      contrasenna: encuesta.contrasenna,
      estado: encuesta.estado,
      categoria: encuesta.categoria,
      usuarioExtra: encuesta.usuarioExtra,
    });

    this.categoriasSharedCollection = this.categoriaService.addCategoriaToCollectionIfMissing(
      this.categoriasSharedCollection,
      encuesta.categoria
    );
    this.usuarioExtrasSharedCollection = this.usuarioExtraService.addUsuarioExtraToCollectionIfMissing(
      this.usuarioExtrasSharedCollection,
      encuesta.usuarioExtra
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

    this.usuarioExtraService
      .query()
      .pipe(map((res: HttpResponse<IUsuarioExtra[]>) => res.body ?? []))
      .pipe(
        map((usuarioExtras: IUsuarioExtra[]) =>
          this.usuarioExtraService.addUsuarioExtraToCollectionIfMissing(usuarioExtras, this.editForm.get('usuarioExtra')!.value)
        )
      )
      .subscribe((usuarioExtras: IUsuarioExtra[]) => (this.usuarioExtrasSharedCollection = usuarioExtras));
  }

  protected createFromForm(): IEncuesta {
    return {
      ...new Encuesta(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaPublicacion: this.editForm.get(['fechaPublicacion'])!.value
        ? dayjs(this.editForm.get(['fechaPublicacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaFinalizar: this.editForm.get(['fechaFinalizar'])!.value
        ? dayjs(this.editForm.get(['fechaFinalizar'])!.value, DATE_TIME_FORMAT)
        : undefined,
      fechaFinalizada: this.editForm.get(['fechaFinalizada'])!.value
        ? dayjs(this.editForm.get(['fechaFinalizada'])!.value, DATE_TIME_FORMAT)
        : undefined,
      calificacion: this.editForm.get(['calificacion'])!.value,
      acceso: this.editForm.get(['acceso'])!.value,
      contrasenna: this.editForm.get(['contrasenna'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      categoria: this.editForm.get(['categoria'])!.value,
      usuarioExtra: this.editForm.get(['usuarioExtra'])!.value,
    };
  }
}

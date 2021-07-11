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
import { IUsuarioExtra, UsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';
import { EstadoEncuesta } from 'app/entities/enumerations/estado-encuesta.model';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

import * as $ from 'jquery';

@Component({
  selector: 'jhi-encuesta',
  templateUrl: './encuesta.component.html',
})
export class EncuestaComponent implements OnInit {
  account: Account | null = null;
  usuarioExtra: UsuarioExtra | null = null;

  encuestas?: IEncuesta[];
  isLoading = false;

  isSaving = false;

  categoriasSharedCollection: ICategoria[] = [];
  usuarioExtrasSharedCollection: IUsuarioExtra[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    descripcion: [],
    // fechaCreacion: [null, [Validators.required]],
    // fechaPublicacion: [],
    // fechaFinalizar: [],
    // fechaFinalizada: [],
    // calificacion: [null, [Validators.required]],
    acceso: [null, [Validators.required]],
    // contrasenna: [],
    // estado: [null, [Validators.required]],
    categoria: [null, [Validators.required]],
    // usuarioExtra: [],
  });

  constructor(
    protected encuestaService: EncuestaService,
    protected modalService: NgbModal,
    protected categoriaService: CategoriaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected accountService: AccountService
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

    // Get jhi_user and usuario_extra information
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account !== null) {
        this.usuarioExtraService.find(account.id).subscribe(usuarioExtra => {
          this.usuarioExtra = usuarioExtra.body;
          if (this.usuarioExtra !== null) {
            if (this.usuarioExtra.id === undefined) {
              const today = dayjs().startOf('day');
              this.usuarioExtra.fechaNacimiento = today;
            }
          }

          // this.loadRelationshipsOptions();
        });
      }
    });
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

    if (encuesta.id !== undefined) {
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
    // this.previousState();
    //  ($('#crearEncuesta') as any).modal('hide');
    this.encuestas = [];
    this.loadAll();
    $('#cancelBtn').click();
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
    const now = dayjs();

    return {
      ...new Encuesta(),
      id: undefined,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fechaCreacion: dayjs(now, DATE_TIME_FORMAT),
      calificacion: 5,
      acceso: this.editForm.get(['acceso'])!.value,
      contrasenna: undefined,
      estado: EstadoEncuesta.DRAFT,
      categoria: this.editForm.get(['categoria'])!.value,
      usuarioExtra: this.usuarioExtra,
    };
  }
}

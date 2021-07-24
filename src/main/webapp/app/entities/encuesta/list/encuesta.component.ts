import { Component, OnInit, AfterViewInit } from '@angular/core';
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
import { Router } from '@angular/router';

import {
  faShareAlt,
  faLock,
  faUnlock,
  faCalendarAlt,
  faEdit,
  faCopy,
  faFile,
  faTrashAlt,
  faPlus,
  faStar,
  faUpload,
} from '@fortawesome/free-solid-svg-icons';

import * as $ from 'jquery';
import { EncuestaPublishDialogComponent } from '../encuesta-publish-dialog/encuesta-publish-dialog.component';

@Component({
  selector: 'jhi-encuesta',
  templateUrl: './encuesta.component.html',
})
export class EncuestaComponent implements OnInit, AfterViewInit {
  // Icons
  faShareAlt = faShareAlt;
  faLock = faLock;
  faUnlock = faUnlock;
  faCalendarAlt = faCalendarAlt;
  faEdit = faEdit;
  faCopy = faCopy;
  faFile = faFile;
  faTrashAlt = faTrashAlt;
  faPlus = faPlus;
  faStar = faStar;
  faUpload = faUpload;
  isPublished = false;
  successPublished = false;
  account: Account | null = null;
  usuarioExtra: UsuarioExtra | null = null;
  estadoDeleted = EstadoEncuesta.DELETED;

  encuestas?: IEncuesta[];
  isLoading = false;
  selectedSurvey?: IEncuesta | null = null;
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

  createAnother: Boolean = false;
  selectedSurveyId: Number = 0;

  constructor(
    protected encuestaService: EncuestaService,
    protected modalService: NgbModal,
    protected categoriaService: CategoriaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected accountService: AccountService,
    protected router: Router
  ) {}

  resetForm(): void {
    this.editForm.reset();
  }

  loadAll(): void {
    this.isLoading = true;

    this.encuestaService.query().subscribe(
      (res: HttpResponse<IEncuesta[]>) => {
        this.isLoading = false;
        const tmpEncuestas = res.body ?? [];
        if (this.isAdmin()) {
          this.encuestas = tmpEncuestas;
        } else {
          this.encuestas = tmpEncuestas
            .filter(e => e.usuarioExtra?.id === this.usuarioExtra?.id)
            .filter(e => e.estado !== EstadoEncuesta.DELETED);
        }
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    document.body.addEventListener('click', e => {
      document.getElementById('contextmenu')!.classList.add('ds-contextmenu--closed');
      document.getElementById('contextmenu')!.classList.remove('ds-contextmenu--open');
      document.getElementById('contextmenu')!.style.maxHeight = '0%';
      if (e.target) {
        if (!(e.target as HTMLElement).classList.contains('ds-list--entity')) {
          document.querySelectorAll('.ds-list--entity').forEach(e => {
            e.classList.remove('active');
          });
        }
      }
    });

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
          this.loadAll();
          this.loadRelationshipsOptions();
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

  ngAfterViewInit(): void {}

  trackId(_index: number, item: IEncuesta): number {
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

  trackCategoriaById(_index: number, item: ICategoria): number {
    return item.id!;
  }

  trackUsuarioExtraById(_index: number, item: IUsuarioExtra): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEncuesta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  createAnotherChange(event: any) {
    this.createAnother = event.target.checked;
  }

  protected onSaveSuccess(): void {
    // this.previousState();
    //  ($('#crearEncuesta') as any).modal('hide');
    this.resetForm();
    this.encuestas = [];
    this.loadAll();
    if (!this.createAnother) {
      $('#cancelBtn').click();
    }
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

  isAdmin(): boolean {
    return this.accountService.hasAnyAuthority('ROLE_ADMIN');
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  openSurvey(event: any): void {
    const surveyId = event.target.getAttribute('data-id');
    this.router.navigate(['/encuesta', surveyId, 'edit']);
  }

  selectSurvey(event: any): void {
    document.querySelectorAll('.ds-list--entity').forEach(e => {
      e.classList.remove('active');
    });
    if (event.target.classList.contains('ds-list--entity')) {
      event.target.classList.add('active');
    }
  }

  counter(i: number) {
    return new Array(i);
  }

  testMe(something: any) {
    return 5 - something;
  }

  async openContextMenu(event: any): Promise<void> {
    document.querySelectorAll('.ds-list--entity').forEach(e => {
      e.classList.remove('active');
    });

    if (event.type === 'contextmenu') {
      event.preventDefault();

      debugger;

      this.selectedSurveyId = event.target.dataset.id;
      console.log(this.selectedSurveyId);

      debugger;
      let res = await this.encuestaService.find(this.selectedSurveyId).toPromise();
      this.selectedSurvey = res.body;
      this.isPublished = this.selectedSurvey!.estado === 'DRAFT'; // QUE SE LE MUESTRE CUANDO ESTE EN DRAFT

      document.getElementById('contextmenu-create--separator')!.style.display = 'block';
      document.getElementById('contextmenu-edit--separator')!.style.display = 'block';
      document.getElementById('contextmenu-delete--separator')!.style.display = 'block';
      document.getElementById('contextmenu-edit')!.style.display = 'block';
      document.getElementById('contextmenu-duplicate')!.style.display = 'block';

      if (this.isPublished) {
        document.getElementById('contextmenu-publish')!.style.display = 'block'; //cambiar
      }
      document.getElementById('contextmenu-share')!.style.display = 'block';

      if ((event.target as HTMLElement).classList.contains('ds-list')) {
        document.getElementById('contextmenu-edit--separator')!.style.display = 'none';
        document.getElementById('contextmenu-delete--separator')!.style.display = 'none';
      } else if ((event.target as HTMLElement).classList.contains('ds-list--entity')) {
        event.target.classList.add('active');
        document.getElementById('contextmenu-create--separator')!.style.display = 'none';
      }

      document.getElementById('contextmenu')!.style.top = event.layerY + 'px';
      document.getElementById('contextmenu')!.style.left = event.layerX + 'px';
      document.getElementById('contextmenu')!.classList.remove('ds-contextmenu--closed');
      document.getElementById('contextmenu')!.classList.add('ds-contextmenu--open');
      document.getElementById('contextmenu')!.style.maxHeight = '100%';
    }
  }

  publish() {
    debugger;

    const modalRef = this.modalService.open(EncuestaPublishDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.encuesta = this.selectedSurvey;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'published') {
        this.successPublished = true;
        this.loadAll();
      }
    });
  }
}

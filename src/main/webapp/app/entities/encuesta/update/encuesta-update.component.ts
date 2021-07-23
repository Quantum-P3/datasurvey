import { EPreguntaCerrada } from './../../e-pregunta-cerrada/e-pregunta-cerrada.model';
import { EPreguntaCerradaOpcion, IEPreguntaCerradaOpcion } from './../../e-pregunta-cerrada-opcion/e-pregunta-cerrada-opcion.model';
import { EPreguntaAbiertaService } from './../../e-pregunta-abierta/service/e-pregunta-abierta.service';
import { EPreguntaCerradaOpcionService } from './../../e-pregunta-cerrada-opcion/service/e-pregunta-cerrada-opcion.service';
import { AfterViewChecked, Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEncuesta, Encuesta } from '../encuesta.model';
import { EncuestaService } from '../service/encuesta.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IUsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IEPreguntaCerrada } from 'app/entities/e-pregunta-cerrada/e-pregunta-cerrada.model';
import { EPreguntaCerradaService } from 'app/entities/e-pregunta-cerrada/service/e-pregunta-cerrada.service';
import { EPreguntaCerradaDeleteDialogComponent } from 'app/entities/e-pregunta-cerrada/delete/e-pregunta-cerrada-delete-dialog.component';

import { faTimes, faPlus } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-encuesta-update',
  templateUrl: './encuesta-update.component.html',
})
export class EncuestaUpdateComponent implements OnInit, AfterViewChecked {
  faTimes = faTimes;
  faPlus = faPlus;

  isSaving = false;

  categoriasSharedCollection: ICategoria[] = [];
  usuarioExtrasSharedCollection: IUsuarioExtra[] = [];

  // editForm = this.fb.group({
  //   id: [],
  //   nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
  //   descripcion: [],
  //   fechaCreacion: [null, [Validators.required]],
  //   fechaPublicacion: [],
  //   fechaFinalizar: [],
  //   fechaFinalizada: [],
  //   calificacion: [null, [Validators.required]],
  //   acceso: [null, [Validators.required]],
  //   contrasenna: [],
  //   estado: [null, [Validators.required]],
  //   categoria: [],
  //   usuarioExtra: [],
  // });

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    // orden: [null, [Validators.required]],
    // cantidad: [null, [Validators.required]],
    // ePreguntaCerrada: [],
  });

  ePreguntas?: any[];
  ePreguntasOpciones?: any[];
  encuesta: Encuesta | null = null;

  isLoading = false;

  createAnother: Boolean = false;
  selectedQuestionToCreateOption: IEPreguntaCerrada | null = null;

  constructor(
    protected encuestaService: EncuestaService,
    protected categoriaService: CategoriaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected ePreguntaCerradaService: EPreguntaCerradaService,
    protected ePreguntaCerradaOpcionService: EPreguntaCerradaOpcionService,
    protected ePreguntaAbiertaService: EPreguntaAbiertaService
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.encuestaService.findQuestions(this.encuesta?.id!).subscribe(
      (res: any) => {
        this.isLoading = false;
        this.ePreguntas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

    this.encuestaService.findQuestionsOptions(this.encuesta?.id!).subscribe(
      (res: any) => {
        this.isLoading = false;
        this.ePreguntasOpciones = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ encuesta }) => {
      if (encuesta.id === undefined) {
        const today = dayjs().startOf('day');
        encuesta.fechaCreacion = today;
        encuesta.fechaPublicacion = today;
        encuesta.fechaFinalizar = today;
        encuesta.fechaFinalizada = today;
      } else {
        this.encuesta = encuesta;
        this.loadAll();
      }

      // this.updateForm(encuesta);

      // this.loadRelationshipsOptions();
    });
  }

  ngAfterViewChecked(): void {
    this.initListeners();
  }

  trackId(index: number, item: IEPreguntaCerrada): number {
    return item.id!;
  }

  delete(ePreguntaCerrada: IEPreguntaCerrada): void {
    const modalRef = this.modalService.open(EPreguntaCerradaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ePreguntaCerrada = ePreguntaCerrada;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  initListeners(): void {
    const checkboxes = document.getElementsByClassName('ds-survey--checkbox');
    for (let i = 0; i < checkboxes.length; i++) {
      checkboxes[i].addEventListener('click', e => {
        console.log(e);
        if ((e.target as HTMLInputElement).checked) {
          (e.target as HTMLElement).offsetParent!.classList.add('ds-survey--closed-option--active');
        } else {
          (e.target as HTMLElement).offsetParent!.classList.remove('ds-survey--closed-option--active');
        }
      });
    }
  }

  previousState(): void {
    window.history.back();
  }

  publishSurvey(): void {}

  finishSurvey(): void {}

  addOption(event: any): void {}

  resetForm(event: any): void {
    this.editForm.reset();
    if (event !== null) {
      const id = event.target.dataset.id;
      this.ePreguntaCerradaService.find(id).subscribe(e => {
        this.selectedQuestionToCreateOption = e.body;
        console.log(this.selectedQuestionToCreateOption);
      });
    }
  }

  deleteQuestion(event: any) {
    const id = event.target.dataset.id;
    if (event.target.dataset.type) {
      // Delete closed question
      const questionElement = (event.target as HTMLElement).parentElement?.parentElement;
      const optionIdsToDelete: number[] = [];

      // Get options IDs
      questionElement?.childNodes.forEach((e, i) => {
        if (e.nodeName !== 'DIV') return;
        if (i === 0) return;
        if ((e as HTMLElement).dataset.id === undefined) return;
        let optionId = (e as HTMLElement).dataset.id;
        optionIdsToDelete.push(+optionId!);
      });

      // Delete question options
      this.ePreguntaCerradaOpcionService.deleteMany(optionIdsToDelete).subscribe(e => {
        // Delete question
        this.ePreguntaCerradaService.delete(id).subscribe(e => {
          console.log('DELETED CLOSED QUESTION: ' + id);
          this.loadAll();
        });
      });
    } else {
      // Delete open question
      this.ePreguntaAbiertaService.delete(id).subscribe(e => {
        console.log('DELETED OPEN QUESTION: ' + id);
        this.loadAll();
      });
    }
  }

  deleteOption(event: any): void {
    const id = event.target.dataset.optionid;
    this.ePreguntaCerradaOpcionService.delete(id).subscribe(e => {
      this.ePreguntas = [];
      this.ePreguntasOpciones = [];
      this.loadAll();
    });
  }

  save(): void {
    this.isSaving = true;
    const ePreguntaCerradaOpcion = this.createFromForm();
    if (ePreguntaCerradaOpcion.id !== undefined) {
      this.subscribeToSaveResponse(this.ePreguntaCerradaOpcionService.update(ePreguntaCerradaOpcion));
    } else {
      this.subscribeToSaveResponse(
        this.ePreguntaCerradaOpcionService.create(ePreguntaCerradaOpcion, this.selectedQuestionToCreateOption?.id!)
      );
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
    // this.previousState();
    this.resetForm(null);
    this.ePreguntas = [];
    this.ePreguntasOpciones = [];
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

  protected createFromForm(): IEPreguntaCerradaOpcion {
    console.log(this.selectedQuestionToCreateOption);

    return {
      // ...new EPreguntaCerradaOpcion(),
      id: undefined,
      nombre: this.editForm.get(['nombre'])!.value,
      orden: 10,
      cantidad: 0,
      ePreguntaCerrada: this.selectedQuestionToCreateOption,
    };
  }

  createAnotherChange(event: any) {
    this.createAnother = event.target.checked;
  }

  // previousState(): void {
  //   window.history.back();
  // }

  // save(): void {
  //   this.isSaving = true;
  //   const encuesta = this.createFromForm();
  //   if (encuesta.id !== undefined) {
  //     this.subscribeToSaveResponse(this.encuestaService.update(encuesta));
  //   } else {
  //     this.subscribeToSaveResponse(this.encuestaService.create(encuesta));
  //   }
  // }

  // trackCategoriaById(index: number, item: ICategoria): number {
  //   return item.id!;
  // }

  // trackUsuarioExtraById(index: number, item: IUsuarioExtra): number {
  //   return item.id!;
  // }

  // protected subscribeToSaveResponse(result: Observable<HttpResponse<IEncuesta>>): void {
  //   result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
  //     () => this.onSaveSuccess(),
  //     () => this.onSaveError()
  //   );
  // }

  // protected onSaveSuccess(): void {
  //   this.previousState();
  // }

  // protected onSaveError(): void {
  //   // Api for inheritance.
  // }

  // protected onSaveFinalize(): void {
  //   this.isSaving = false;
  // }

  // protected updateForm(encuesta: IEncuesta): void {
  //   this.editForm.patchValue({
  //     id: encuesta.id,
  //     nombre: encuesta.nombre,
  //     descripcion: encuesta.descripcion,
  //     fechaCreacion: encuesta.fechaCreacion ? encuesta.fechaCreacion.format(DATE_TIME_FORMAT) : null,
  //     fechaPublicacion: encuesta.fechaPublicacion ? encuesta.fechaPublicacion.format(DATE_TIME_FORMAT) : null,
  //     fechaFinalizar: encuesta.fechaFinalizar ? encuesta.fechaFinalizar.format(DATE_TIME_FORMAT) : null,
  //     fechaFinalizada: encuesta.fechaFinalizada ? encuesta.fechaFinalizada.format(DATE_TIME_FORMAT) : null,
  //     calificacion: encuesta.calificacion,
  //     acceso: encuesta.acceso,
  //     contrasenna: encuesta.contrasenna,
  //     estado: encuesta.estado,
  //     categoria: encuesta.categoria,
  //     usuarioExtra: encuesta.usuarioExtra,
  //   });

  //   this.categoriasSharedCollection = this.categoriaService.addCategoriaToCollectionIfMissing(
  //     this.categoriasSharedCollection,
  //     encuesta.categoria
  //   );
  //   this.usuarioExtrasSharedCollection = this.usuarioExtraService.addUsuarioExtraToCollectionIfMissing(
  //     this.usuarioExtrasSharedCollection,
  //     encuesta.usuarioExtra
  //   );
  // }

  // protected loadRelationshipsOptions(): void {
  //   this.categoriaService
  //     .query()
  //     .pipe(map((res: HttpResponse<ICategoria[]>) => res.body ?? []))
  //     .pipe(
  //       map((categorias: ICategoria[]) =>
  //         this.categoriaService.addCategoriaToCollectionIfMissing(categorias, this.editForm.get('categoria')!.value)
  //       )
  //     )
  //     .subscribe((categorias: ICategoria[]) => (this.categoriasSharedCollection = categorias));

  //   this.usuarioExtraService
  //     .query()
  //     .pipe(map((res: HttpResponse<IUsuarioExtra[]>) => res.body ?? []))
  //     .pipe(
  //       map((usuarioExtras: IUsuarioExtra[]) =>
  //         this.usuarioExtraService.addUsuarioExtraToCollectionIfMissing(usuarioExtras, this.editForm.get('usuarioExtra')!.value)
  //       )
  //     )
  //     .subscribe((usuarioExtras: IUsuarioExtra[]) => (this.usuarioExtrasSharedCollection = usuarioExtras));
  // }

  // protected createFromForm(): IEncuesta {
  //   return {
  //     ...new Encuesta(),
  //     id: this.editForm.get(['id'])!.value,
  //     nombre: this.editForm.get(['nombre'])!.value,
  //     descripcion: this.editForm.get(['descripcion'])!.value,
  //     fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
  //       ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
  //       : undefined,
  //     fechaPublicacion: this.editForm.get(['fechaPublicacion'])!.value
  //       ? dayjs(this.editForm.get(['fechaPublicacion'])!.value, DATE_TIME_FORMAT)
  //       : undefined,
  //     fechaFinalizar: this.editForm.get(['fechaFinalizar'])!.value
  //       ? dayjs(this.editForm.get(['fechaFinalizar'])!.value, DATE_TIME_FORMAT)
  //       : undefined,
  //     fechaFinalizada: this.editForm.get(['fechaFinalizada'])!.value
  //       ? dayjs(this.editForm.get(['fechaFinalizada'])!.value, DATE_TIME_FORMAT)
  //       : undefined,
  //     calificacion: this.editForm.get(['calificacion'])!.value,
  //     acceso: this.editForm.get(['acceso'])!.value,
  //     contrasenna: this.editForm.get(['contrasenna'])!.value,
  //     estado: this.editForm.get(['estado'])!.value,
  //     categoria: this.editForm.get(['categoria'])!.value,
  //     usuarioExtra: this.editForm.get(['usuarioExtra'])!.value,
  //   };
  // }
}

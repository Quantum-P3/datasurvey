import { EPreguntaAbierta, IEPreguntaAbierta } from './../../e-pregunta-abierta/e-pregunta-abierta.model';
import { EPreguntaCerrada } from './../../e-pregunta-cerrada/e-pregunta-cerrada.model';
import { IEPreguntaCerradaOpcion } from './../../e-pregunta-cerrada-opcion/e-pregunta-cerrada-opcion.model';
import { EPreguntaAbiertaService } from './../../e-pregunta-abierta/service/e-pregunta-abierta.service';
import { EPreguntaCerradaOpcionService } from './../../e-pregunta-cerrada-opcion/service/e-pregunta-cerrada-opcion.service';
import { AfterViewChecked, Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { Encuesta } from '../encuesta.model';
import { EncuestaService } from '../service/encuesta.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IUsuarioExtra, UsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IEPreguntaCerrada } from 'app/entities/e-pregunta-cerrada/e-pregunta-cerrada.model';
import { EPreguntaCerradaService } from 'app/entities/e-pregunta-cerrada/service/e-pregunta-cerrada.service';
import { EPreguntaCerradaDeleteDialogComponent } from 'app/entities/e-pregunta-cerrada/delete/e-pregunta-cerrada-delete-dialog.component';

import { faEye, faPlus, faPollH, faQuestion, faTimes } from '@fortawesome/free-solid-svg-icons';
import { PreguntaCerradaTipo } from 'app/entities/enumerations/pregunta-cerrada-tipo.model';
import { EncuestaDeleteQuestionDialogComponent } from '../encuesta-delete-question-dialog/encuesta-delete-question-dialog.component';
import { EncuestaDeleteOptionDialogComponent } from '../encuesta-delete-option-dialog/encuesta-delete-option-dialog.component';

import { ParametroAplicacionService } from './../../parametro-aplicacion/service/parametro-aplicacion.service';
import { IParametroAplicacion } from './../../parametro-aplicacion/parametro-aplicacion.model';

import { UsuarioEncuestaService } from 'app/entities/usuario-encuesta/service/usuario-encuesta.service';
import { IUsuarioEncuesta, UsuarioEncuesta } from '../../usuario-encuesta/usuario-encuesta.model';
import { RolColaborador } from '../../enumerations/rol-colaborador.model';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { EncuestaFinalizarDialogComponent } from '../encuesta-finalizar-dialog/encuesta-finalizar-dialog.component';
import { EncuestaDeleteColaboratorDialogComponent } from '../encuesta-delete-colaborator-dialog/encuesta-delete-colaborator-dialog.component';
import { IUser } from '../../user/user.model';

import * as $ from 'jquery';
import { UserService } from '../../user/user.service';
import { EstadoColaborador } from '../../enumerations/estado-colaborador.model';

@Component({
  selector: 'jhi-encuesta-update',
  templateUrl: './encuesta-update.component.html',
})
export class EncuestaUpdateComponent implements OnInit, AfterViewChecked {
  faTimes = faTimes;
  faPlus = faPlus;
  faPollH = faPollH;
  faQuestion = faQuestion;
  faEye = faEye;

  isSaving = false;
  isSavingQuestion = false;
  isSavingCollab = false;
  isSavingAddCollab = false;
  finalizada = false;
  public rolSeleccionado: RolColaborador | undefined = undefined;
  categoriasSharedCollection: ICategoria[] = [];
  usuarioExtrasSharedCollection: IUsuarioExtra[] = [];
  usuariosColaboradores: IUsuarioEncuesta[] = [];
  colaborador: IUsuarioEncuesta | null = null;

  account: Account | null = null;
  usuarioExtra: UsuarioExtra | null = null;

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

  editFormQuestion = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    tipo: [PreguntaCerradaTipo.SINGLE],
    opcional: [false],
    tipopregunta: ['CLOSED'],
  });

  editFormUpdateCollab = this.fb.group({
    rol: [null, [Validators.required]],
  });

  editFormAddCollab = this.fb.group({
    email_add: [null, [Validators.required, Validators.email]],
    rol_add: [null, [Validators.required]],
  });

  ePreguntas?: any[];
  ePreguntasOpciones?: any[];
  encuesta: Encuesta | null = null;
  parametrosAplicacion?: IParametroAplicacion | null = null;

  isLoading = false;

  createAnother: Boolean = false;
  createAnotherQuestion: Boolean = false;
  selectedQuestionToCreateOption: IEPreguntaCerrada | null = null;

  userPublicCollab: IUser | null = null;
  usuarioExtraCollab: UsuarioExtra | null = null;
  userCollabNotExist: boolean = false;
  userCollabIsCollab: boolean = false;
  userCollabIsAutor: boolean = false;
  constructor(
    protected encuestaService: EncuestaService,
    protected categoriaService: CategoriaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected ePreguntaCerradaService: EPreguntaCerradaService,
    protected ePreguntaCerradaOpcionService: EPreguntaCerradaOpcionService,
    protected parametroAplicacionService: ParametroAplicacionService,
    protected ePreguntaAbiertaService: EPreguntaAbiertaService,
    protected usuarioEncuestaService: UsuarioEncuestaService,
    protected router: Router,
    protected accountService: AccountService,
    protected userService: UserService
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

    this.usuarioEncuestaService.findCollaborators(this.encuesta?.id!).subscribe(
      (res: any) => {
        this.isLoading = false;
        this.usuariosColaboradores = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  async loadAplicationParameters(): Promise<void> {
    const params = await this.parametroAplicacionService.find(1).toPromise();
    this.parametrosAplicacion = params.body;
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
        this.loadAplicationParameters();
      }

      // this.updateForm(encuesta);

      // this.loadRelationshipsOptions();
    });

    // Get jhi_user and usuario_extra information
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account !== null) {
        this.usuarioExtraService.find(account.id).subscribe(usuarioExtra => {
          this.usuarioExtra = usuarioExtra.body;
        });
      }
    });
  }

  ngAfterViewChecked(): void {
    // this.initListeners();
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

  // initListeners(): void {
  //   const checkboxes = document.getElementsByClassName('ds-survey--checkbox');
  //   for (let i = 0; i < checkboxes.length; i++) {
  //     checkboxes[i].addEventListener('click', e => {
  //       if ((e.target as HTMLInputElement).checked) {
  //         (e.target as HTMLElement).offsetParent!.classList.add('ds-survey--closed-option--active');
  //       } else {
  //         (e.target as HTMLElement).offsetParent!.classList.remove('ds-survey--closed-option--active');
  //       }
  //     });
  //   }
  // }

  previousState(): void {
    window.history.back();
  }

  publishSurvey(): void {}

  finishSurvey(): void {}

  addOption(event: any): void {}

  openPreview() {
    const surveyId = this.encuesta?.id;
    this.router.navigate(['/encuesta', surveyId, 'preview']);
  }

  resetForm(event: any): void {
    this.editForm.reset();
    if (event !== null) {
      const id = event.target.dataset.id;
      this.ePreguntaCerradaService.find(id).subscribe(e => {
        this.selectedQuestionToCreateOption = e.body;
      });
    }
  }

  deleteQuestion(event: any) {
    const modalRef = this.modalService.open(EncuestaDeleteQuestionDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.closed.subscribe(reason => {
      if (reason === 'confirm') {
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
            if (!(e as HTMLElement).classList.contains('can-delete')) return;
            let optionId = (e as HTMLElement).dataset.id;
            optionIdsToDelete.push(+optionId!);
          });

          if (optionIdsToDelete.length === 0) {
            this.ePreguntaCerradaService.delete(id).subscribe(e => {
              this.loadAll();
            });
          } else {
            // Delete question options
            this.ePreguntaCerradaOpcionService.deleteMany(optionIdsToDelete).subscribe(e => {
              // Delete question
              this.ePreguntaCerradaService.delete(id).subscribe(e => {
                this.loadAll();
              });
            });
          }
        } else {
          // Delete open question
          this.ePreguntaAbiertaService.delete(id).subscribe(e => {
            this.loadAll();
          });
        }
      }
    });
  }

  deleteOption(event: any): void {
    const modalRef = this.modalService.open(EncuestaDeleteOptionDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.closed.subscribe(reason => {
      if (reason === 'confirm') {
        const id = event.target.dataset.optionid;
        this.ePreguntaCerradaOpcionService.delete(id).subscribe(e => {
          this.ePreguntas = [];
          this.ePreguntasOpciones = [];
          this.loadAll();
        });
      }
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
    return {
      // ...new EPreguntaCerradaOpcion(),
      id: undefined,
      nombre: this.editForm.get(['nombre'])!.value,
      orden: 10,
      cantidad: 0,
      epreguntaCerrada: this.selectedQuestionToCreateOption,
    };
  }

  createAnotherChange(event: any) {
    this.createAnother = event.target.checked;
  }

  createQuestion(): void {
    const surveyId = this.encuesta?.id;
  }

  protected createFromFormClosedQuestion(): IEPreguntaCerrada {
    return {
      // ...new EPreguntaCerrada(),
      id: undefined,
      nombre: this.editFormQuestion.get(['nombre'])!.value,
      tipo: this.editFormQuestion.get(['tipo'])!.value,
      opcional: this.editFormQuestion.get(['opcional'])!.value,
      orden: 10,
      encuesta: this.encuesta,
    };
  }

  protected createFromFormOpenQuestion(): IEPreguntaAbierta {
    return {
      // ...new EPreguntaAbierta(),
      id: undefined,
      nombre: this.editFormQuestion.get(['nombre'])!.value,
      opcional: this.editFormQuestion.get(['opcional'])!.value,
      orden: 10,
      encuesta: this.encuesta,
    };
  }

  createAnotherQuestionChange(event: any) {
    this.createAnotherQuestion = event.target.checked;
  }

  saveQuestion(): void {
    this.isSavingQuestion = true;
    const tipoPregunta = this.editFormQuestion.get(['tipopregunta'])!.value;

    if (tipoPregunta === 'CLOSED') {
      const ePreguntaCerrada = this.createFromFormClosedQuestion();
      if (ePreguntaCerrada.id !== undefined) {
        this.subscribeToSaveResponseQuestionClosed(this.ePreguntaCerradaService.update(ePreguntaCerrada));
      } else {
        this.subscribeToSaveResponseQuestionClosed(this.ePreguntaCerradaService.create(ePreguntaCerrada));
      }
    } else if (tipoPregunta === 'OPEN') {
      const ePreguntaAbierta = this.createFromFormOpenQuestion();
      if (ePreguntaAbierta.id !== undefined) {
        this.subscribeToSaveResponseQuestionOpen(this.ePreguntaAbiertaService.update(ePreguntaAbierta));
      } else {
        this.subscribeToSaveResponseQuestionOpen(this.ePreguntaAbiertaService.create(ePreguntaAbierta));
      }
    }
  }

  protected subscribeToSaveResponseQuestionClosed(result: Observable<HttpResponse<IEPreguntaCerrada>>): void {
    result.pipe(finalize(() => this.onSaveFinalizeQuestion())).subscribe(
      () => this.onSaveSuccessQuestion(),
      () => this.onSaveErrorQuestion()
    );
  }

  protected subscribeToSaveResponseQuestionOpen(result: Observable<HttpResponse<IEPreguntaAbierta>>): void {
    result.pipe(finalize(() => this.onSaveFinalizeQuestion())).subscribe(
      () => this.onSaveSuccessQuestion(),
      () => this.onSaveErrorQuestion()
    );
  }

  protected onSaveSuccessQuestion(): void {
    this.editFormQuestion.reset({ tipo: PreguntaCerradaTipo.SINGLE, tipopregunta: 'CLOSED', opcional: false });
    this.editForm.reset();
    this.ePreguntas = [];
    this.ePreguntasOpciones = [];
    this.loadAll();
    if (!this.createAnotherQuestion) {
      $('#cancelBtnQuestion').click();
    }
  }

  protected onSaveErrorQuestion(): void {
    // Api for inheritance.
  }

  protected onSaveFinalizeQuestion(): void {
    this.isSavingQuestion = false;
  }

  updateSurveyName(event: any) {
    const updatedSurveyName = event.target.innerText;
    if (updatedSurveyName !== this.encuesta?.nombre) {
      const survey = { ...this.encuesta };
      survey.nombre = updatedSurveyName;
      // Prevent user update by setting to null
      survey.usuarioExtra!.user = null;

      this.encuestaService.updateSurvey(survey).subscribe(res => {});
    }
  }

  updateQuestionName(event: any): void {
    const questionType = event.target.dataset.tipo;
    const questionId = event.target.dataset.id;
    const questionName = event.target.innerText;
    if (questionType) {
      // Closed question
      this.ePreguntaCerradaService.find(questionId).subscribe(res => {
        const ePreguntaCerrada: EPreguntaCerrada | null = res.body ?? null;
        const updatedEPreguntaCerrada = { ...ePreguntaCerrada };
        if (questionName !== ePreguntaCerrada?.nombre && ePreguntaCerrada !== null) {
          updatedEPreguntaCerrada.nombre = questionName;
          this.ePreguntaCerradaService.update(updatedEPreguntaCerrada).subscribe(updatedQuestion => {
            console.log(updatedQuestion);
          });
        }
      });
    } else {
      // Open question
      // Closed question
      this.ePreguntaAbiertaService.find(questionId).subscribe(res => {
        const ePreguntaAbierta: EPreguntaAbierta | null = res.body ?? null;
        const updatedEPreguntaAbierta = { ...ePreguntaAbierta };
        if (questionName !== ePreguntaAbierta?.nombre && ePreguntaAbierta !== null) {
          updatedEPreguntaAbierta.nombre = questionName;
          this.ePreguntaAbiertaService.update(updatedEPreguntaAbierta).subscribe(updatedQuestion => {
            console.log(updatedQuestion);
          });
        }
      });
    }
    // const questionId = event.target.dataset.id;
    // const survey = { ...this.encuesta };
    // survey.nombre = updatedQuestionName;
    // // Prevent user update by setting to null
    // survey.usuarioExtra!.user = null;

    // this.encuestaService.updateSurvey(survey).subscribe(res => {});
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

  trackCategoriaById(index: number, item: ICategoria): number {
    return item.id!;
  }

  trackUsuarioExtraById(index: number, item: IUsuarioExtra): number {
    return item.id!;
  }

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

  /* methods for colaborators*/
  protected createFromFormCollab(): UsuarioEncuesta {
    return {
      id: undefined,
      rol: this.editFormAddCollab.get(['rol_add'])!.value,
    };
  }

  selectColaborator(c: IUsuarioEncuesta) {
    this.colaborador = c;
    this.rolSeleccionado = c.rol;
  }

  saveCollab(): void {
    this.isSavingCollab = true;
    const collab = this.colaborador;
    if (collab !== null) {
      collab.rol = this.editFormUpdateCollab.get('rol')!.value;
      collab.fechaAgregado = dayjs(this.colaborador?.fechaAgregado, DATE_TIME_FORMAT);
      /*this.usuarioEncuestaService.update(collab).subscribe(
        res => {},
      (error) => {console.log(error)}
      );*/

      this.subscribeToSaveResponseUpdateCollab(this.usuarioEncuestaService.update(collab));
    }
  }

  resetFormAddCollab(): void {
    this.editFormAddCollab.reset();
    this.userPublicCollab = null;
  }

  saveAddCollab(): void {
    this.isSavingAddCollab = true;
    this.userCollabIsAutor = false;
    this.userCollabIsCollab = false;
    this.userCollabNotExist = false;

    const collab = this.createFromFormCollab();
    let rol = this.editFormAddCollab.get('rol_add')!.value;
    if (rol === 'READ') {
      collab.rol = RolColaborador.READ;
    } else if (rol === 'WRITE') {
      collab.rol = RolColaborador.WRITE;
    }
    let correoCollab = this.editFormAddCollab.get('email_add')!.value;

    this.userService
      .retrieveAllPublicUsers()
      .pipe(
        finalize(() => {
          if (this.userPublicCollab?.id !== undefined) {
            if (correoCollab === this.usuarioExtra?.user?.login) {
              this.userCollabIsAutor = true;
              this.isSavingAddCollab = false;
            } else if (this.validarUserIsCollab(correoCollab)) {
              this.userCollabIsCollab = true;
              this.isSavingAddCollab = false;
            } else {
              this.usuarioExtraService.find(this.userPublicCollab?.id).subscribe(res => {
                this.usuarioExtraCollab = res.body;
                let now = new Date();
                collab.fechaAgregado = dayjs(now);
                collab.usuarioExtra = this.usuarioExtraCollab;
                collab.estado = EstadoColaborador.PENDING;
                collab.encuesta = this.encuesta;
                let id = 0;
                this.subscribeToSaveResponseAddCollab(this.usuarioEncuestaService.create(collab));
              });
            }
          } else {
            this.userCollabNotExist = true;
            this.isSavingAddCollab = false;
          }
          this.resetFormAddCollab();
        })
      )
      .subscribe(res => {
        res.forEach(user => {
          if (user.login === correoCollab) {
            this.userPublicCollab = user;
          }
          if (user.id === this.usuarioExtra?.id) {
            // @ts-ignore
            this.usuarioExtra?.user?.login = user.login;
          }
        });
      });
  }

  protected subscribeToSaveResponseUpdateCollab(result: Observable<HttpResponse<IUsuarioEncuesta>>): void {
    result.pipe(finalize(() => this.onSaveFinalizeUpdateCollab())).subscribe(
      () => this.onSaveSuccessUpdateCollab(),
      () => this.onSaveErrorUpdateCollab()
    );
  }

  protected onSaveSuccessUpdateCollab(): void {
    this.loadAll();
    $('#btnCancelUbdateColaboradores').click();
  }

  protected onSaveErrorUpdateCollab(): void {
    // Api for inheritance.
  }

  protected onSaveFinalizeUpdateCollab(): void {
    this.isSavingCollab = false;
  }

  protected subscribeToSaveResponseAddCollab(result: Observable<HttpResponse<IUsuarioEncuesta>>): void {
    result.pipe(finalize(() => this.onSaveFinalizeAddCollab())).subscribe(
      () => this.onSaveSuccessAddCollab(),
      () => this.onSaveErrorAddCollab()
    );
  }

  protected onSaveSuccessAddCollab(): void {
    this.loadAll();
    $('#btnCancelAddColaboradores').click();
  }

  protected onSaveErrorAddCollab(): void {
    // Api for inheritance.
  }

  protected onSaveFinalizeAddCollab(): void {
    this.isSavingAddCollab = false;
  }
  deleteCollab(collab: IUsuarioEncuesta) {
    //$('#btnCancelUbdateColaboradores').click();
    //setTimeout(() => {
    const modalRef = this.modalService.open(EncuestaDeleteColaboratorDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.colaborador = collab;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        $('#btnCancelUbdateColaboradores').click();
        this.loadAll();
      }
    });
    //}, 500);
  }

  isAutor() {
    return this.usuarioExtra?.id === this.encuesta?.usuarioExtra?.id;
  }

  isEscritor() {
    let escritor = false;
    this.usuariosColaboradores.forEach(c => {
      if (this.usuarioExtra?.id === c.usuarioExtra?.id) {
        if (c.rol === 'WRITE') {
          escritor = true;
        }
      }
    });
    return escritor;
  }

  validarUserIsCollab(correoCollab: string) {
    let isCollab = false;
    this.usuariosColaboradores.forEach(c => {
      if (c.usuarioExtra?.id === this.userPublicCollab?.id) {
        isCollab = true;
      }
    });
    return isCollab;
  }

  finalizar(): void {
    const modalRef = this.modalService.open(EncuestaFinalizarDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.encuesta = this.encuesta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'finalized') {
        this.finalizada = true;
        this.loadAll();
      }
    });
  }
}

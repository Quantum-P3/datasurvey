import { PPreguntaAbierta, IPPreguntaAbierta } from './../../p-pregunta-abierta/p-pregunta-abierta.model';
import { PPreguntaCerrada } from './../../p-pregunta-cerrada/p-pregunta-cerrada.model';
import { PPreguntaCerradaOpcion, IPPreguntaCerradaOpcion } from './../../p-pregunta-cerrada-opcion/p-pregunta-cerrada-opcion.model';
import { PPreguntaAbiertaService } from './../../p-pregunta-abierta/service/p-pregunta-abierta.service';
import { PPreguntaCerradaOpcionService } from './../../p-pregunta-cerrada-opcion/service/p-pregunta-cerrada-opcion.service';
import { AfterViewChecked, Component, OnInit } from '@angular/core';
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
import { IUsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IPPreguntaCerrada } from 'app/entities/p-pregunta-cerrada/p-pregunta-cerrada.model';
import { PPreguntaCerradaService } from 'app/entities/p-pregunta-cerrada/service/p-pregunta-cerrada.service';
import { PPreguntaCerradaDeleteDialogComponent } from 'app/entities/p-pregunta-cerrada/delete/p-pregunta-cerrada-delete-dialog.component';

import { faTimes, faPlus, faQuestion, faPollH, faEye, faStore } from '@fortawesome/free-solid-svg-icons';
import { PreguntaCerradaTipo } from 'app/entities/enumerations/pregunta-cerrada-tipo.model';
import { PlantillaDeleteQuestionDialogComponent } from '../plantilla-delete-question-dialog/plantilla-delete-question-dialog.component';
import { PlantillaDeleteOptionDialogComponent } from '../plantilla-delete-option-dialog/plantilla-delete-option-dialog.component';

import { ParametroAplicacionService } from './../../parametro-aplicacion/service/parametro-aplicacion.service';
import { IParametroAplicacion } from './../../parametro-aplicacion/parametro-aplicacion.model';
import { Router } from '@angular/router';
import { EstadoPlantilla } from 'app/entities/enumerations/estado-plantilla.model';
import { PlantillaDeleteStoreDialogComponent } from '../plantilla-delete-store-dialog/plantilla-delete-store-dialog.component';
import { PlantillaPublishStoreDialogComponent } from '../plantilla-publish-store-dialog/plantilla-publish-store-dialog.component';

@Component({
  selector: 'jhi-plantilla-update',
  templateUrl: './plantilla-update.component.html',
})
export class PlantillaUpdateComponent implements OnInit, AfterViewChecked {
  faTimes = faTimes;
  faPlus = faPlus;
  faPollH = faPollH;
  faQuestion = faQuestion;
  faEye = faEye;
  faStore = faStore;

  isSaving = false;
  isSavingQuestion = false;

  categoriasSharedCollection: ICategoria[] = [];
  usuarioExtrasSharedCollection: IUsuarioExtra[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
  });

  editFormQuestion = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.minLength(1), Validators.maxLength(500)]],
    tipo: [PreguntaCerradaTipo.SINGLE],
    opcional: [false],
    tipopregunta: ['CLOSED'],
  });

  pPreguntas?: any[];
  pPreguntasOpciones?: any[];
  plantilla: Plantilla | null = null;
  parametrosAplicacion?: IParametroAplicacion | null = null;

  isLoading = false;

  createAnother: Boolean = false;
  createAnotherQuestion: Boolean = false;
  selectedQuestionToCreateOption: IPPreguntaCerrada | null = null;

  constructor(
    protected plantillaService: PlantillaService,
    protected categoriaService: CategoriaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected pPreguntaCerradaService: PPreguntaCerradaService,
    protected pPreguntaCerradaOpcionService: PPreguntaCerradaOpcionService,
    protected parametroAplicacionService: ParametroAplicacionService,
    protected pPreguntaAbiertaService: PPreguntaAbiertaService,
    protected router: Router
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.plantillaService.findQuestions(this.plantilla?.id!).subscribe(
      (res: any) => {
        this.isLoading = false;
        this.pPreguntas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );

    this.plantillaService.findQuestionsOptions(this.plantilla?.id!).subscribe(
      (res: any) => {
        this.isLoading = false;
        this.pPreguntasOpciones = res.body ?? [];
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
    this.activatedRoute.data.subscribe(({ plantilla }) => {
      if (plantilla.id === undefined) {
        const today = dayjs().startOf('day');
        plantilla.fechaCreacion = today;
        plantilla.fechaPublicacion = today;
        plantilla.fechaFinalizar = today;
        plantilla.fechaFinalizada = today;
      } else {
        this.plantilla = plantilla;
        this.loadAll();
        this.loadAplicationParameters();
      }

      // this.updateForm(plantilla);

      // this.loadRelationshipsOptions();
    });
  }

  ngAfterViewChecked(): void {
    // this.initListeners();
  }

  trackId(index: number, item: IPPreguntaCerrada): number {
    return item.id!;
  }

  delete(pPreguntaCerrada: IPPreguntaCerrada): void {
    const modalRef = this.modalService.open(PPreguntaCerradaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pPreguntaCerrada = pPreguntaCerrada;
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
    const surveyId = this.plantilla?.id;
    this.router.navigate(['/plantilla', surveyId, 'preview']);
  }

  resetForm(event: any): void {
    this.editForm.reset();
    if (event !== null) {
      const id = event.target.dataset.id;
      this.pPreguntaCerradaService.find(id).subscribe(e => {
        this.selectedQuestionToCreateOption = e.body;
      });
    }
  }

  deleteQuestion(event: any) {
    const modalRef = this.modalService.open(PlantillaDeleteQuestionDialogComponent, { size: 'lg', backdrop: 'static' });
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
            this.pPreguntaCerradaService.delete(id).subscribe(e => {
              this.loadAll();
            });
          } else {
            // Delete question options
            this.pPreguntaCerradaOpcionService.deleteMany(optionIdsToDelete).subscribe(e => {
              // Delete question
              this.pPreguntaCerradaService.delete(id).subscribe(e => {
                this.loadAll();
              });
            });
          }
        } else {
          // Delete open question
          this.pPreguntaAbiertaService.delete(id).subscribe(e => {
            this.loadAll();
          });
        }
      }
    });
  }

  deleteOption(event: any): void {
    const modalRef = this.modalService.open(PlantillaDeleteOptionDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.closed.subscribe(reason => {
      if (reason === 'confirm') {
        const id = event.target.dataset.optionid;
        this.pPreguntaCerradaOpcionService.delete(id).subscribe(e => {
          this.pPreguntas = [];
          this.pPreguntasOpciones = [];
          this.loadAll();
        });
      }
    });
  }

  save(): void {
    this.isSaving = true;
    const pPreguntaCerradaOpcion = this.createFromForm();
    if (pPreguntaCerradaOpcion.id !== undefined) {
      this.subscribeToSaveResponse(this.pPreguntaCerradaOpcionService.update(pPreguntaCerradaOpcion));
    } else {
      this.subscribeToSaveResponse(
        this.pPreguntaCerradaOpcionService.create(pPreguntaCerradaOpcion, this.selectedQuestionToCreateOption?.id!)
      );
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
    // this.previousState();
    this.resetForm(null);
    this.pPreguntas = [];
    this.pPreguntasOpciones = [];
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

  protected createFromForm(): IPPreguntaCerradaOpcion {
    return {
      ...new PPreguntaCerradaOpcion(),
      id: undefined,
      nombre: this.editForm.get(['nombre'])!.value,
      orden: 10,
      pPreguntaCerrada: this.selectedQuestionToCreateOption,
    };
  }

  createAnotherChange(event: any) {
    this.createAnother = event.target.checked;
  }

  createQuestion(): void {
    const surveyId = this.plantilla?.id;
  }

  protected createFromFormClosedQuestion(): IPPreguntaCerrada {
    return {
      // ...new PPreguntaCerrada(),
      id: undefined,
      nombre: this.editFormQuestion.get(['nombre'])!.value,
      tipo: this.editFormQuestion.get(['tipo'])!.value,
      opcional: this.editFormQuestion.get(['opcional'])!.value,
      orden: 10,
      plantilla: this.plantilla,
    };
  }

  protected createFromFormOpenQuestion(): IPPreguntaAbierta {
    return {
      // ...new PPreguntaAbierta(),
      id: undefined,
      nombre: this.editFormQuestion.get(['nombre'])!.value,
      opcional: this.editFormQuestion.get(['opcional'])!.value,
      orden: 10,
      plantilla: this.plantilla,
    };
  }

  createAnotherQuestionChange(event: any) {
    this.createAnotherQuestion = event.target.checked;
  }

  saveQuestion(): void {
    this.isSavingQuestion = true;
    const tipoPregunta = this.editFormQuestion.get(['tipopregunta'])!.value;

    if (tipoPregunta === 'CLOSED') {
      const pPreguntaCerrada = this.createFromFormClosedQuestion();
      if (pPreguntaCerrada.id !== undefined) {
        this.subscribeToSaveResponseQuestionClosed(this.pPreguntaCerradaService.update(pPreguntaCerrada));
      } else {
        this.subscribeToSaveResponseQuestionClosed(this.pPreguntaCerradaService.create(pPreguntaCerrada));
      }
    } else if (tipoPregunta === 'OPEN') {
      const pPreguntaAbierta = this.createFromFormOpenQuestion();
      if (pPreguntaAbierta.id !== undefined) {
        this.subscribeToSaveResponseQuestionOpen(this.pPreguntaAbiertaService.update(pPreguntaAbierta));
      } else {
        this.subscribeToSaveResponseQuestionOpen(this.pPreguntaAbiertaService.create(pPreguntaAbierta));
      }
    }
  }

  protected subscribeToSaveResponseQuestionClosed(result: Observable<HttpResponse<IPPreguntaCerrada>>): void {
    result.pipe(finalize(() => this.onSaveFinalizeQuestion())).subscribe(
      () => this.onSaveSuccessQuestion(),
      () => this.onSaveErrorQuestion()
    );
  }

  protected subscribeToSaveResponseQuestionOpen(result: Observable<HttpResponse<IPPreguntaAbierta>>): void {
    result.pipe(finalize(() => this.onSaveFinalizeQuestion())).subscribe(
      () => this.onSaveSuccessQuestion(),
      () => this.onSaveErrorQuestion()
    );
  }

  protected onSaveSuccessQuestion(): void {
    this.editFormQuestion.reset({ tipo: PreguntaCerradaTipo.SINGLE, tipopregunta: 'CLOSED', opcional: false });
    this.editForm.reset();
    this.pPreguntas = [];
    this.pPreguntasOpciones = [];
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

  updateTemplateName(event: any) {
    const updatedSurveyName = event.target.innerText;
    if (updatedSurveyName !== this.plantilla?.nombre) {
      const survey = { ...this.plantilla };
      survey.nombre = updatedSurveyName;

      this.plantillaService.update(survey).subscribe(res => {});
    }
  }

  updateQuestionName(event: any): void {
    const questionType = event.target.dataset.tipo;
    const questionId = event.target.dataset.id;
    const questionName = event.target.innerText;
    if (questionType) {
      // Closed question
      this.pPreguntaCerradaService.find(questionId).subscribe(res => {
        const pPreguntaCerrada: PPreguntaCerrada | null = res.body ?? null;
        const updatedPPreguntaCerrada = { ...pPreguntaCerrada };
        if (questionName !== pPreguntaCerrada?.nombre && pPreguntaCerrada !== null) {
          updatedPPreguntaCerrada.nombre = questionName;
          this.pPreguntaCerradaService.update(updatedPPreguntaCerrada).subscribe(updatedQuestion => {
            console.log(updatedQuestion);
          });
        }
      });
    } else {
      // Open question
      // Closed question
      this.pPreguntaAbiertaService.find(questionId).subscribe(res => {
        const pPreguntaAbierta: PPreguntaAbierta | null = res.body ?? null;
        const updatedPPreguntaAbierta = { ...pPreguntaAbierta };
        if (questionName !== pPreguntaAbierta?.nombre && pPreguntaAbierta !== null) {
          updatedPPreguntaAbierta.nombre = questionName;
          this.pPreguntaAbiertaService.update(updatedPPreguntaAbierta).subscribe(updatedQuestion => {
            console.log(updatedQuestion);
          });
        }
      });
    }
    // const questionId = event.target.dataset.id;
    // const survey = { ...this.plantilla };
    // survey.nombre = updatedQuestionName;
    // // Prevent user update by setting to null
    // survey.usuarioExtra!.user = null;

    // this.plantillaService.updateSurvey(survey).subscribe(res => {});
  }

  trackCategoriaById(index: number, item: ICategoria): number {
    return item.id!;
  }

  trackUsuarioExtraById(index: number, item: IUsuarioExtra): number {
    return item.id!;
  }

  publishTemplateToStore(): void {
    const modalRef = this.modalService.open(PlantillaPublishStoreDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.closed.subscribe(reason => {
      if (reason === 'confirm') {
        this.plantilla!.estado = EstadoPlantilla.ACTIVE;
        this.plantillaService.update(this.plantilla!).subscribe(res => {});
      }
    });
  }

  deleteTemplateFromStore(): void {
    const modalRef = this.modalService.open(PlantillaDeleteStoreDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.closed.subscribe(reason => {
      if (reason === 'confirm') {
        this.plantilla!.estado = EstadoPlantilla.DRAFT;
        this.plantillaService.update(this.plantilla!).subscribe(res => {});
      }
    });
  }
}

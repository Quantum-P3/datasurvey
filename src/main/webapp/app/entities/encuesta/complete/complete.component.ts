import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { IEncuesta } from '../encuesta.model';
import { EncuestaService } from '../service/encuesta.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { IUsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IEPreguntaCerrada } from 'app/entities/e-pregunta-cerrada/e-pregunta-cerrada.model';
import { EPreguntaCerradaService } from 'app/entities/e-pregunta-cerrada/service/e-pregunta-cerrada.service';
import { EPreguntaAbiertaService } from '../../e-pregunta-abierta/service/e-pregunta-abierta.service';
import { EPreguntaAbiertaRespuestaService } from '../../e-pregunta-abierta-respuesta/service/e-pregunta-abierta-respuesta.service';
import { EPreguntaCerradaOpcionService } from '../../e-pregunta-cerrada-opcion/service/e-pregunta-cerrada-opcion.service';
import { faStar, faQuestion } from '@fortawesome/free-solid-svg-icons';
import { AccesoEncuesta } from 'app/entities/enumerations/acceso-encuesta.model';
import { EncuestaPasswordDialogComponent } from '../encuesta-password-dialog/encuesta-password-dialog.component';
import { PreguntaCerradaTipo } from 'app/entities/enumerations/pregunta-cerrada-tipo.model';
import { EPreguntaAbiertaRespuesta } from 'app/entities/e-pregunta-abierta-respuesta/e-pregunta-abierta-respuesta.model';
import { Observable } from 'rxjs/internal/Observable';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-complete',
  templateUrl: './complete.component.html',
})
export class EncuestaCompleteComponent implements OnInit {
  categoriasSharedCollection: ICategoria[] = [];
  usuarioExtrasSharedCollection: IUsuarioExtra[] = [];
  faStar = faStar;
  faQuestion = faQuestion;
  encuesta?: IEncuesta;
  isLoading = false;
  ePreguntas?: any[];
  ePreguntasOpciones?: any[];
  isLocked?: boolean;
  selectedOpenOptions: any;
  selectedSingleOptions: any;
  selectedMultiOptions: any;
  error: boolean;
  calificacion: number;
  stars: number[] = [1, 2, 3, 4, 5];
  cantidadCalificaciones: number = 0;
  avgCalificacion: number = 0;
  sumCalificacion: number = 0;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected encuestaService: EncuestaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected ePreguntaCerradaService: EPreguntaCerradaService,
    protected ePreguntaCerradaOpcionService: EPreguntaCerradaOpcionService,
    protected ePreguntaAbiertaService: EPreguntaAbiertaService,
    protected ePreguntaAbiertaRespuestaService: EPreguntaAbiertaRespuestaService
  ) {
    this.selectedOpenOptions = {};
    this.selectedSingleOptions = {};
    this.selectedMultiOptions = [];
    this.error = false;
    this.calificacion = 0;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ encuesta }) => {
      if (encuesta) {
        this.encuesta = encuesta;
        this.avgCalificacion = parseInt(this.encuesta!.calificacion!.toString().split('.')[0]);
        this.cantidadCalificaciones = parseInt(this.encuesta!.calificacion!.toString().split('.')[1]);
        this.sumCalificacion = this.avgCalificacion * this.cantidadCalificaciones;
      }
      this.isLocked = this.verifyPassword();
      if (this.isLocked) {
        this.previousState();
      } else {
        this.loadAll();
      }
    });
    for (let pregunta of this.ePreguntas!) {
      if (pregunta.tipo && pregunta.tipo === PreguntaCerradaTipo.SINGLE) {
        this.selectedSingleOptions[pregunta.id] = null;
      }
    }
  }

  verifyPassword(): boolean {
    if (this.encuesta!.acceso === AccesoEncuesta.PUBLIC) {
      return false;
    } else {
      const modalRef = this.modalService.open(EncuestaPasswordDialogComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.encuesta = this.encuesta;
      modalRef.closed.subscribe(reason => {
        return reason === 'success';
      });
    }
    return true;
  }

  ngAfterViewChecked(): void {
    this.initListeners();
  }

  initListeners(): void {
    const questions = document.getElementsByClassName('ds-survey--question-wrapper');
    for (let i = 0; i < questions.length; i++) {
      if (questions[i].classList.contains('ds-survey--closed-option')) {
        questions[i].addEventListener('click', e => {
          if ((e.target as HTMLInputElement).checked) {
            (e.target as HTMLElement).offsetParent!.classList.add('ds-survey--closed-option--active');
            (e.target as HTMLElement).id;
          } else {
            (e.target as HTMLElement).offsetParent!.classList.remove('ds-survey--closed-option--active');
          }
        });
      }
    }
  }

  trackId(_index: number, item: IEPreguntaCerrada): number {
    return item.id!;
  }

  trackEPreguntaCerradaById(_index: number, item: IEPreguntaCerrada): number {
    return item.id!;
  }

  trackCategoriaById(_index: number, item: ICategoria): number {
    return item.id!;
  }

  trackUsuarioExtraById(_index: number, item: IUsuarioExtra): number {
    return item.id!;
  }

  loadAll(): void {
    this.isLoading = true;
    this.encuestaService
      .findQuestions(this.encuesta?.id!)
      .pipe(
        finalize(() =>
          this.encuestaService.findQuestionsOptions(this.encuesta?.id!).subscribe(
            (res: any) => {
              this.isLoading = false;
              this.ePreguntasOpciones = res.body ?? [];
            },
            () => {
              this.isLoading = false;
            }
          )
        )
      )
      .subscribe(
        (res: any) => {
          this.isLoading = false;
          this.ePreguntas = res.body ?? [];
        },
        () => {
          this.isLoading = false;
        }
      );
    if (this.ePreguntas!.length == 0) {
      this.previousState();
    }
  }

  previousState(): void {
    window.history.back();
  }

  onCheck(preguntaOpcion: { epreguntaCerrada: any; id: any }): void {
    this.selectedSingleOptions[preguntaOpcion.epreguntaCerrada!.id!] = preguntaOpcion.id;
  }

  toggleOption(ePreguntaOpcionFinal: { id: any }): void {
    if (this.selectedMultiOptions.includes(ePreguntaOpcionFinal.id)) {
      for (let i = 0; i < this.selectedMultiOptions.length; i++) {
        if (this.selectedMultiOptions[i] === ePreguntaOpcionFinal.id) {
          this.selectedMultiOptions.splice(i, 1);
        }
      }
    } else {
      this.selectedMultiOptions.push(ePreguntaOpcionFinal.id);
    }
  }

  finish(): void {
    this.updateEncuestaRating();
    this.getOpenQuestionAnswers();
    this.registerOpenQuestionAnswers();
    this.updateClosedOptionsCount();
  }

  updateEncuestaRating() {
    if (this.calificacion !== 0) {
      const newSumCalificacion = this.sumCalificacion + this.calificacion;
      const newCantidadCalificacion = this.cantidadCalificaciones + 1;
      const newAvgCalificacion = Math.round(newSumCalificacion / newCantidadCalificacion);
      const newRating = Number(this.joinRatingValues(newAvgCalificacion, newCantidadCalificacion));
      this.encuesta!.calificacion = newRating;
      this.encuestaService.updateSurvey(this.encuesta!);
    }
  }

  updateClosedOptionsCount() {
    for (let key in this.selectedSingleOptions) {
      this.ePreguntaCerradaOpcionService.updateCount(this.selectedSingleOptions[key]);
    }
    this.selectedMultiOptions.forEach((option: any) => {
      this.ePreguntaCerradaOpcionService.updateCount(option);
    });
  }

  registerOpenQuestionAnswers() {
    for (let id in this.selectedOpenOptions) {
      let pregunta = this.ePreguntas!.find(p => {
        return p.id == id;
      });
      let newRespuesta = new EPreguntaAbiertaRespuesta(0, this.selectedOpenOptions[id], pregunta);
      this.ePreguntaAbiertaRespuestaService.create(newRespuesta);
    }
  }

  protected onSaveFinalize(): void {
    // this.isSaving = false;
  }

  processError(response: HttpErrorResponse): void {
    if (response.status === 400) {
      this.error = true;
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<any>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.previousState(),
      response => this.processError(response)
    );
  }

  getOpenQuestionAnswers() {
    this.ePreguntas!.forEach(pregunta => {
      if (!pregunta.tipo) {
        let textValue = (document.getElementById(pregunta.id) as HTMLInputElement).value;
        this.selectedOpenOptions[pregunta.id] = textValue;
      }
    });
  }

  joinRatingValues(totalValue: number, ratingCount: number): Number {
    const result = totalValue.toString() + '.' + ratingCount.toString();
    return parseFloat(result);
  }

  updateRating(value: number) {
    this.calificacion = value;
    this.stars.forEach(starNumber => {
      let starElement = document.getElementById(`star-${starNumber}`);
      if (starNumber > this.calificacion!) {
        starElement!.classList.add('entity-icon--star--off');
        starElement!.classList.remove('entity-icon--star');
      } else {
        starElement!.classList.add('entity-icon--star');
        starElement!.classList.remove('entity-icon--star--off');
      }
    });
  }
}

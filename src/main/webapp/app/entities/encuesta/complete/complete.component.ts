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
import { EPreguntaCerradaOpcionService } from '../../e-pregunta-cerrada-opcion/service/e-pregunta-cerrada-opcion.service';
import { faStar, faQuestion } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-complete',
  templateUrl: './complete.component.html',
})
export class EncuestaCompleteComponent implements OnInit {
  categoriasSharedCollection: ICategoria[] = [];
  usuarioExtrasSharedCollection: IUsuarioExtra[] = [];
  faStar = faStar;
  faQuestion = faQuestion;
  encuesta: IEncuesta | null = null;
  isLoading = false;
  ePreguntas?: any[];
  ePreguntasOpciones?: any[];

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected encuestaService: EncuestaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected ePreguntaCerradaService: EPreguntaCerradaService,
    protected ePreguntaCerradaOpcionService: EPreguntaCerradaOpcionService,
    protected ePreguntaAbiertaService: EPreguntaAbiertaService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ encuesta }) => {
      if (encuesta) {
        this.encuesta = encuesta;
        this.loadAll();
      } else {
        this.previousState();
      }
    });
  }

  ngAfterViewChecked(): void {
    this.initListeners();
  }

  initListeners(): void {
    const checkboxes = document.getElementsByClassName('ds-survey--checkbox');
    for (let i = 0; i < checkboxes.length; i++) {
      checkboxes[i].addEventListener('click', e => {
        if ((e.target as HTMLInputElement).checked) {
          (e.target as HTMLElement).offsetParent!.classList.add('ds-survey--closed-option--active');
        } else {
          (e.target as HTMLElement).offsetParent!.classList.remove('ds-survey--closed-option--active');
        }
      });
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
  }

  previousState(): void {
    window.history.back();
  }
}

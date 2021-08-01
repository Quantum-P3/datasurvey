import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EstadoEncuesta } from 'app/entities/enumerations/estado-encuesta.model';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEncuesta, Encuesta } from '../encuesta.model';
import { EncuestaService } from '../service/encuesta.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IUsuarioExtra, UsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IEPreguntaCerrada } from 'app/entities/e-pregunta-cerrada/e-pregunta-cerrada.model';
import { EPreguntaCerradaService } from 'app/entities/e-pregunta-cerrada/service/e-pregunta-cerrada.service';
import { EPreguntaCerradaDeleteDialogComponent } from 'app/entities/e-pregunta-cerrada/delete/e-pregunta-cerrada-delete-dialog.component';
import { IEPreguntaAbierta } from '../../e-pregunta-abierta/e-pregunta-abierta.model';
import { EPreguntaCerrada } from '../../e-pregunta-cerrada/e-pregunta-cerrada.model';
import { EPreguntaCerradaOpcion, IEPreguntaCerradaOpcion } from '../../e-pregunta-cerrada-opcion/e-pregunta-cerrada-opcion.model';
import { EPreguntaAbiertaService } from '../../e-pregunta-abierta/service/e-pregunta-abierta.service';
import { EPreguntaCerradaOpcionService } from '../../e-pregunta-cerrada-opcion/service/e-pregunta-cerrada-opcion.service';
import { PreguntaCerradaTipo } from 'app/entities/enumerations/pregunta-cerrada-tipo.model';

import { faTimes, faPlus, faStar, faQuestion } from '@fortawesome/free-solid-svg-icons';
import { EncuestaPublishDialogComponent } from '../encuesta-publish-dialog/encuesta-publish-dialog.component';
import { UsuarioEncuestaService } from 'app/entities/usuario-encuesta/service/usuario-encuesta.service';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { IUsuarioEncuesta } from '../../usuario-encuesta/usuario-encuesta.model';

@Component({
  selector: 'jhi-encuesta-detail',
  templateUrl: './encuesta-detail.component.html',
})
export class EncuestaDetailComponent implements OnInit {
  categoriasSharedCollection: ICategoria[] = [];
  usuarioExtrasSharedCollection: IUsuarioExtra[] = [];
  faTimes = faTimes;
  faPlus = faPlus;
  faStar = faStar;
  faQuestion = faQuestion;
  encuesta: IEncuesta | null = null;
  isLoading = false;
  successPublished = false;
  ePreguntas?: any[];
  ePreguntasOpciones?: any[];
  usuarioExtra: UsuarioExtra | null = null;
  usuariosColaboradores: IUsuarioEncuesta[] = [];

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected encuestaService: EncuestaService,
    protected categoriaService: CategoriaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected ePreguntaCerradaService: EPreguntaCerradaService,
    protected ePreguntaCerradaOpcionService: EPreguntaCerradaOpcionService,
    protected ePreguntaAbiertaService: EPreguntaAbiertaService,
    protected accountService: AccountService,
    protected usuarioEncuestaService: UsuarioEncuestaService
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

  trackId(index: number, item: IEPreguntaCerrada): number {
    return item.id!;
  }

  trackEPreguntaCerradaById(index: number, item: IEPreguntaCerrada): number {
    return item.id!;
  }

  trackCategoriaById(index: number, item: ICategoria): number {
    return item.id!;
  }

  trackUsuarioExtraById(index: number, item: IUsuarioExtra): number {
    return item.id!;
  }

  getEncuesta(id: number) {
    return this.encuestaService.findEncuesta(id);
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

    /* this.encuestaService.findQuestionsOptions(this.encuesta?.id!).subscribe(
      (res: any) => {
        this.isLoading = false;
        this.ePreguntasOpciones = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );*/

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
  publishSurvey(): void {
    const modalRef = this.modalService.open(EncuestaPublishDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.encuesta = this.encuesta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'published') {
        this.successPublished = true;
        this.loadAll();
      }
    });
  }

  previousState(): void {
    window.history.back();
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
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { EstadoPlantilla } from 'app/entities/enumerations/estado-plantilla.model';

import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPlantilla, Plantilla } from '../plantilla.model';
import { PlantillaService } from '../service/plantilla.service';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IUsuarioExtra, UsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IPPreguntaCerrada } from 'app/entities/p-pregunta-cerrada/p-pregunta-cerrada.model';
import { PPreguntaCerradaService } from 'app/entities/p-pregunta-cerrada/service/p-pregunta-cerrada.service';
import { PPreguntaCerradaDeleteDialogComponent } from 'app/entities/p-pregunta-cerrada/delete/p-pregunta-cerrada-delete-dialog.component';
import { IPPreguntaAbierta } from '../../p-pregunta-abierta/p-pregunta-abierta.model';
import { PPreguntaCerrada } from '../../p-pregunta-cerrada/p-pregunta-cerrada.model';
import { PPreguntaCerradaOpcion, IPPreguntaCerradaOpcion } from '../../p-pregunta-cerrada-opcion/p-pregunta-cerrada-opcion.model';
import { PPreguntaAbiertaService } from '../../p-pregunta-abierta/service/p-pregunta-abierta.service';
import { PPreguntaCerradaOpcionService } from '../../p-pregunta-cerrada-opcion/service/p-pregunta-cerrada-opcion.service';
import { PreguntaCerradaTipo } from 'app/entities/enumerations/pregunta-cerrada-tipo.model';

import { faTimes, faPlus, faStar, faQuestion } from '@fortawesome/free-solid-svg-icons';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-plantilla-detail',
  templateUrl: './plantilla-detail.component.html',
})
export class PlantillaDetailComponent implements OnInit {
  categoriasSharedCollection: ICategoria[] = [];
  usuarioExtrasSharedCollection: IUsuarioExtra[] = [];
  faTimes = faTimes;
  faPlus = faPlus;
  faStar = faStar;
  faQuestion = faQuestion;
  plantilla: IPlantilla | null = null;
  isLoading = false;
  successPublished = false;
  pPreguntas?: any[];
  pPreguntasOpciones?: any[];
  usuarioExtra: UsuarioExtra | null = null;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected plantillaService: PlantillaService,
    protected categoriaService: CategoriaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected pPreguntaCerradaService: PPreguntaCerradaService,
    protected pPreguntaCerradaOpcionService: PPreguntaCerradaOpcionService,
    protected pPreguntaAbiertaService: PPreguntaAbiertaService,
    protected accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plantilla }) => {
      if (plantilla) {
        this.plantilla = plantilla;
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

  trackId(index: number, item: IPPreguntaCerrada): number {
    return item.id!;
  }

  trackPPreguntaCerradaById(index: number, item: IPPreguntaCerrada): number {
    return item.id!;
  }

  trackCategoriaById(index: number, item: ICategoria): number {
    return item.id!;
  }

  trackUsuarioExtraById(index: number, item: IUsuarioExtra): number {
    return item.id!;
  }

  getPlantilla(id: number) {
    return this.plantillaService.findPlantilla(id);
  }

  loadAll(): void {
    this.isLoading = true;

    this.plantillaService
      .findQuestions(this.plantilla?.id!)
      .pipe(
        finalize(() =>
          this.plantillaService.findQuestionsOptions(this.plantilla?.id!).subscribe(
            (res: any) => {
              this.isLoading = false;
              this.pPreguntasOpciones = res.body ?? [];
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
          this.pPreguntas = res.body ?? [];
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

import { Component, OnInit } from '@angular/core';
import { UsuarioExtra } from '../../usuario-extra/usuario-extra.model';
import { IPlantilla } from '../../plantilla/plantilla.model';

import { PlantillaService } from '../../plantilla/service/plantilla.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoriaService } from '../../categoria/service/categoria.service';
import { UsuarioExtraService } from '../../usuario-extra/service/usuario-extra.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { AccountService } from '../../../core/auth/account.service';
import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { Account } from '../../../core/auth/account.model';
import { Encuesta } from './../../encuesta/encuesta.model';
import { EncuestaService } from 'app/entities/encuesta/service/encuesta.service';
import { AccesoEncuesta } from 'app/entities/enumerations/acceso-encuesta.model';
import { EstadoEncuesta } from 'app/entities/enumerations/estado-encuesta.model';

@Component({
  selector: 'jhi-usuario-plantillas',
  templateUrl: './usuario-plantillas.component.html',
  styleUrls: ['./usuario-plantillas.component.scss'],
})
export class UsuarioPlantillasComponent implements OnInit {
  misPlantillas?: IPlantilla[] | null | undefined;
  isLoading = false;
  usuarioExtra: UsuarioExtra | null = null;
  account: Account | null = null;

  constructor(
    protected plantillaService: PlantillaService,
    protected modalService: NgbModal,
    protected categoriaService: CategoriaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected accountService: AccountService,
    protected encuestaService: EncuestaService,
    protected router: Router
  ) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account !== null) {
        this.account = account;
        this.loadAll();
      }
    });
  }

  loadAll() {
    this.isLoading = true;
    // Get jhi_user and usuario_extra information
    if (this.account !== null) {
      this.usuarioExtraService.find(this.account.id).subscribe(usuarioExtra => {
        this.usuarioExtra = usuarioExtra.body;
        this.misPlantillas = usuarioExtra.body?.plantillas;
        this.isLoading = false;
        if (this.usuarioExtra !== null) {
          if (this.usuarioExtra.id === undefined) {
            const today = dayjs().startOf('day');
            this.usuarioExtra.fechaNacimiento = today;
          }
        }
      });
    }
  }

  trackId(index: number, item: IPlantilla): number {
    return item.id!;
  }

  crearEncuesta(plantillaId: any): void {
    const now = dayjs();

    const newSurvey = {
      ...new Encuesta(),
      id: undefined,
      nombre: 'This is a survey',
      descripcion: 'This is a survey',
      fechaCreacion: dayjs(now, DATE_TIME_FORMAT),
      calificacion: 5,
      acceso: AccesoEncuesta.PUBLIC,
      contrasenna: undefined,
      estado: EstadoEncuesta.DRAFT,
      categoria: undefined,
      usuarioExtra: this.usuarioExtra,
    };

    console.log(plantillaId, newSurvey);

    this.encuestaService.createFromTemplate(newSurvey, plantillaId).subscribe(res => {
      this.router.navigate(['/encuesta']);
    });
  }
}

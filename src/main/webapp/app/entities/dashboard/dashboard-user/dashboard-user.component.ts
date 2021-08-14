import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { IEncuesta } from '../../encuesta/encuesta.model';
import { EstadoEncuesta } from '../../enumerations/estado-encuesta.model';
import { EncuestaService } from '../../encuesta/service/encuesta.service';
import { UsuarioExtra } from '../../usuario-extra/usuario-extra.model';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from '../../../core/auth/account.service';
import { UsuarioExtraService } from '../../usuario-extra/service/usuario-extra.service';
import { faListAlt, faUser, faEye, faStar, faCalendarAlt } from '@fortawesome/free-solid-svg-icons';
import * as Chartist from 'chartist';

@Component({
  selector: 'jhi-dashboard-user',
  templateUrl: './dashboard-user.component.html',
  styleUrls: ['./dashboard-user.component.scss'],
})
export class DashboardUserComponent implements OnInit {
  cantEncuestas: number = 0;
  cantPersonas: number = 0;
  cantActivas: number = 0;
  cantFinalizadas: number = 0;
  cantDraft: number = 0;
  cantPublicas: number = 0;
  cantPrivadas: number = 0;
  faListAlt = faListAlt;
  faUser = faUser;
  faEye = faEye;
  faStar = faStar;
  faCalendarAlt = faCalendarAlt;
  reportsGeneral = false;
  reportForEncuestas = true;
  duracion?: number = 0;

  isLoading = false;
  encuestas?: IEncuesta[];
  usuarioExtra: UsuarioExtra | null = null;
  account: Account | null = null;

  constructor(
    protected encuestaService: EncuestaService,
    protected accountService: AccountService,
    protected usuarioExtraService: UsuarioExtraService
  ) {}

  ngOnInit(): void {
    this.loadUser();
  }

  cambiarVista() {
    if (this.reportsGeneral) {
      this.reportsGeneral = false;
      this.reportForEncuestas = true;
    } else if (this.reportForEncuestas) {
      this.reportsGeneral = true;
      this.reportForEncuestas = false;
    }
  }

  loadEncuestas() {
    this.encuestaService.query().subscribe(
      (res: HttpResponse<IEncuesta[]>) => {
        this.isLoading = false;
        const tmpEncuestas = res.body ?? [];

        this.encuestas = tmpEncuestas.filter(e => e.usuarioExtra?.id === this.usuarioExtra?.id);
        this.cantEncuestas = this.encuestas.length;
        this.cantActivas = tmpEncuestas.filter(e => e.estado === 'ACTIVE' && e.usuarioExtra?.id === this.usuarioExtra?.id).length;
        this.cantDraft = tmpEncuestas.filter(e => e.estado === 'DRAFT' && e.usuarioExtra?.id === this.usuarioExtra?.id).length;
        this.cantFinalizadas = tmpEncuestas.filter(e => e.estado === 'FINISHED' && e.usuarioExtra?.id === this.usuarioExtra?.id).length;
        this.cantPublicas = tmpEncuestas.filter(e => e.acceso === 'PUBLIC' && e.usuarioExtra?.id === this.usuarioExtra?.id).length;
        this.cantPrivadas = tmpEncuestas.filter(e => e.acceso === 'PRIVATE' && e.usuarioExtra?.id === this.usuarioExtra?.id).length;

        tmpEncuestas.forEach(encuesta => {
          const _calificacion = encuesta.calificacion;
          encuesta.calificacion = Number(_calificacion?.toString().split('.')[0]);

          debugger;

          if (encuesta.fechaFinalizada == null) {
            this.duracion = -1;
          } else {
            this.duracion = encuesta.fechaPublicacion?.diff(encuesta.fechaFinalizada!, 'days');
          }
        });

        this.cantPersonas = tmpEncuestas.filter(e => e.calificacion && e.usuarioExtra?.id === this.usuarioExtra?.id).length;
        //cantidad de personas que han completado la encuesta

        this.loadFirstChart();
        this.loadSecondChart();
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  loadUser(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account !== null) {
        this.usuarioExtraService.find(account.id).subscribe(usuarioExtra => {
          this.usuarioExtra = usuarioExtra.body;
        });
      }
    });

    this.loadEncuestas();
  }

  loadFirstChart(): void {
    var dataEstado = {
      labels: ['ACTIVOS', 'BORRADOR', 'FINALIZADOS'],
      series: [this.cantActivas, this.cantDraft, this.cantFinalizadas],
    };

    new Chartist.Pie('#chartEstado', dataEstado);
  }

  loadSecondChart(): void {
    var dataAcceso = {
      labels: ['PÚBLICA', 'PRIVADA'],
      series: [this.cantPublicas, this.cantPrivadas],
    };

    new Chartist.Pie('#chartAcceso', dataAcceso);
  }

  detallesPreguntas(): void {}
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { IEncuesta } from '../../encuesta/encuesta.model';
import { EstadoEncuesta } from '../../enumerations/estado-encuesta.model';
import { EncuestaService } from '../../encuesta/service/encuesta.service';
import { UsuarioExtra } from '../../usuario-extra/usuario-extra.model';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from '../../../core/auth/account.service';
import { UsuarioExtraService } from '../../usuario-extra/service/usuario-extra.service';

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

  loadEncuestas() {
    this.encuestaService.query().subscribe(
      (res: HttpResponse<IEncuesta[]>) => {
        this.isLoading = false;
        const tmpEncuestas = res.body ?? [];

        this.encuestas = tmpEncuestas.filter(e => e.usuarioExtra?.id === this.usuarioExtra?.id);
        this.cantEncuestas = this.encuestas.length;
        this.cantActivas = tmpEncuestas.filter(e => e.estado === 'ACTIVE').length;
        this.cantDraft = tmpEncuestas.filter(e => e.estado === 'DRAFT').length;
        this.cantFinalizadas = tmpEncuestas.filter(e => e.estado === 'FINISHED').length;
        this.cantPublicas = tmpEncuestas.filter(e => e.acceso === 'PUBLIC').length;
        this.cantPrivadas = tmpEncuestas.filter(e => e.acceso === 'PRIVATE').length;
        //cantidad de personas que han completado la encuesta
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
}

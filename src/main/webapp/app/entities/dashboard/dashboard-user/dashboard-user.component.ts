import { Component, OnInit } from '@angular/core';
import { IUsuarioEncuesta } from '../../usuario-encuesta/usuario-encuesta.model';
import { IUsuarioExtra } from '../../usuario-extra/usuario-extra.model';
import { IUser } from '../../user/user.model';
import { UsuarioEncuestaService } from '../../usuario-encuesta/service/usuario-encuesta.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UsuarioExtraService } from '../../usuario-extra/service/usuario-extra.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from '../../../core/auth/account.service';
import { HttpResponse } from '@angular/common/http';
import * as dayjs from 'dayjs';

@Component({
  selector: 'jhi-dashboard-user',
  templateUrl: './dashboard-user.component.html',
  styleUrls: ['./dashboard-user.component.scss'],
})
export class DashboardUserComponent implements OnInit {
  usuarioEncuestas?: IUsuarioEncuesta[];
  usuarioExtra: IUsuarioExtra | null = null;
  user: IUser | null = null;

  constructor(
    protected usuarioEncuestaService: UsuarioEncuestaService,
    protected modalService: NgbModal,
    protected usuarioExtraService: UsuarioExtraService,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    protected router: Router
  ) {}

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account !== null) {
        this.usuarioExtraService.find(account.id).subscribe(usuarioExtra => {
          this.usuarioExtra = usuarioExtra.body;
          this.loadAllColaboraciones();
          if (this.usuarioExtra !== null) {
            if (this.usuarioExtra.id === undefined) {
              const today = dayjs().startOf('day');
              this.usuarioExtra.fechaNacimiento = today;
            }
          }
        });
      }
    });
  }

  loadAllColaboraciones(): void {
    this.usuarioEncuestaService.query().subscribe((res: HttpResponse<IUsuarioEncuesta[]>) => {
      const tempUsuarioEncuestas = res.body ?? [];
      this.usuarioEncuestas = tempUsuarioEncuestas
        .filter(c => c.usuarioExtra?.id === this.usuarioExtra?.id)
        .filter(c => c.encuesta?.estado !== 'DELETED');
    });
  }
}

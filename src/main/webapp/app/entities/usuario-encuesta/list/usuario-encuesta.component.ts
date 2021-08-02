import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuarioEncuesta } from '../usuario-encuesta.model';
import { UsuarioEncuestaService } from '../service/usuario-encuesta.service';
import { UsuarioEncuestaDeleteDialogComponent } from '../delete/usuario-encuesta-delete-dialog.component';
import * as dayjs from 'dayjs';
import { faPencilAlt, faPollH } from '@fortawesome/free-solid-svg-icons';

import { AccountService } from 'app/core/auth/account.service';
import { IUsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { IUser } from '../../user/user.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';
import { ActivatedRoute, Router } from '@angular/router';
import { EstadoColaborador } from '../../enumerations/estado-colaborador.model';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import * as $ from 'jquery';
import { DATE_TIME_FORMAT } from '../../../config/input.constants';

@Component({
  selector: 'jhi-usuario-encuesta',
  templateUrl: './usuario-encuesta.component.html',
})
export class UsuarioEncuestaComponent implements OnInit {
  faPollH = faPollH;
  faPencilAlt = faPencilAlt;

  usuarioEncuestas?: IUsuarioEncuesta[];
  isLoading = false;
  usuarioExtra: IUsuarioExtra | null = null;
  user: IUser | null = null;
  isSavingCollab = false;
  public searchRol: string;
  public searchEstado: string;

  constructor(
    protected usuarioEncuestaService: UsuarioEncuestaService,
    protected modalService: NgbModal,
    protected usuarioExtraService: UsuarioExtraService,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    protected router: Router
  ) {
    this.searchRol = '';
    this.searchEstado = '';
  }

  loadAll(): void {
    this.isLoading = true;

    this.usuarioEncuestaService.query().subscribe(
      (res: HttpResponse<IUsuarioEncuesta[]>) => {
        this.isLoading = false;
        const tempUsuarioEncuestas = res.body ?? [];
        this.usuarioEncuestas = tempUsuarioEncuestas
          .filter(c => c.usuarioExtra?.id === this.usuarioExtra?.id)
          .filter(c => c.encuesta?.estado !== 'DELETED');
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.searchRol = '';
    this.searchEstado = '';
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account !== null) {
        this.usuarioExtraService.find(account.id).subscribe(usuarioExtra => {
          this.usuarioExtra = usuarioExtra.body;
          this.loadAll();
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

  trackId(index: number, item: IUsuarioEncuesta): number {
    return item.id!;
  }

  delete(usuarioEncuesta: IUsuarioEncuesta): void {
    const modalRef = this.modalService.open(UsuarioEncuestaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.usuarioEncuesta = usuarioEncuesta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  aceptarInvitacion(usuarioEncuesta: IUsuarioEncuesta) {
    usuarioEncuesta.estado = EstadoColaborador.ACTIVE;
    usuarioEncuesta.fechaAgregado = dayjs(usuarioEncuesta.fechaAgregado, DATE_TIME_FORMAT);
    this.subscribeToSaveResponseCollab(this.usuarioEncuestaService.update(usuarioEncuesta));
  }

  protected subscribeToSaveResponseCollab(result: Observable<HttpResponse<IUsuarioEncuesta>>): void {
    result.pipe(finalize(() => this.onSaveFinalizeCollab())).subscribe(
      () => this.onSaveSuccessCollab(),
      () => this.onSaveErrorCollab()
    );
  }

  protected onSaveSuccessCollab(): void {
    this.loadAll();
  }

  protected onSaveErrorCollab(): void {
    // Api for inheritance.
  }

  protected onSaveFinalizeCollab(): void {
    this.isSavingCollab = false;
  }
}

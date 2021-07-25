import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuarioExtra } from '../usuario-extra.model';
import { UsuarioExtraService } from '../service/usuario-extra.service';
import { UsuarioExtraDeleteDialogComponent } from '../delete/usuario-extra-delete-dialog.component';
import { IUser } from '../../user/user.model';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'jhi-usuario-extra',
  templateUrl: './usuario-extra.component.html',
  styleUrls: ['./usuario-extra.component.scss'],
})
export class UsuarioExtraComponent implements OnInit {
  usuarioExtras?: IUsuarioExtra[];
  publicUsers?: IUser[];
  isLoading = false;
  successChange = false;
  public searchNombreUsuario: string;
  public searchEstadoUsuario: string;

  constructor(protected usuarioExtraService: UsuarioExtraService, protected modalService: NgbModal) {
    this.searchNombreUsuario = '';
    this.searchEstadoUsuario = '';
  }

  loadPublicUser(): void {
    this.usuarioExtraService
      .retrieveAllPublicUsers()
      .pipe(finalize(() => this.loadUserExtras()))
      .subscribe(res => {
        res.forEach(user => {
          let rolList: string[] | undefined;
          rolList = user.authorities;
          let a = rolList?.pop();
          if (a == 'ROLE_ADMIN') {
            user.authorities = ['Admin'];
          } else if (a == 'ROLE_USER') {
            user.authorities = ['Usuario'];
          }
        });
        this.publicUsers = res;
      });
  }

  loadUserExtras() {
    this.usuarioExtraService.query().subscribe(
      (res: HttpResponse<IUsuarioExtra[]>) => {
        this.isLoading = false;
        this.usuarioExtras = res.body ?? [];
        this.usuarioExtras.forEach(uE => {
          uE.user = this.publicUsers?.find(pU => pU.id == uE.user?.id);
        });
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  loadAll(): void {
    this.isLoading = true;
    this.loadPublicUser();
  }

  ngOnInit(): void {
    this.searchNombreUsuario = '';
    this.searchEstadoUsuario = '';
    this.loadAll();
  }

  trackId(index: number, item: IUsuarioExtra): number {
    return item.id!;
  }

  delete(usuarioExtra: IUsuarioExtra): void {
    const modalRef = this.modalService.open(UsuarioExtraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.usuarioExtra = usuarioExtra;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.successChange = true;
        this.loadAll();
      }
    });
  }
}

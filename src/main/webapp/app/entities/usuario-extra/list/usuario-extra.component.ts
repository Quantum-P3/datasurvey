import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuarioExtra } from '../usuario-extra.model';
import { UsuarioExtraService } from '../service/usuario-extra.service';
import { UsuarioExtraDeleteDialogComponent } from '../delete/usuario-extra-delete-dialog.component';

@Component({
  selector: 'jhi-usuario-extra',
  templateUrl: './usuario-extra.component.html',
})
export class UsuarioExtraComponent implements OnInit {
  usuarioExtras?: IUsuarioExtra[];
  isLoading = false;

  constructor(protected usuarioExtraService: UsuarioExtraService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.usuarioExtraService.query().subscribe(
      (res: HttpResponse<IUsuarioExtra[]>) => {
        this.isLoading = false;
        this.usuarioExtras = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
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
        this.loadAll();
      }
    });
  }
}

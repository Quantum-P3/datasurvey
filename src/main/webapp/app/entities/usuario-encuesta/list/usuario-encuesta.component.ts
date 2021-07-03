import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuarioEncuesta } from '../usuario-encuesta.model';
import { UsuarioEncuestaService } from '../service/usuario-encuesta.service';
import { UsuarioEncuestaDeleteDialogComponent } from '../delete/usuario-encuesta-delete-dialog.component';

@Component({
  selector: 'jhi-usuario-encuesta',
  templateUrl: './usuario-encuesta.component.html',
})
export class UsuarioEncuestaComponent implements OnInit {
  usuarioEncuestas?: IUsuarioEncuesta[];
  isLoading = false;

  constructor(protected usuarioEncuestaService: UsuarioEncuestaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.usuarioEncuestaService.query().subscribe(
      (res: HttpResponse<IUsuarioEncuesta[]>) => {
        this.isLoading = false;
        this.usuarioEncuestas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
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
}

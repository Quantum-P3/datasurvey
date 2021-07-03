import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IParametroAplicacion } from '../parametro-aplicacion.model';
import { ParametroAplicacionService } from '../service/parametro-aplicacion.service';
import { ParametroAplicacionDeleteDialogComponent } from '../delete/parametro-aplicacion-delete-dialog.component';

@Component({
  selector: 'jhi-parametro-aplicacion',
  templateUrl: './parametro-aplicacion.component.html',
})
export class ParametroAplicacionComponent implements OnInit {
  parametroAplicacions?: IParametroAplicacion[];
  isLoading = false;

  constructor(protected parametroAplicacionService: ParametroAplicacionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.parametroAplicacionService.query().subscribe(
      (res: HttpResponse<IParametroAplicacion[]>) => {
        this.isLoading = false;
        this.parametroAplicacions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IParametroAplicacion): number {
    return item.id!;
  }

  delete(parametroAplicacion: IParametroAplicacion): void {
    const modalRef = this.modalService.open(ParametroAplicacionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.parametroAplicacion = parametroAplicacion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

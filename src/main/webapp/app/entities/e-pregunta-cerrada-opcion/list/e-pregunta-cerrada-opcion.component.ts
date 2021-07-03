import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEPreguntaCerradaOpcion } from '../e-pregunta-cerrada-opcion.model';
import { EPreguntaCerradaOpcionService } from '../service/e-pregunta-cerrada-opcion.service';
import { EPreguntaCerradaOpcionDeleteDialogComponent } from '../delete/e-pregunta-cerrada-opcion-delete-dialog.component';

@Component({
  selector: 'jhi-e-pregunta-cerrada-opcion',
  templateUrl: './e-pregunta-cerrada-opcion.component.html',
})
export class EPreguntaCerradaOpcionComponent implements OnInit {
  ePreguntaCerradaOpcions?: IEPreguntaCerradaOpcion[];
  isLoading = false;

  constructor(protected ePreguntaCerradaOpcionService: EPreguntaCerradaOpcionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ePreguntaCerradaOpcionService.query().subscribe(
      (res: HttpResponse<IEPreguntaCerradaOpcion[]>) => {
        this.isLoading = false;
        this.ePreguntaCerradaOpcions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEPreguntaCerradaOpcion): number {
    return item.id!;
  }

  delete(ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion): void {
    const modalRef = this.modalService.open(EPreguntaCerradaOpcionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ePreguntaCerradaOpcion = ePreguntaCerradaOpcion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPPreguntaCerradaOpcion } from '../p-pregunta-cerrada-opcion.model';
import { PPreguntaCerradaOpcionService } from '../service/p-pregunta-cerrada-opcion.service';
import { PPreguntaCerradaOpcionDeleteDialogComponent } from '../delete/p-pregunta-cerrada-opcion-delete-dialog.component';

@Component({
  selector: 'jhi-p-pregunta-cerrada-opcion',
  templateUrl: './p-pregunta-cerrada-opcion.component.html',
})
export class PPreguntaCerradaOpcionComponent implements OnInit {
  pPreguntaCerradaOpcions?: IPPreguntaCerradaOpcion[];
  isLoading = false;

  constructor(protected pPreguntaCerradaOpcionService: PPreguntaCerradaOpcionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pPreguntaCerradaOpcionService.query().subscribe(
      (res: HttpResponse<IPPreguntaCerradaOpcion[]>) => {
        this.isLoading = false;
        this.pPreguntaCerradaOpcions = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPPreguntaCerradaOpcion): number {
    return item.id!;
  }

  delete(pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion): void {
    const modalRef = this.modalService.open(PPreguntaCerradaOpcionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pPreguntaCerradaOpcion = pPreguntaCerradaOpcion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

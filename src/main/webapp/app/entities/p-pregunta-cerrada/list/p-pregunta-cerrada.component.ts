import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPPreguntaCerrada } from '../p-pregunta-cerrada.model';
import { PPreguntaCerradaService } from '../service/p-pregunta-cerrada.service';
import { PPreguntaCerradaDeleteDialogComponent } from '../delete/p-pregunta-cerrada-delete-dialog.component';

@Component({
  selector: 'jhi-p-pregunta-cerrada',
  templateUrl: './p-pregunta-cerrada.component.html',
})
export class PPreguntaCerradaComponent implements OnInit {
  pPreguntaCerradas?: IPPreguntaCerrada[];
  isLoading = false;

  constructor(protected pPreguntaCerradaService: PPreguntaCerradaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pPreguntaCerradaService.query().subscribe(
      (res: HttpResponse<IPPreguntaCerrada[]>) => {
        this.isLoading = false;
        this.pPreguntaCerradas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPPreguntaCerrada): number {
    return item.id!;
  }

  delete(pPreguntaCerrada: IPPreguntaCerrada): void {
    const modalRef = this.modalService.open(PPreguntaCerradaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pPreguntaCerrada = pPreguntaCerrada;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

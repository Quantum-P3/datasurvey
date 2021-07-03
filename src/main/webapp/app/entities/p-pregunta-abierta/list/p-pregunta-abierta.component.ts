import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPPreguntaAbierta } from '../p-pregunta-abierta.model';
import { PPreguntaAbiertaService } from '../service/p-pregunta-abierta.service';
import { PPreguntaAbiertaDeleteDialogComponent } from '../delete/p-pregunta-abierta-delete-dialog.component';

@Component({
  selector: 'jhi-p-pregunta-abierta',
  templateUrl: './p-pregunta-abierta.component.html',
})
export class PPreguntaAbiertaComponent implements OnInit {
  pPreguntaAbiertas?: IPPreguntaAbierta[];
  isLoading = false;

  constructor(protected pPreguntaAbiertaService: PPreguntaAbiertaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pPreguntaAbiertaService.query().subscribe(
      (res: HttpResponse<IPPreguntaAbierta[]>) => {
        this.isLoading = false;
        this.pPreguntaAbiertas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPPreguntaAbierta): number {
    return item.id!;
  }

  delete(pPreguntaAbierta: IPPreguntaAbierta): void {
    const modalRef = this.modalService.open(PPreguntaAbiertaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pPreguntaAbierta = pPreguntaAbierta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEPreguntaAbierta } from '../e-pregunta-abierta.model';
import { EPreguntaAbiertaService } from '../service/e-pregunta-abierta.service';
import { EPreguntaAbiertaDeleteDialogComponent } from '../delete/e-pregunta-abierta-delete-dialog.component';

@Component({
  selector: 'jhi-e-pregunta-abierta',
  templateUrl: './e-pregunta-abierta.component.html',
})
export class EPreguntaAbiertaComponent implements OnInit {
  ePreguntaAbiertas?: IEPreguntaAbierta[];
  isLoading = false;

  constructor(protected ePreguntaAbiertaService: EPreguntaAbiertaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ePreguntaAbiertaService.query().subscribe(
      (res: HttpResponse<IEPreguntaAbierta[]>) => {
        this.isLoading = false;
        this.ePreguntaAbiertas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEPreguntaAbierta): number {
    return item.id!;
  }

  delete(ePreguntaAbierta: IEPreguntaAbierta): void {
    const modalRef = this.modalService.open(EPreguntaAbiertaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ePreguntaAbierta = ePreguntaAbierta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

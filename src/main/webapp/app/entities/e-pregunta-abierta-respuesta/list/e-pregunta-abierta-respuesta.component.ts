import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEPreguntaAbiertaRespuesta } from '../e-pregunta-abierta-respuesta.model';
import { EPreguntaAbiertaRespuestaService } from '../service/e-pregunta-abierta-respuesta.service';
import { EPreguntaAbiertaRespuestaDeleteDialogComponent } from '../delete/e-pregunta-abierta-respuesta-delete-dialog.component';

@Component({
  selector: 'jhi-e-pregunta-abierta-respuesta',
  templateUrl: './e-pregunta-abierta-respuesta.component.html',
})
export class EPreguntaAbiertaRespuestaComponent implements OnInit {
  ePreguntaAbiertaRespuestas?: IEPreguntaAbiertaRespuesta[];
  isLoading = false;

  constructor(protected ePreguntaAbiertaRespuestaService: EPreguntaAbiertaRespuestaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ePreguntaAbiertaRespuestaService.query().subscribe(
      (res: HttpResponse<IEPreguntaAbiertaRespuesta[]>) => {
        this.isLoading = false;
        this.ePreguntaAbiertaRespuestas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEPreguntaAbiertaRespuesta): number {
    return item.id!;
  }

  delete(ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta): void {
    const modalRef = this.modalService.open(EPreguntaAbiertaRespuestaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ePreguntaAbiertaRespuesta = ePreguntaAbiertaRespuesta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

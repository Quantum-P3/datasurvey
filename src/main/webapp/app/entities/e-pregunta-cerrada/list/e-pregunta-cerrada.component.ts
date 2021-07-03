import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEPreguntaCerrada } from '../e-pregunta-cerrada.model';
import { EPreguntaCerradaService } from '../service/e-pregunta-cerrada.service';
import { EPreguntaCerradaDeleteDialogComponent } from '../delete/e-pregunta-cerrada-delete-dialog.component';

@Component({
  selector: 'jhi-e-pregunta-cerrada',
  templateUrl: './e-pregunta-cerrada.component.html',
})
export class EPreguntaCerradaComponent implements OnInit {
  ePreguntaCerradas?: IEPreguntaCerrada[];
  isLoading = false;

  constructor(protected ePreguntaCerradaService: EPreguntaCerradaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ePreguntaCerradaService.query().subscribe(
      (res: HttpResponse<IEPreguntaCerrada[]>) => {
        this.isLoading = false;
        this.ePreguntaCerradas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEPreguntaCerrada): number {
    return item.id!;
  }

  delete(ePreguntaCerrada: IEPreguntaCerrada): void {
    const modalRef = this.modalService.open(EPreguntaCerradaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ePreguntaCerrada = ePreguntaCerrada;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

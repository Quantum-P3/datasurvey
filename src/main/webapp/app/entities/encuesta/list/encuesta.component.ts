import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEncuesta } from '../encuesta.model';
import { EncuestaService } from '../service/encuesta.service';
import { EncuestaDeleteDialogComponent } from '../delete/encuesta-delete-dialog.component';

@Component({
  selector: 'jhi-encuesta',
  templateUrl: './encuesta.component.html',
})
export class EncuestaComponent implements OnInit {
  encuestas?: IEncuesta[];
  isLoading = false;

  constructor(protected encuestaService: EncuestaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.encuestaService.query().subscribe(
      (res: HttpResponse<IEncuesta[]>) => {
        this.isLoading = false;
        this.encuestas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEncuesta): number {
    return item.id!;
  }

  delete(encuesta: IEncuesta): void {
    const modalRef = this.modalService.open(EncuestaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.encuesta = encuesta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

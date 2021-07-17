import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EncuestaService } from 'app/entities/encuesta/service/encuesta.service';
import { EstadoCategoria } from 'app/entities/enumerations/estado-categoria.model';

import { ICategoria } from '../categoria.model';
import { CategoriaService } from '../service/categoria.service';

@Component({
  templateUrl: './categoria-delete-dialog.component.html',
})
export class CategoriaDeleteDialogComponent {
  categoria?: ICategoria;

  constructor(
    protected categoriaService: CategoriaService,
    protected activeModal: NgbActiveModal,
    protected encuestaService: EncuestaService
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(categoria: ICategoria): void {
    categoria.estado = EstadoCategoria.INACTIVE;
    this.categoriaService.update(categoria).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

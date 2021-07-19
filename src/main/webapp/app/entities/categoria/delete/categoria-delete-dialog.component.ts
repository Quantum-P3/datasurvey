import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { EncuestaService } from 'app/entities/encuesta/service/encuesta.service';
import { EstadoCategoria } from 'app/entities/enumerations/estado-categoria.model';

import { Categoria, ICategoria } from '../categoria.model';
import { CategoriaService } from '../service/categoria.service';

@Component({
  templateUrl: './categoria-delete-dialog.component.html',
})
export class CategoriaDeleteDialogComponent {
  categoria?: ICategoria;
  encuestas?: IEncuesta[];
  encuestasFiltradas?: IEncuesta[];

  constructor(
    protected categoriaService: CategoriaService,
    protected activeModal: NgbActiveModal,
    protected encuestaService: EncuestaService
  ) {
    this.getEncuestas();
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(categoria: ICategoria): void {
    const categoriaNula = new Categoria(0, 'Otra', EstadoCategoria.ACTIVE);
    this.getEncuestas();
    debugger;
    if (categoria.estado == EstadoCategoria.INACTIVE) {
      categoria.estado = EstadoCategoria.ACTIVE;
    } else {
      this.encuestas!.forEach(encuesta => {
        debugger;
        if (encuesta.categoria != null && encuesta.categoria!.id === categoria.id) {
          encuesta.categoria = categoriaNula;
          this.encuestaService.update(encuesta);
        }
      });
      categoria.estado = EstadoCategoria.INACTIVE;
    }
    this.categoriaService.update(categoria).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }

  getEncuestas(): void {
    this.encuestaService.query().subscribe(res => {
      this.encuestas = res.body ?? [];
    });
  }
}

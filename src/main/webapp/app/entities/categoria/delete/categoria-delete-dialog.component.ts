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
  ensureNulaExists(): void {
    const categoriaNula = new Categoria(0, 'Otra', EstadoCategoria.ACTIVE);
    const categoria = this.categoriaService.find(0);
    if (categoria) {
      this.categoriaService.update(categoriaNula);
    } else {
      this.categoriaService.create(categoriaNula);
    }
  }

  protected getEncuestas(categoria: ICategoria): void {
    this.encuestaService.query().subscribe(res => {
      this.encuestas = res.body ?? [];
    });
    if (this.encuestas) {
      this.encuestasFiltradas = this.encuestas.filter(encuesta => {
        encuesta.categoria!.id === categoria.id;
      });
    }
  }
}

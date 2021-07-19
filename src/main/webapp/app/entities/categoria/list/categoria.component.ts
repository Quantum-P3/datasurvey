import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategoria } from '../categoria.model';
import { CategoriaService } from '../service/categoria.service';
import { CategoriaDeleteDialogComponent } from '../delete/categoria-delete-dialog.component';

@Component({
  selector: 'jhi-categoria',
  templateUrl: './categoria.component.html',
})
export class CategoriaComponent implements OnInit {
  categorias?: ICategoria[];
  isLoading = false;
  public searchString: string;
  success = false;

  constructor(protected categoriaService: CategoriaService, protected modalService: NgbModal) {
    this.searchString = '';
  }

  loadAll(): void {
    this.isLoading = true;

    this.categoriaService.query().subscribe(
      (res: HttpResponse<ICategoria[]>) => {
        this.isLoading = false;
        this.categorias = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.searchString = '';
    this.loadAll();
  }

  trackId(_index: number, item: ICategoria): number {
    return item.id!;
  }

  toggleStatus(categoria: ICategoria): void {
    const modalRef = this.modalService.open(CategoriaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.categoria = categoria;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.success = true;
        this.loadAll();
      }
    });
  }
}

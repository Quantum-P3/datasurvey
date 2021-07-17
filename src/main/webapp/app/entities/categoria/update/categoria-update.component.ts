import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICategoria, Categoria } from '../categoria.model';
import { CategoriaService } from '../service/categoria.service';

@Component({
  selector: 'jhi-categoria-update',
  templateUrl: './categoria-update.component.html',
})
export class CategoriaUpdateComponent implements OnInit {
  isSaving = false;
  public categorias?: ICategoria[];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    estado: [null, [Validators.required]],
  });
  public duplicateName: boolean;

  constructor(protected categoriaService: CategoriaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {
    this.duplicateName = false;
    this.categorias = [];
    this.loadAll();
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoria }) => {
      this.updateForm(categoria);
    });
    this.loadAll();
  }

  loadAll(): void {
    this.categoriaService.query().subscribe(res => {
      this.categorias = res.body ?? [];
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categoria = this.createFromForm();
    const condicion = this.categoryExists(categoria);
    if (!condicion) {
      if (categoria.id !== undefined) {
        this.subscribeToSaveResponse(this.categoriaService.update(categoria));
      } else {
        this.subscribeToSaveResponse(this.categoriaService.create(categoria));
      }
    } else {
      this.duplicateName = true;
      this.isSaving = false;
    }
  }

  protected categoryExists(categoria: ICategoria): boolean {
    this.loadAll();
    var condicion = this.categorias!.some(cat => cat.nombre!.toLowerCase() === categoria.nombre!.toLowerCase() && cat.id !== categoria.id);
    return condicion;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoria>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(categoria: ICategoria): void {
    this.editForm.patchValue({
      id: categoria.id,
      nombre: categoria.nombre,
      estado: categoria.estado,
    });
  }

  protected createFromForm(): ICategoria {
    return {
      ...new Categoria(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}

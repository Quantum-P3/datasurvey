import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFactura, Factura } from '../factura.model';
import { FacturaService } from '../service/factura.service';

@Component({
  selector: 'jhi-factura-update',
  templateUrl: './factura-update.component.html',
})
export class FacturaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombreUsuario: [null, [Validators.required]],
    nombrePlantilla: [null, [Validators.required]],
    costo: [null, [Validators.required]],
    fecha: [null, [Validators.required]],
  });

  constructor(protected facturaService: FacturaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      if (factura.id === undefined) {
        const today = dayjs().startOf('day');
        factura.fecha = today;
      }

      this.updateForm(factura);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factura = this.createFromForm();
    if (factura.id !== undefined) {
      this.subscribeToSaveResponse(this.facturaService.update(factura));
    } else {
      this.subscribeToSaveResponse(this.facturaService.create(factura));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactura>>): void {
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

  protected updateForm(factura: IFactura): void {
    this.editForm.patchValue({
      id: factura.id,
      nombreUsuario: factura.nombreUsuario,
      nombrePlantilla: factura.nombrePlantilla,
      costo: factura.costo,
      fecha: factura.fecha ? factura.fecha.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IFactura {
    return {
      ...new Factura(),
      id: this.editForm.get(['id'])!.value,
      nombreUsuario: this.editForm.get(['nombreUsuario'])!.value,
      nombrePlantilla: this.editForm.get(['nombrePlantilla'])!.value,
      costo: this.editForm.get(['costo'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? dayjs(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}

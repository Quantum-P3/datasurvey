import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUsuarioEncuesta, UsuarioEncuesta } from '../usuario-encuesta.model';
import { UsuarioEncuestaService } from '../service/usuario-encuesta.service';
import { IUsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';
import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { EncuestaService } from 'app/entities/encuesta/service/encuesta.service';

@Component({
  selector: 'jhi-usuario-encuesta-update',
  templateUrl: './usuario-encuesta-update.component.html',
})
export class UsuarioEncuestaUpdateComponent implements OnInit {
  isSaving = false;

  usuarioExtrasSharedCollection: IUsuarioExtra[] = [];
  encuestasSharedCollection: IEncuesta[] = [];

  editForm = this.fb.group({
    id: [],
    rol: [null, [Validators.required]],
    estado: [null, [Validators.required]],
    fechaAgregado: [null, [Validators.required]],
    usuarioExtra: [],
    encuesta: [],
  });

  constructor(
    protected usuarioEncuestaService: UsuarioEncuestaService,
    protected usuarioExtraService: UsuarioExtraService,
    protected encuestaService: EncuestaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioEncuesta }) => {
      if (usuarioEncuesta.id === undefined) {
        const today = dayjs().startOf('day');
        usuarioEncuesta.fechaAgregado = today;
      }

      this.updateForm(usuarioEncuesta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuarioEncuesta = this.createFromForm();
    if (usuarioEncuesta.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioEncuestaService.update(usuarioEncuesta));
    } else {
      this.subscribeToSaveResponse(this.usuarioEncuestaService.create(usuarioEncuesta));
    }
  }

  trackUsuarioExtraById(index: number, item: IUsuarioExtra): number {
    return item.id!;
  }

  trackEncuestaById(index: number, item: IEncuesta): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuarioEncuesta>>): void {
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

  protected updateForm(usuarioEncuesta: IUsuarioEncuesta): void {
    this.editForm.patchValue({
      id: usuarioEncuesta.id,
      rol: usuarioEncuesta.rol,
      estado: usuarioEncuesta.estado,
      fechaAgregado: usuarioEncuesta.fechaAgregado ? usuarioEncuesta.fechaAgregado.format(DATE_TIME_FORMAT) : null,
      usuarioExtra: usuarioEncuesta.usuarioExtra,
      encuesta: usuarioEncuesta.encuesta,
    });

    this.usuarioExtrasSharedCollection = this.usuarioExtraService.addUsuarioExtraToCollectionIfMissing(
      this.usuarioExtrasSharedCollection,
      usuarioEncuesta.usuarioExtra
    );
    this.encuestasSharedCollection = this.encuestaService.addEncuestaToCollectionIfMissing(
      this.encuestasSharedCollection,
      usuarioEncuesta.encuesta
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioExtraService
      .query()
      .pipe(map((res: HttpResponse<IUsuarioExtra[]>) => res.body ?? []))
      .pipe(
        map((usuarioExtras: IUsuarioExtra[]) =>
          this.usuarioExtraService.addUsuarioExtraToCollectionIfMissing(usuarioExtras, this.editForm.get('usuarioExtra')!.value)
        )
      )
      .subscribe((usuarioExtras: IUsuarioExtra[]) => (this.usuarioExtrasSharedCollection = usuarioExtras));

    this.encuestaService
      .query()
      .pipe(map((res: HttpResponse<IEncuesta[]>) => res.body ?? []))
      .pipe(
        map((encuestas: IEncuesta[]) =>
          this.encuestaService.addEncuestaToCollectionIfMissing(encuestas, this.editForm.get('encuesta')!.value)
        )
      )
      .subscribe((encuestas: IEncuesta[]) => (this.encuestasSharedCollection = encuestas));
  }

  protected createFromForm(): IUsuarioEncuesta {
    return {
      ...new UsuarioEncuesta(),
      id: this.editForm.get(['id'])!.value,
      rol: this.editForm.get(['rol'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      fechaAgregado: this.editForm.get(['fechaAgregado'])!.value
        ? dayjs(this.editForm.get(['fechaAgregado'])!.value, DATE_TIME_FORMAT)
        : undefined,
      usuarioExtra: this.editForm.get(['usuarioExtra'])!.value,
      encuesta: this.editForm.get(['encuesta'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUsuarioExtra, UsuarioExtra } from '../usuario-extra.model';
import { UsuarioExtraService } from '../service/usuario-extra.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPlantilla } from 'app/entities/plantilla/plantilla.model';
import { PlantillaService } from 'app/entities/plantilla/service/plantilla.service';

@Component({
  selector: 'jhi-usuario-extra-update',
  templateUrl: './usuario-extra-update.component.html',
})
export class UsuarioExtraUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  plantillasSharedCollection: IPlantilla[] = [];

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required]],
    iconoPerfil: [],
    fechaNacimiento: [],
    estado: [null, [Validators.required]],
    user: [],
    plantillas: [],
  });

  constructor(
    protected usuarioExtraService: UsuarioExtraService,
    protected userService: UserService,
    protected plantillaService: PlantillaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuarioExtra }) => {
      if (usuarioExtra.id === undefined) {
        const today = dayjs().startOf('day');
        usuarioExtra.fechaNacimiento = today;
      }

      this.updateForm(usuarioExtra);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuarioExtra = this.createFromForm();
    if (usuarioExtra.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioExtraService.update(usuarioExtra));
    } else {
      this.subscribeToSaveResponse(this.usuarioExtraService.create(usuarioExtra));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackPlantillaById(index: number, item: IPlantilla): number {
    return item.id!;
  }

  getSelectedPlantilla(option: IPlantilla, selectedVals?: IPlantilla[]): IPlantilla {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuarioExtra>>): void {
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

  protected updateForm(usuarioExtra: IUsuarioExtra): void {
    this.editForm.patchValue({
      id: usuarioExtra.id,
      nombre: usuarioExtra.nombre,
      iconoPerfil: usuarioExtra.iconoPerfil,
      fechaNacimiento: usuarioExtra.fechaNacimiento ? usuarioExtra.fechaNacimiento.format(DATE_TIME_FORMAT) : null,
      estado: usuarioExtra.estado,
      user: usuarioExtra.user,
      plantillas: usuarioExtra.plantillas,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, usuarioExtra.user);
    this.plantillasSharedCollection = this.plantillaService.addPlantillaToCollectionIfMissing(
      this.plantillasSharedCollection,
      ...(usuarioExtra.plantillas ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.plantillaService
      .query()
      .pipe(map((res: HttpResponse<IPlantilla[]>) => res.body ?? []))
      .pipe(
        map((plantillas: IPlantilla[]) =>
          this.plantillaService.addPlantillaToCollectionIfMissing(plantillas, ...(this.editForm.get('plantillas')!.value ?? []))
        )
      )
      .subscribe((plantillas: IPlantilla[]) => (this.plantillasSharedCollection = plantillas));
  }

  protected createFromForm(): IUsuarioExtra {
    return {
      ...new UsuarioExtra(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      iconoPerfil: this.editForm.get(['iconoPerfil'])!.value,
      fechaNacimiento: this.editForm.get(['fechaNacimiento'])!.value
        ? dayjs(this.editForm.get(['fechaNacimiento'])!.value, DATE_TIME_FORMAT)
        : undefined,
      estado: this.editForm.get(['estado'])!.value,
      user: this.editForm.get(['user'])!.value,
      plantillas: this.editForm.get(['plantillas'])!.value,
    };
  }
}

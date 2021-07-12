import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPlantilla } from 'app/entities/plantilla/plantilla.model';
import { PlantillaService } from 'app/entities/plantilla/service/plantilla.service';
import { IUsuarioExtra, UsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html',
})
export class SettingsComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  plantillasSharedCollection: IPlantilla[] = [];

  editForm = this.fb.group({
    email: [null, [Validators.required]],
    id: [],
    nombre: [null, [Validators.required]],
    iconoPerfil: [],
    fechaNacimiento: [],
    estado: [null, [Validators.required]],
    user: [],
    plantillas: [],
  });

  passwordForm = this.fb.group({
    password: [null, [Validators.required]],
    passwordNew: [null, [Validators.required]],
    passwordNewConfirm: [null, [Validators.required]],
  });

  usuarioExtra: UsuarioExtra | null = null;
  profileIcon: number = 1;
  profileIcons: any[] = [
    { name: 'C1' },
    { name: 'C2' },
    { name: 'C3' },
    { name: 'C4' },
    { name: 'C5' },
    { name: 'C6' },
    { name: 'C7' },
    { name: 'C8' },
    { name: 'C9' },
    { name: 'C10' },
    { name: 'C11' },
    { name: 'C12' },
    { name: 'C13' },
    { name: 'C14' },
    { name: 'C15' },
    { name: 'C16' },
    { name: 'C17' },
    { name: 'C18' },
    { name: 'C19' },
    { name: 'C20' },
    { name: 'C21' },
    { name: 'C22' },
    { name: 'C23' },
    { name: 'C24' },
    { name: 'C25' },
    { name: 'C26' },
    { name: 'C27' },
    { name: 'C28' },
  ];

  constructor(
    protected usuarioExtraService: UsuarioExtraService,
    protected userService: UserService,
    protected plantillaService: PlantillaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected accountService: AccountService
  ) {}

  ngOnInit(): void {
    // Get jhi_user and usuario_extra information
    this.accountService.getAuthenticationState().subscribe(account => {
      if (account !== null) {
        this.usuarioExtraService.find(account.id).subscribe(usuarioExtra => {
          this.usuarioExtra = usuarioExtra.body;
          if (this.usuarioExtra !== null) {
            if (this.usuarioExtra.id === undefined) {
              const today = dayjs().startOf('day');
              this.usuarioExtra.fechaNacimiento = today;
            }
            this.updateForm(this.usuarioExtra);
          }

          // this.loadRelationshipsOptions();
        });
      }
    });

    // this.activatedRoute.data.subscribe(({ usuarioExtra }) => {

    // });
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
      email: usuarioExtra.user?.login,
      id: usuarioExtra.id,
      nombre: usuarioExtra.nombre,
      iconoPerfil: usuarioExtra.iconoPerfil,
      fechaNacimiento: usuarioExtra.fechaNacimiento ? usuarioExtra.fechaNacimiento.format(DATE_TIME_FORMAT) : null,
      estado: usuarioExtra.estado,
      user: usuarioExtra.user,
      plantillas: usuarioExtra.plantillas,
    });

    // Update swiper
    this.profileIcon = parseInt(usuarioExtra.iconoPerfil!);
    this.profileIcons.forEach(icon => {
      if (parseInt(icon.name.split('C')[1]) === this.profileIcon) {
        icon.class = 'active';
      }
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

  selectIcon(event: MouseEvent): void {
    if (event.target instanceof Element) {
      document.querySelectorAll('.active').forEach(e => e.classList.remove('active'));
      event.target.classList.add('active');
      this.profileIcon = +event.target.getAttribute('id')! + 1;
    }
  }
}

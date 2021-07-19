import { Component, ContentChild, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import * as dayjs from 'dayjs';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPlantilla } from 'app/entities/plantilla/plantilla.model';
import { PlantillaService } from 'app/entities/plantilla/service/plantilla.service';
import { IUsuarioExtra, UsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';
import { AccountService } from 'app/core/auth/account.service';
import { LocalStorageService } from 'ngx-webstorage';
import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from '../../config/error.constants';
import { PasswordService } from '../password/password.service';

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html',
})
export class SettingsComponent implements OnInit {
  currentUrl = this.router.url;
  isSaving = false;
  success = false;
  successPassword = false;
  samePassword = false;
  error = false;
  errorPassword = false;
  doNotMatch = false;
  usersSharedCollection: IUser[] = [];
  plantillasSharedCollection: IPlantilla[] = [];
  showPassword = false;

  isGoogle = this.localStorageService.retrieve('IsGoogle');

  //Form info del usuario
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

  //form de la contraseña
  passwordForm = this.fb.group({
    password: [null, [Validators.required, Validators.minLength(8), Validators.maxLength(50)]],
    passwordNew: [null, [Validators.required, Validators.minLength(8), Validators.maxLength(50)]],
    passwordNewConfirm: [null, [Validators.required, Validators.minLength(8), Validators.maxLength(50)]],
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

  /*  @ContentChild(IonInput) input: IonInput;*/

  constructor(
    protected usuarioExtraService: UsuarioExtraService,
    protected userService: UserService,
    protected plantillaService: PlantillaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected accountService: AccountService,
    private localStorageService: LocalStorageService,
    protected passwordService: PasswordService,
    private router: Router
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

    //console.log(this.isGoogle);

    // this.activatedRoute.data.subscribe(({ usuarioExtra }) => {

    // });
  }

  previousState(): void {
    window.history.back();
  }

  //Se manda la info a guardar
  save(): void {
    this.isSaving = true;
    const usuarioExtra = this.createFromForm();

    console.log(usuarioExtra.iconoPerfil);
    console.log(usuarioExtra.fechaNacimiento);

    this.subscribeToSaveResponse(this.usuarioExtraService.update(usuarioExtra));
  }

  savePassword(): void {
    this.successPassword = false;
    this.doNotMatch = false;
    this.samePassword = false;
    this.errorPassword = false;

    const passwordNew = this.passwordForm.get(['passwordNew'])!.value;
    const passwordOld = this.passwordForm.get(['password'])!.value;
    if (passwordOld == passwordNew) {
      this.samePassword = true;
    } else {
      if (passwordNew !== this.passwordForm.get(['passwordNewConfirm'])!.value) {
        (this.doNotMatch = true), (this.samePassword = false);
      } else {
        this.passwordService.save(passwordNew, passwordOld).subscribe(
          () => (this.successPassword = true),

          () => (this.errorPassword = true)
        );
      }
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
      () => ((this.success = true), this.windowReload()),
      response => this.processError(response)
    );
  }
  windowReload() {
    this.router.navigate(['account/settings']).then(() => {
      window.location.reload();
    });
  }

  processError(response: HttpErrorResponse): void {
    if (response.status === 400) {
      this.error = true;
    }
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

  //Llena el formulario para que se vea en pantalla
  protected updateForm(usuarioExtra: IUsuarioExtra): void {
    this.editForm.patchValue({
      email: usuarioExtra.user?.login,
      id: usuarioExtra.id,
      nombre: usuarioExtra.nombre,
      iconoPerfil: usuarioExtra.iconoPerfil,
      fechaNacimiento: usuarioExtra.fechaNacimiento ? usuarioExtra.fechaNacimiento.format(DATE_FORMAT) : null,
      estado: usuarioExtra.estado,
      user: usuarioExtra.user,
      plantillas: usuarioExtra.plantillas,
    });

    // Update swiper
    this.profileIcon = usuarioExtra.iconoPerfil!;

    console.log(this.profileIcon);
    this.profileIcons.forEach(icon => {
      if (icon.name.split('C')[1] === this.profileIcon) {
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
      iconoPerfil: this.profileIcon,
      fechaNacimiento: this.editForm.get(['fechaNacimiento'])!.value
        ? dayjs(this.editForm.get(['fechaNacimiento'])!.value, DATE_FORMAT)
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

      //console.log(this.profileIcon);
    }
  }
}

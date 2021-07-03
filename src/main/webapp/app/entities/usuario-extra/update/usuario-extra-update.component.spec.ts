jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UsuarioExtraService } from '../service/usuario-extra.service';
import { IUsuarioExtra, UsuarioExtra } from '../usuario-extra.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IPlantilla } from 'app/entities/plantilla/plantilla.model';
import { PlantillaService } from 'app/entities/plantilla/service/plantilla.service';

import { UsuarioExtraUpdateComponent } from './usuario-extra-update.component';

describe('Component Tests', () => {
  describe('UsuarioExtra Management Update Component', () => {
    let comp: UsuarioExtraUpdateComponent;
    let fixture: ComponentFixture<UsuarioExtraUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let usuarioExtraService: UsuarioExtraService;
    let userService: UserService;
    let plantillaService: PlantillaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UsuarioExtraUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UsuarioExtraUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UsuarioExtraUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      usuarioExtraService = TestBed.inject(UsuarioExtraService);
      userService = TestBed.inject(UserService);
      plantillaService = TestBed.inject(PlantillaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call User query and add missing value', () => {
        const usuarioExtra: IUsuarioExtra = { id: 456 };
        const user: IUser = { id: 58280 };
        usuarioExtra.user = user;

        const userCollection: IUser[] = [{ id: 29686 }];
        jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
        const additionalUsers = [user];
        const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
        jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ usuarioExtra });
        comp.ngOnInit();

        expect(userService.query).toHaveBeenCalled();
        expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
        expect(comp.usersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Plantilla query and add missing value', () => {
        const usuarioExtra: IUsuarioExtra = { id: 456 };
        const plantillas: IPlantilla[] = [{ id: 54411 }];
        usuarioExtra.plantillas = plantillas;

        const plantillaCollection: IPlantilla[] = [{ id: 32212 }];
        jest.spyOn(plantillaService, 'query').mockReturnValue(of(new HttpResponse({ body: plantillaCollection })));
        const additionalPlantillas = [...plantillas];
        const expectedCollection: IPlantilla[] = [...additionalPlantillas, ...plantillaCollection];
        jest.spyOn(plantillaService, 'addPlantillaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ usuarioExtra });
        comp.ngOnInit();

        expect(plantillaService.query).toHaveBeenCalled();
        expect(plantillaService.addPlantillaToCollectionIfMissing).toHaveBeenCalledWith(plantillaCollection, ...additionalPlantillas);
        expect(comp.plantillasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const usuarioExtra: IUsuarioExtra = { id: 456 };
        const user: IUser = { id: 30429 };
        usuarioExtra.user = user;
        const plantillas: IPlantilla = { id: 61011 };
        usuarioExtra.plantillas = [plantillas];

        activatedRoute.data = of({ usuarioExtra });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(usuarioExtra));
        expect(comp.usersSharedCollection).toContain(user);
        expect(comp.plantillasSharedCollection).toContain(plantillas);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UsuarioExtra>>();
        const usuarioExtra = { id: 123 };
        jest.spyOn(usuarioExtraService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ usuarioExtra });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: usuarioExtra }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(usuarioExtraService.update).toHaveBeenCalledWith(usuarioExtra);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UsuarioExtra>>();
        const usuarioExtra = new UsuarioExtra();
        jest.spyOn(usuarioExtraService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ usuarioExtra });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: usuarioExtra }));
        saveSubject.complete();

        // THEN
        expect(usuarioExtraService.create).toHaveBeenCalledWith(usuarioExtra);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UsuarioExtra>>();
        const usuarioExtra = { id: 123 };
        jest.spyOn(usuarioExtraService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ usuarioExtra });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(usuarioExtraService.update).toHaveBeenCalledWith(usuarioExtra);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUserById', () => {
        it('Should return tracked User primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUserById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPlantillaById', () => {
        it('Should return tracked Plantilla primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPlantillaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedPlantilla', () => {
        it('Should return option if no Plantilla is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedPlantilla(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Plantilla for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedPlantilla(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Plantilla is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedPlantilla(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});

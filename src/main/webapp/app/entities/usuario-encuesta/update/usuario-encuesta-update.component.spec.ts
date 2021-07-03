jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { UsuarioEncuestaService } from '../service/usuario-encuesta.service';
import { IUsuarioEncuesta, UsuarioEncuesta } from '../usuario-encuesta.model';
import { IUsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';
import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { EncuestaService } from 'app/entities/encuesta/service/encuesta.service';

import { UsuarioEncuestaUpdateComponent } from './usuario-encuesta-update.component';

describe('Component Tests', () => {
  describe('UsuarioEncuesta Management Update Component', () => {
    let comp: UsuarioEncuestaUpdateComponent;
    let fixture: ComponentFixture<UsuarioEncuestaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let usuarioEncuestaService: UsuarioEncuestaService;
    let usuarioExtraService: UsuarioExtraService;
    let encuestaService: EncuestaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UsuarioEncuestaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(UsuarioEncuestaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UsuarioEncuestaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      usuarioEncuestaService = TestBed.inject(UsuarioEncuestaService);
      usuarioExtraService = TestBed.inject(UsuarioExtraService);
      encuestaService = TestBed.inject(EncuestaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call UsuarioExtra query and add missing value', () => {
        const usuarioEncuesta: IUsuarioEncuesta = { id: 456 };
        const usuarioExtra: IUsuarioExtra = { id: 70975 };
        usuarioEncuesta.usuarioExtra = usuarioExtra;

        const usuarioExtraCollection: IUsuarioExtra[] = [{ id: 39782 }];
        jest.spyOn(usuarioExtraService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioExtraCollection })));
        const additionalUsuarioExtras = [usuarioExtra];
        const expectedCollection: IUsuarioExtra[] = [...additionalUsuarioExtras, ...usuarioExtraCollection];
        jest.spyOn(usuarioExtraService, 'addUsuarioExtraToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ usuarioEncuesta });
        comp.ngOnInit();

        expect(usuarioExtraService.query).toHaveBeenCalled();
        expect(usuarioExtraService.addUsuarioExtraToCollectionIfMissing).toHaveBeenCalledWith(
          usuarioExtraCollection,
          ...additionalUsuarioExtras
        );
        expect(comp.usuarioExtrasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Encuesta query and add missing value', () => {
        const usuarioEncuesta: IUsuarioEncuesta = { id: 456 };
        const encuesta: IEncuesta = { id: 77959 };
        usuarioEncuesta.encuesta = encuesta;

        const encuestaCollection: IEncuesta[] = [{ id: 65363 }];
        jest.spyOn(encuestaService, 'query').mockReturnValue(of(new HttpResponse({ body: encuestaCollection })));
        const additionalEncuestas = [encuesta];
        const expectedCollection: IEncuesta[] = [...additionalEncuestas, ...encuestaCollection];
        jest.spyOn(encuestaService, 'addEncuestaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ usuarioEncuesta });
        comp.ngOnInit();

        expect(encuestaService.query).toHaveBeenCalled();
        expect(encuestaService.addEncuestaToCollectionIfMissing).toHaveBeenCalledWith(encuestaCollection, ...additionalEncuestas);
        expect(comp.encuestasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const usuarioEncuesta: IUsuarioEncuesta = { id: 456 };
        const usuarioExtra: IUsuarioExtra = { id: 58090 };
        usuarioEncuesta.usuarioExtra = usuarioExtra;
        const encuesta: IEncuesta = { id: 899 };
        usuarioEncuesta.encuesta = encuesta;

        activatedRoute.data = of({ usuarioEncuesta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(usuarioEncuesta));
        expect(comp.usuarioExtrasSharedCollection).toContain(usuarioExtra);
        expect(comp.encuestasSharedCollection).toContain(encuesta);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UsuarioEncuesta>>();
        const usuarioEncuesta = { id: 123 };
        jest.spyOn(usuarioEncuestaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ usuarioEncuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: usuarioEncuesta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(usuarioEncuestaService.update).toHaveBeenCalledWith(usuarioEncuesta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UsuarioEncuesta>>();
        const usuarioEncuesta = new UsuarioEncuesta();
        jest.spyOn(usuarioEncuestaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ usuarioEncuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: usuarioEncuesta }));
        saveSubject.complete();

        // THEN
        expect(usuarioEncuestaService.create).toHaveBeenCalledWith(usuarioEncuesta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<UsuarioEncuesta>>();
        const usuarioEncuesta = { id: 123 };
        jest.spyOn(usuarioEncuestaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ usuarioEncuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(usuarioEncuestaService.update).toHaveBeenCalledWith(usuarioEncuesta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackUsuarioExtraById', () => {
        it('Should return tracked UsuarioExtra primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUsuarioExtraById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEncuestaById', () => {
        it('Should return tracked Encuesta primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEncuestaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

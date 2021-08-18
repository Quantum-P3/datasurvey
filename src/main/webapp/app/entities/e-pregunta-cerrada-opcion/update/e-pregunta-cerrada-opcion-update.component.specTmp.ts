jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EPreguntaCerradaOpcionService } from '../service/e-pregunta-cerrada-opcion.service';
import { IEPreguntaCerradaOpcion, EPreguntaCerradaOpcion } from '../e-pregunta-cerrada-opcion.model';
import { IEPreguntaCerrada } from 'app/entities/e-pregunta-cerrada/e-pregunta-cerrada.model';
import { EPreguntaCerradaService } from 'app/entities/e-pregunta-cerrada/service/e-pregunta-cerrada.service';

import { EPreguntaCerradaOpcionUpdateComponent } from './e-pregunta-cerrada-opcion-update.component';

describe('Component Tests', () => {
  describe('EPreguntaCerradaOpcion Management Update Component', () => {
    let comp: EPreguntaCerradaOpcionUpdateComponent;
    let fixture: ComponentFixture<EPreguntaCerradaOpcionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ePreguntaCerradaOpcionService: EPreguntaCerradaOpcionService;
    let ePreguntaCerradaService: EPreguntaCerradaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EPreguntaCerradaOpcionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EPreguntaCerradaOpcionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EPreguntaCerradaOpcionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ePreguntaCerradaOpcionService = TestBed.inject(EPreguntaCerradaOpcionService);
      ePreguntaCerradaService = TestBed.inject(EPreguntaCerradaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call EPreguntaCerrada query and add missing value', () => {
        const ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion = { id: 456 };
        const ePreguntaCerrada: IEPreguntaCerrada = { id: 2192 };
        ePreguntaCerradaOpcion.epreguntaCerrada = ePreguntaCerrada;

        const ePreguntaCerradaCollection: IEPreguntaCerrada[] = [{ id: 89287 }];
        jest.spyOn(ePreguntaCerradaService, 'query').mockReturnValue(of(new HttpResponse({ body: ePreguntaCerradaCollection })));
        const additionalEPreguntaCerradas = [ePreguntaCerrada];
        const expectedCollection: IEPreguntaCerrada[] = [...additionalEPreguntaCerradas, ...ePreguntaCerradaCollection];
        jest.spyOn(ePreguntaCerradaService, 'addEPreguntaCerradaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ ePreguntaCerradaOpcion });
        comp.ngOnInit();

        expect(ePreguntaCerradaService.query).toHaveBeenCalled();
        expect(ePreguntaCerradaService.addEPreguntaCerradaToCollectionIfMissing).toHaveBeenCalledWith(
          ePreguntaCerradaCollection,
          ...additionalEPreguntaCerradas
        );
        expect(comp.ePreguntaCerradasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion = { id: 456 };
        const ePreguntaCerrada: IEPreguntaCerrada = { id: 64500 };
        ePreguntaCerradaOpcion.epreguntaCerrada = ePreguntaCerrada;

        activatedRoute.data = of({ ePreguntaCerradaOpcion });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ePreguntaCerradaOpcion));
        expect(comp.ePreguntaCerradasSharedCollection).toContain(ePreguntaCerrada);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaCerradaOpcion>>();
        const ePreguntaCerradaOpcion = { id: 123 };
        jest.spyOn(ePreguntaCerradaOpcionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaCerradaOpcion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ePreguntaCerradaOpcion }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ePreguntaCerradaOpcionService.update).toHaveBeenCalledWith(ePreguntaCerradaOpcion);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaCerradaOpcion>>();
        const ePreguntaCerradaOpcion = new EPreguntaCerradaOpcion();
        jest.spyOn(ePreguntaCerradaOpcionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaCerradaOpcion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ePreguntaCerradaOpcion }));
        saveSubject.complete();

        // THEN
        expect(ePreguntaCerradaOpcionService.create).toHaveBeenCalledWith(ePreguntaCerradaOpcion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaCerradaOpcion>>();
        const ePreguntaCerradaOpcion = { id: 123 };
        jest.spyOn(ePreguntaCerradaOpcionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaCerradaOpcion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ePreguntaCerradaOpcionService.update).toHaveBeenCalledWith(ePreguntaCerradaOpcion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEPreguntaCerradaById', () => {
        it('Should return tracked EPreguntaCerrada primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEPreguntaCerradaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EPreguntaCerradaService } from '../service/e-pregunta-cerrada.service';
import { IEPreguntaCerrada, EPreguntaCerrada } from '../e-pregunta-cerrada.model';
import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { EncuestaService } from 'app/entities/encuesta/service/encuesta.service';

import { EPreguntaCerradaUpdateComponent } from './e-pregunta-cerrada-update.component';

describe('Component Tests', () => {
  describe('EPreguntaCerrada Management Update Component', () => {
    let comp: EPreguntaCerradaUpdateComponent;
    let fixture: ComponentFixture<EPreguntaCerradaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ePreguntaCerradaService: EPreguntaCerradaService;
    let encuestaService: EncuestaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EPreguntaCerradaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EPreguntaCerradaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EPreguntaCerradaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ePreguntaCerradaService = TestBed.inject(EPreguntaCerradaService);
      encuestaService = TestBed.inject(EncuestaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Encuesta query and add missing value', () => {
        const ePreguntaCerrada: IEPreguntaCerrada = { id: 456 };
        const encuesta: IEncuesta = { id: 27208 };
        ePreguntaCerrada.encuesta = encuesta;

        const encuestaCollection: IEncuesta[] = [{ id: 3120 }];
        jest.spyOn(encuestaService, 'query').mockReturnValue(of(new HttpResponse({ body: encuestaCollection })));
        const additionalEncuestas = [encuesta];
        const expectedCollection: IEncuesta[] = [...additionalEncuestas, ...encuestaCollection];
        jest.spyOn(encuestaService, 'addEncuestaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ ePreguntaCerrada });
        comp.ngOnInit();

        expect(encuestaService.query).toHaveBeenCalled();
        expect(encuestaService.addEncuestaToCollectionIfMissing).toHaveBeenCalledWith(encuestaCollection, ...additionalEncuestas);
        expect(comp.encuestasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ePreguntaCerrada: IEPreguntaCerrada = { id: 456 };
        const encuesta: IEncuesta = { id: 4358 };
        ePreguntaCerrada.encuesta = encuesta;

        activatedRoute.data = of({ ePreguntaCerrada });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ePreguntaCerrada));
        expect(comp.encuestasSharedCollection).toContain(encuesta);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaCerrada>>();
        const ePreguntaCerrada = { id: 123 };
        jest.spyOn(ePreguntaCerradaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaCerrada });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ePreguntaCerrada }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ePreguntaCerradaService.update).toHaveBeenCalledWith(ePreguntaCerrada);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaCerrada>>();
        const ePreguntaCerrada = new EPreguntaCerrada();
        jest.spyOn(ePreguntaCerradaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaCerrada });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ePreguntaCerrada }));
        saveSubject.complete();

        // THEN
        expect(ePreguntaCerradaService.create).toHaveBeenCalledWith(ePreguntaCerrada);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaCerrada>>();
        const ePreguntaCerrada = { id: 123 };
        jest.spyOn(ePreguntaCerradaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaCerrada });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ePreguntaCerradaService.update).toHaveBeenCalledWith(ePreguntaCerrada);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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

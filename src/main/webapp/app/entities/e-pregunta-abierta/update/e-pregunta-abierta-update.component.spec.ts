jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EPreguntaAbiertaService } from '../service/e-pregunta-abierta.service';
import { IEPreguntaAbierta, EPreguntaAbierta } from '../e-pregunta-abierta.model';
import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { EncuestaService } from 'app/entities/encuesta/service/encuesta.service';

import { EPreguntaAbiertaUpdateComponent } from './e-pregunta-abierta-update.component';

describe('Component Tests', () => {
  describe('EPreguntaAbierta Management Update Component', () => {
    let comp: EPreguntaAbiertaUpdateComponent;
    let fixture: ComponentFixture<EPreguntaAbiertaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ePreguntaAbiertaService: EPreguntaAbiertaService;
    let encuestaService: EncuestaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EPreguntaAbiertaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EPreguntaAbiertaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EPreguntaAbiertaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ePreguntaAbiertaService = TestBed.inject(EPreguntaAbiertaService);
      encuestaService = TestBed.inject(EncuestaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Encuesta query and add missing value', () => {
        const ePreguntaAbierta: IEPreguntaAbierta = { id: 456 };
        const encuesta: IEncuesta = { id: 87544 };
        ePreguntaAbierta.encuesta = encuesta;

        const encuestaCollection: IEncuesta[] = [{ id: 93487 }];
        jest.spyOn(encuestaService, 'query').mockReturnValue(of(new HttpResponse({ body: encuestaCollection })));
        const additionalEncuestas = [encuesta];
        const expectedCollection: IEncuesta[] = [...additionalEncuestas, ...encuestaCollection];
        jest.spyOn(encuestaService, 'addEncuestaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ ePreguntaAbierta });
        comp.ngOnInit();

        expect(encuestaService.query).toHaveBeenCalled();
        expect(encuestaService.addEncuestaToCollectionIfMissing).toHaveBeenCalledWith(encuestaCollection, ...additionalEncuestas);
        expect(comp.encuestasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ePreguntaAbierta: IEPreguntaAbierta = { id: 456 };
        const encuesta: IEncuesta = { id: 41784 };
        ePreguntaAbierta.encuesta = encuesta;

        activatedRoute.data = of({ ePreguntaAbierta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ePreguntaAbierta));
        expect(comp.encuestasSharedCollection).toContain(encuesta);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaAbierta>>();
        const ePreguntaAbierta = { id: 123 };
        jest.spyOn(ePreguntaAbiertaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaAbierta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ePreguntaAbierta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ePreguntaAbiertaService.update).toHaveBeenCalledWith(ePreguntaAbierta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaAbierta>>();
        const ePreguntaAbierta = new EPreguntaAbierta();
        jest.spyOn(ePreguntaAbiertaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaAbierta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ePreguntaAbierta }));
        saveSubject.complete();

        // THEN
        expect(ePreguntaAbiertaService.create).toHaveBeenCalledWith(ePreguntaAbierta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaAbierta>>();
        const ePreguntaAbierta = { id: 123 };
        jest.spyOn(ePreguntaAbiertaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaAbierta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ePreguntaAbiertaService.update).toHaveBeenCalledWith(ePreguntaAbierta);
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

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EPreguntaAbiertaRespuestaService } from '../service/e-pregunta-abierta-respuesta.service';
import { IEPreguntaAbiertaRespuesta, EPreguntaAbiertaRespuesta } from '../e-pregunta-abierta-respuesta.model';
import { IEPreguntaAbierta } from 'app/entities/e-pregunta-abierta/e-pregunta-abierta.model';
import { EPreguntaAbiertaService } from 'app/entities/e-pregunta-abierta/service/e-pregunta-abierta.service';

import { EPreguntaAbiertaRespuestaUpdateComponent } from './e-pregunta-abierta-respuesta-update.component';

describe('Component Tests', () => {
  describe('EPreguntaAbiertaRespuesta Management Update Component', () => {
    let comp: EPreguntaAbiertaRespuestaUpdateComponent;
    let fixture: ComponentFixture<EPreguntaAbiertaRespuestaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ePreguntaAbiertaRespuestaService: EPreguntaAbiertaRespuestaService;
    let ePreguntaAbiertaService: EPreguntaAbiertaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EPreguntaAbiertaRespuestaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EPreguntaAbiertaRespuestaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EPreguntaAbiertaRespuestaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ePreguntaAbiertaRespuestaService = TestBed.inject(EPreguntaAbiertaRespuestaService);
      ePreguntaAbiertaService = TestBed.inject(EPreguntaAbiertaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call EPreguntaAbierta query and add missing value', () => {
        const ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta = { id: 456 };
        const ePreguntaAbierta: IEPreguntaAbierta = { id: 35011 };
        ePreguntaAbiertaRespuesta.ePreguntaAbierta = ePreguntaAbierta;

        const ePreguntaAbiertaCollection: IEPreguntaAbierta[] = [{ id: 58318 }];
        jest.spyOn(ePreguntaAbiertaService, 'query').mockReturnValue(of(new HttpResponse({ body: ePreguntaAbiertaCollection })));
        const additionalEPreguntaAbiertas = [ePreguntaAbierta];
        const expectedCollection: IEPreguntaAbierta[] = [...additionalEPreguntaAbiertas, ...ePreguntaAbiertaCollection];
        jest.spyOn(ePreguntaAbiertaService, 'addEPreguntaAbiertaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ ePreguntaAbiertaRespuesta });
        comp.ngOnInit();

        expect(ePreguntaAbiertaService.query).toHaveBeenCalled();
        expect(ePreguntaAbiertaService.addEPreguntaAbiertaToCollectionIfMissing).toHaveBeenCalledWith(
          ePreguntaAbiertaCollection,
          ...additionalEPreguntaAbiertas
        );
        expect(comp.ePreguntaAbiertasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta = { id: 456 };
        const ePreguntaAbierta: IEPreguntaAbierta = { id: 40814 };
        ePreguntaAbiertaRespuesta.ePreguntaAbierta = ePreguntaAbierta;

        activatedRoute.data = of({ ePreguntaAbiertaRespuesta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(ePreguntaAbiertaRespuesta));
        expect(comp.ePreguntaAbiertasSharedCollection).toContain(ePreguntaAbierta);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaAbiertaRespuesta>>();
        const ePreguntaAbiertaRespuesta = { id: 123 };
        jest.spyOn(ePreguntaAbiertaRespuestaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaAbiertaRespuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ePreguntaAbiertaRespuesta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ePreguntaAbiertaRespuestaService.update).toHaveBeenCalledWith(ePreguntaAbiertaRespuesta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaAbiertaRespuesta>>();
        const ePreguntaAbiertaRespuesta = new EPreguntaAbiertaRespuesta();
        jest.spyOn(ePreguntaAbiertaRespuestaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaAbiertaRespuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: ePreguntaAbiertaRespuesta }));
        saveSubject.complete();

        // THEN
        expect(ePreguntaAbiertaRespuestaService.create).toHaveBeenCalledWith(ePreguntaAbiertaRespuesta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<EPreguntaAbiertaRespuesta>>();
        const ePreguntaAbiertaRespuesta = { id: 123 };
        jest.spyOn(ePreguntaAbiertaRespuestaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ ePreguntaAbiertaRespuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ePreguntaAbiertaRespuestaService.update).toHaveBeenCalledWith(ePreguntaAbiertaRespuesta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackEPreguntaAbiertaById', () => {
        it('Should return tracked EPreguntaAbierta primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEPreguntaAbiertaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

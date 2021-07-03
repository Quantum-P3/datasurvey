jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PPreguntaCerradaOpcionService } from '../service/p-pregunta-cerrada-opcion.service';
import { IPPreguntaCerradaOpcion, PPreguntaCerradaOpcion } from '../p-pregunta-cerrada-opcion.model';
import { IPPreguntaCerrada } from 'app/entities/p-pregunta-cerrada/p-pregunta-cerrada.model';
import { PPreguntaCerradaService } from 'app/entities/p-pregunta-cerrada/service/p-pregunta-cerrada.service';

import { PPreguntaCerradaOpcionUpdateComponent } from './p-pregunta-cerrada-opcion-update.component';

describe('Component Tests', () => {
  describe('PPreguntaCerradaOpcion Management Update Component', () => {
    let comp: PPreguntaCerradaOpcionUpdateComponent;
    let fixture: ComponentFixture<PPreguntaCerradaOpcionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pPreguntaCerradaOpcionService: PPreguntaCerradaOpcionService;
    let pPreguntaCerradaService: PPreguntaCerradaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PPreguntaCerradaOpcionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PPreguntaCerradaOpcionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PPreguntaCerradaOpcionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pPreguntaCerradaOpcionService = TestBed.inject(PPreguntaCerradaOpcionService);
      pPreguntaCerradaService = TestBed.inject(PPreguntaCerradaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PPreguntaCerrada query and add missing value', () => {
        const pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion = { id: 456 };
        const pPreguntaCerrada: IPPreguntaCerrada = { id: 81712 };
        pPreguntaCerradaOpcion.pPreguntaCerrada = pPreguntaCerrada;

        const pPreguntaCerradaCollection: IPPreguntaCerrada[] = [{ id: 88109 }];
        jest.spyOn(pPreguntaCerradaService, 'query').mockReturnValue(of(new HttpResponse({ body: pPreguntaCerradaCollection })));
        const additionalPPreguntaCerradas = [pPreguntaCerrada];
        const expectedCollection: IPPreguntaCerrada[] = [...additionalPPreguntaCerradas, ...pPreguntaCerradaCollection];
        jest.spyOn(pPreguntaCerradaService, 'addPPreguntaCerradaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ pPreguntaCerradaOpcion });
        comp.ngOnInit();

        expect(pPreguntaCerradaService.query).toHaveBeenCalled();
        expect(pPreguntaCerradaService.addPPreguntaCerradaToCollectionIfMissing).toHaveBeenCalledWith(
          pPreguntaCerradaCollection,
          ...additionalPPreguntaCerradas
        );
        expect(comp.pPreguntaCerradasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion = { id: 456 };
        const pPreguntaCerrada: IPPreguntaCerrada = { id: 91913 };
        pPreguntaCerradaOpcion.pPreguntaCerrada = pPreguntaCerrada;

        activatedRoute.data = of({ pPreguntaCerradaOpcion });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pPreguntaCerradaOpcion));
        expect(comp.pPreguntaCerradasSharedCollection).toContain(pPreguntaCerrada);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PPreguntaCerradaOpcion>>();
        const pPreguntaCerradaOpcion = { id: 123 };
        jest.spyOn(pPreguntaCerradaOpcionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pPreguntaCerradaOpcion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pPreguntaCerradaOpcion }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pPreguntaCerradaOpcionService.update).toHaveBeenCalledWith(pPreguntaCerradaOpcion);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PPreguntaCerradaOpcion>>();
        const pPreguntaCerradaOpcion = new PPreguntaCerradaOpcion();
        jest.spyOn(pPreguntaCerradaOpcionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pPreguntaCerradaOpcion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pPreguntaCerradaOpcion }));
        saveSubject.complete();

        // THEN
        expect(pPreguntaCerradaOpcionService.create).toHaveBeenCalledWith(pPreguntaCerradaOpcion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PPreguntaCerradaOpcion>>();
        const pPreguntaCerradaOpcion = { id: 123 };
        jest.spyOn(pPreguntaCerradaOpcionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pPreguntaCerradaOpcion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pPreguntaCerradaOpcionService.update).toHaveBeenCalledWith(pPreguntaCerradaOpcion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPPreguntaCerradaById', () => {
        it('Should return tracked PPreguntaCerrada primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPPreguntaCerradaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

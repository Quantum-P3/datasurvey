jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PPreguntaCerradaService } from '../service/p-pregunta-cerrada.service';
import { IPPreguntaCerrada, PPreguntaCerrada } from '../p-pregunta-cerrada.model';
import { IPlantilla } from 'app/entities/plantilla/plantilla.model';
import { PlantillaService } from 'app/entities/plantilla/service/plantilla.service';

import { PPreguntaCerradaUpdateComponent } from './p-pregunta-cerrada-update.component';

describe('Component Tests', () => {
  describe('PPreguntaCerrada Management Update Component', () => {
    let comp: PPreguntaCerradaUpdateComponent;
    let fixture: ComponentFixture<PPreguntaCerradaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pPreguntaCerradaService: PPreguntaCerradaService;
    let plantillaService: PlantillaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PPreguntaCerradaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PPreguntaCerradaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PPreguntaCerradaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pPreguntaCerradaService = TestBed.inject(PPreguntaCerradaService);
      plantillaService = TestBed.inject(PlantillaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Plantilla query and add missing value', () => {
        const pPreguntaCerrada: IPPreguntaCerrada = { id: 456 };
        const plantilla: IPlantilla = { id: 93915 };
        pPreguntaCerrada.plantilla = plantilla;

        const plantillaCollection: IPlantilla[] = [{ id: 13237 }];
        jest.spyOn(plantillaService, 'query').mockReturnValue(of(new HttpResponse({ body: plantillaCollection })));
        const additionalPlantillas = [plantilla];
        const expectedCollection: IPlantilla[] = [...additionalPlantillas, ...plantillaCollection];
        jest.spyOn(plantillaService, 'addPlantillaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ pPreguntaCerrada });
        comp.ngOnInit();

        expect(plantillaService.query).toHaveBeenCalled();
        expect(plantillaService.addPlantillaToCollectionIfMissing).toHaveBeenCalledWith(plantillaCollection, ...additionalPlantillas);
        expect(comp.plantillasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pPreguntaCerrada: IPPreguntaCerrada = { id: 456 };
        const plantilla: IPlantilla = { id: 35501 };
        pPreguntaCerrada.plantilla = plantilla;

        activatedRoute.data = of({ pPreguntaCerrada });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pPreguntaCerrada));
        expect(comp.plantillasSharedCollection).toContain(plantilla);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PPreguntaCerrada>>();
        const pPreguntaCerrada = { id: 123 };
        jest.spyOn(pPreguntaCerradaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pPreguntaCerrada });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pPreguntaCerrada }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pPreguntaCerradaService.update).toHaveBeenCalledWith(pPreguntaCerrada);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PPreguntaCerrada>>();
        const pPreguntaCerrada = new PPreguntaCerrada();
        jest.spyOn(pPreguntaCerradaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pPreguntaCerrada });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pPreguntaCerrada }));
        saveSubject.complete();

        // THEN
        expect(pPreguntaCerradaService.create).toHaveBeenCalledWith(pPreguntaCerrada);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PPreguntaCerrada>>();
        const pPreguntaCerrada = { id: 123 };
        jest.spyOn(pPreguntaCerradaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pPreguntaCerrada });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pPreguntaCerradaService.update).toHaveBeenCalledWith(pPreguntaCerrada);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPlantillaById', () => {
        it('Should return tracked Plantilla primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPlantillaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

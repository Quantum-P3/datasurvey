jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PPreguntaAbiertaService } from '../service/p-pregunta-abierta.service';
import { IPPreguntaAbierta, PPreguntaAbierta } from '../p-pregunta-abierta.model';
import { IPlantilla } from 'app/entities/plantilla/plantilla.model';
import { PlantillaService } from 'app/entities/plantilla/service/plantilla.service';

import { PPreguntaAbiertaUpdateComponent } from './p-pregunta-abierta-update.component';

describe('Component Tests', () => {
  describe('PPreguntaAbierta Management Update Component', () => {
    let comp: PPreguntaAbiertaUpdateComponent;
    let fixture: ComponentFixture<PPreguntaAbiertaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pPreguntaAbiertaService: PPreguntaAbiertaService;
    let plantillaService: PlantillaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PPreguntaAbiertaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PPreguntaAbiertaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PPreguntaAbiertaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pPreguntaAbiertaService = TestBed.inject(PPreguntaAbiertaService);
      plantillaService = TestBed.inject(PlantillaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Plantilla query and add missing value', () => {
        const pPreguntaAbierta: IPPreguntaAbierta = { id: 456 };
        const plantilla: IPlantilla = { id: 89636 };
        pPreguntaAbierta.plantilla = plantilla;

        const plantillaCollection: IPlantilla[] = [{ id: 65124 }];
        jest.spyOn(plantillaService, 'query').mockReturnValue(of(new HttpResponse({ body: plantillaCollection })));
        const additionalPlantillas = [plantilla];
        const expectedCollection: IPlantilla[] = [...additionalPlantillas, ...plantillaCollection];
        jest.spyOn(plantillaService, 'addPlantillaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ pPreguntaAbierta });
        comp.ngOnInit();

        expect(plantillaService.query).toHaveBeenCalled();
        expect(plantillaService.addPlantillaToCollectionIfMissing).toHaveBeenCalledWith(plantillaCollection, ...additionalPlantillas);
        expect(comp.plantillasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pPreguntaAbierta: IPPreguntaAbierta = { id: 456 };
        const plantilla: IPlantilla = { id: 15037 };
        pPreguntaAbierta.plantilla = plantilla;

        activatedRoute.data = of({ pPreguntaAbierta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pPreguntaAbierta));
        expect(comp.plantillasSharedCollection).toContain(plantilla);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PPreguntaAbierta>>();
        const pPreguntaAbierta = { id: 123 };
        jest.spyOn(pPreguntaAbiertaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pPreguntaAbierta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pPreguntaAbierta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pPreguntaAbiertaService.update).toHaveBeenCalledWith(pPreguntaAbierta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PPreguntaAbierta>>();
        const pPreguntaAbierta = new PPreguntaAbierta();
        jest.spyOn(pPreguntaAbiertaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pPreguntaAbierta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pPreguntaAbierta }));
        saveSubject.complete();

        // THEN
        expect(pPreguntaAbiertaService.create).toHaveBeenCalledWith(pPreguntaAbierta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PPreguntaAbierta>>();
        const pPreguntaAbierta = { id: 123 };
        jest.spyOn(pPreguntaAbiertaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pPreguntaAbierta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pPreguntaAbiertaService.update).toHaveBeenCalledWith(pPreguntaAbierta);
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

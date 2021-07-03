jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlantillaService } from '../service/plantilla.service';
import { IPlantilla, Plantilla } from '../plantilla.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';

import { PlantillaUpdateComponent } from './plantilla-update.component';

describe('Component Tests', () => {
  describe('Plantilla Management Update Component', () => {
    let comp: PlantillaUpdateComponent;
    let fixture: ComponentFixture<PlantillaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let plantillaService: PlantillaService;
    let categoriaService: CategoriaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlantillaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PlantillaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlantillaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      plantillaService = TestBed.inject(PlantillaService);
      categoriaService = TestBed.inject(CategoriaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Categoria query and add missing value', () => {
        const plantilla: IPlantilla = { id: 456 };
        const categoria: ICategoria = { id: 73894 };
        plantilla.categoria = categoria;

        const categoriaCollection: ICategoria[] = [{ id: 27351 }];
        jest.spyOn(categoriaService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaCollection })));
        const additionalCategorias = [categoria];
        const expectedCollection: ICategoria[] = [...additionalCategorias, ...categoriaCollection];
        jest.spyOn(categoriaService, 'addCategoriaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ plantilla });
        comp.ngOnInit();

        expect(categoriaService.query).toHaveBeenCalled();
        expect(categoriaService.addCategoriaToCollectionIfMissing).toHaveBeenCalledWith(categoriaCollection, ...additionalCategorias);
        expect(comp.categoriasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const plantilla: IPlantilla = { id: 456 };
        const categoria: ICategoria = { id: 51554 };
        plantilla.categoria = categoria;

        activatedRoute.data = of({ plantilla });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(plantilla));
        expect(comp.categoriasSharedCollection).toContain(categoria);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Plantilla>>();
        const plantilla = { id: 123 };
        jest.spyOn(plantillaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ plantilla });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: plantilla }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(plantillaService.update).toHaveBeenCalledWith(plantilla);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Plantilla>>();
        const plantilla = new Plantilla();
        jest.spyOn(plantillaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ plantilla });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: plantilla }));
        saveSubject.complete();

        // THEN
        expect(plantillaService.create).toHaveBeenCalledWith(plantilla);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Plantilla>>();
        const plantilla = { id: 123 };
        jest.spyOn(plantillaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ plantilla });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(plantillaService.update).toHaveBeenCalledWith(plantilla);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCategoriaById', () => {
        it('Should return tracked Categoria primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCategoriaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

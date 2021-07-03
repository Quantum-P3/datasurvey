jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ParametroAplicacionService } from '../service/parametro-aplicacion.service';
import { IParametroAplicacion, ParametroAplicacion } from '../parametro-aplicacion.model';

import { ParametroAplicacionUpdateComponent } from './parametro-aplicacion-update.component';

describe('Component Tests', () => {
  describe('ParametroAplicacion Management Update Component', () => {
    let comp: ParametroAplicacionUpdateComponent;
    let fixture: ComponentFixture<ParametroAplicacionUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let parametroAplicacionService: ParametroAplicacionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ParametroAplicacionUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ParametroAplicacionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParametroAplicacionUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      parametroAplicacionService = TestBed.inject(ParametroAplicacionService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const parametroAplicacion: IParametroAplicacion = { id: 456 };

        activatedRoute.data = of({ parametroAplicacion });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(parametroAplicacion));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ParametroAplicacion>>();
        const parametroAplicacion = { id: 123 };
        jest.spyOn(parametroAplicacionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ parametroAplicacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: parametroAplicacion }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(parametroAplicacionService.update).toHaveBeenCalledWith(parametroAplicacion);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ParametroAplicacion>>();
        const parametroAplicacion = new ParametroAplicacion();
        jest.spyOn(parametroAplicacionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ parametroAplicacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: parametroAplicacion }));
        saveSubject.complete();

        // THEN
        expect(parametroAplicacionService.create).toHaveBeenCalledWith(parametroAplicacion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ParametroAplicacion>>();
        const parametroAplicacion = { id: 123 };
        jest.spyOn(parametroAplicacionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ parametroAplicacion });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(parametroAplicacionService.update).toHaveBeenCalledWith(parametroAplicacion);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

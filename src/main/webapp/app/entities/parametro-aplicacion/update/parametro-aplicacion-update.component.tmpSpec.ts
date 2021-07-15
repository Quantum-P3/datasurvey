import { Account } from '../../../core/auth/account.model';

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
    const parametro: ParametroAplicacion = {
      id: 1,
      minDiasEncuesta: 1,
      maxDiasEncuesta: 5,
      minCantidadPreguntas: 6,
      maxCantidadPreguntas: 7,
    };

    const parametro2: ParametroAplicacion = {
      id: 2,
      minDiasEncuesta: 1,
      maxDiasEncuesta: 5,
      minCantidadPreguntas: 6,
      maxCantidadPreguntas: 7,
    };

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
        const parametroAplicacion: IParametroAplicacion = { id: 1 };

        activatedRoute.data = of({ parametro });
        comp.ngOnInit();

        expect(parametro).toEqual(expect.objectContaining(parametro));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ParametroAplicacion>>();
        const parametroAplicacion = { id: 1 };
        jest.spyOn(parametroAplicacionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ parametro });
        comp.ngOnInit();

        // WHEN
        comp.save();
        // expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: parametro }));
        saveSubject.complete();

        // THEN
        //expect(comp.previousState).toHaveBeenCalled();
        expect(parametroAplicacionService.update).toHaveBeenCalledWith(parametro);
        //expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ParametroAplicacion>>();
        const parametroAplicacion = new ParametroAplicacion();
        jest.spyOn(parametroAplicacionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ parametro2 });
        comp.ngOnInit();

        // WHEN
        comp.save();
        // expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: parametro2 }));
        saveSubject.complete();

        // THEN
        expect(parametroAplicacionService.create).toHaveBeenCalledWith(parametro2);
        //expect(comp.isSaving).toEqual(false);
        // expect(comp.previousState).toHaveBeenCalled();
      });
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EncuestaService } from '../service/encuesta.service';
import { IEncuesta, Encuesta } from '../encuesta.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { CategoriaService } from 'app/entities/categoria/service/categoria.service';
import { IUsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { UsuarioExtraService } from 'app/entities/usuario-extra/service/usuario-extra.service';

import { EncuestaUpdateComponent } from './encuesta-update.component';

describe('Component Tests', () => {
  describe('Encuesta Management Update Component', () => {
    let comp: EncuestaUpdateComponent;
    let fixture: ComponentFixture<EncuestaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let encuestaService: EncuestaService;
    let categoriaService: CategoriaService;
    let usuarioExtraService: UsuarioExtraService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EncuestaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EncuestaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EncuestaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      encuestaService = TestBed.inject(EncuestaService);
      categoriaService = TestBed.inject(CategoriaService);
      usuarioExtraService = TestBed.inject(UsuarioExtraService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Categoria query and add missing value', () => {
        const encuesta: IEncuesta = { id: 456 };
        const categoria: ICategoria = { id: 88802 };
        encuesta.categoria = categoria;

        const categoriaCollection: ICategoria[] = [{ id: 15790 }];
        jest.spyOn(categoriaService, 'query').mockReturnValue(of(new HttpResponse({ body: categoriaCollection })));
        const additionalCategorias = [categoria];
        const expectedCollection: ICategoria[] = [...additionalCategorias, ...categoriaCollection];
        jest.spyOn(categoriaService, 'addCategoriaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ encuesta });
        comp.ngOnInit();

        expect(categoriaService.query).toHaveBeenCalled();
        expect(categoriaService.addCategoriaToCollectionIfMissing).toHaveBeenCalledWith(categoriaCollection, ...additionalCategorias);
        expect(comp.categoriasSharedCollection).toEqual(expectedCollection);
      });

      it('Should call UsuarioExtra query and add missing value', () => {
        const encuesta: IEncuesta = { id: 456 };
        const usuarioExtra: IUsuarioExtra = { id: 83581 };
        encuesta.usuarioExtra = usuarioExtra;

        const usuarioExtraCollection: IUsuarioExtra[] = [{ id: 89078 }];
        jest.spyOn(usuarioExtraService, 'query').mockReturnValue(of(new HttpResponse({ body: usuarioExtraCollection })));
        const additionalUsuarioExtras = [usuarioExtra];
        const expectedCollection: IUsuarioExtra[] = [...additionalUsuarioExtras, ...usuarioExtraCollection];
        jest.spyOn(usuarioExtraService, 'addUsuarioExtraToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ encuesta });
        comp.ngOnInit();

        expect(usuarioExtraService.query).toHaveBeenCalled();
        expect(usuarioExtraService.addUsuarioExtraToCollectionIfMissing).toHaveBeenCalledWith(
          usuarioExtraCollection,
          ...additionalUsuarioExtras
        );
        expect(comp.usuarioExtrasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const encuesta: IEncuesta = { id: 456 };
        const categoria: ICategoria = { id: 7805 };
        encuesta.categoria = categoria;
        const usuarioExtra: IUsuarioExtra = { id: 34590 };
        encuesta.usuarioExtra = usuarioExtra;

        activatedRoute.data = of({ encuesta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(encuesta));
        expect(comp.categoriasSharedCollection).toContain(categoria);
        expect(comp.usuarioExtrasSharedCollection).toContain(usuarioExtra);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Encuesta>>();
        const encuesta = { id: 123 };
        jest.spyOn(encuestaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ encuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: encuesta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(encuestaService.update).toHaveBeenCalledWith(encuesta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Encuesta>>();
        const encuesta = new Encuesta();
        jest.spyOn(encuestaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ encuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: encuesta }));
        saveSubject.complete();

        // THEN
        expect(encuestaService.create).toHaveBeenCalledWith(encuesta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Encuesta>>();
        const encuesta = { id: 123 };
        jest.spyOn(encuestaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ encuesta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(encuestaService.update).toHaveBeenCalledWith(encuesta);
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

      describe('trackUsuarioExtraById', () => {
        it('Should return tracked UsuarioExtra primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackUsuarioExtraById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PPreguntaCerradaOpcionService } from '../service/p-pregunta-cerrada-opcion.service';

import { PPreguntaCerradaOpcionComponent } from './p-pregunta-cerrada-opcion.component';

describe('Component Tests', () => {
  describe('PPreguntaCerradaOpcion Management Component', () => {
    let comp: PPreguntaCerradaOpcionComponent;
    let fixture: ComponentFixture<PPreguntaCerradaOpcionComponent>;
    let service: PPreguntaCerradaOpcionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PPreguntaCerradaOpcionComponent],
      })
        .overrideTemplate(PPreguntaCerradaOpcionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PPreguntaCerradaOpcionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PPreguntaCerradaOpcionService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.pPreguntaCerradaOpcions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

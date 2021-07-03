import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EPreguntaCerradaOpcionService } from '../service/e-pregunta-cerrada-opcion.service';

import { EPreguntaCerradaOpcionComponent } from './e-pregunta-cerrada-opcion.component';

describe('Component Tests', () => {
  describe('EPreguntaCerradaOpcion Management Component', () => {
    let comp: EPreguntaCerradaOpcionComponent;
    let fixture: ComponentFixture<EPreguntaCerradaOpcionComponent>;
    let service: EPreguntaCerradaOpcionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EPreguntaCerradaOpcionComponent],
      })
        .overrideTemplate(EPreguntaCerradaOpcionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EPreguntaCerradaOpcionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EPreguntaCerradaOpcionService);

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
      expect(comp.ePreguntaCerradaOpcions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

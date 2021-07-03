import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PPreguntaCerradaService } from '../service/p-pregunta-cerrada.service';

import { PPreguntaCerradaComponent } from './p-pregunta-cerrada.component';

describe('Component Tests', () => {
  describe('PPreguntaCerrada Management Component', () => {
    let comp: PPreguntaCerradaComponent;
    let fixture: ComponentFixture<PPreguntaCerradaComponent>;
    let service: PPreguntaCerradaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PPreguntaCerradaComponent],
      })
        .overrideTemplate(PPreguntaCerradaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PPreguntaCerradaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PPreguntaCerradaService);

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
      expect(comp.pPreguntaCerradas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

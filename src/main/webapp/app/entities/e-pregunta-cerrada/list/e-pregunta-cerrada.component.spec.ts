import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EPreguntaCerradaService } from '../service/e-pregunta-cerrada.service';

import { EPreguntaCerradaComponent } from './e-pregunta-cerrada.component';

describe('Component Tests', () => {
  describe('EPreguntaCerrada Management Component', () => {
    let comp: EPreguntaCerradaComponent;
    let fixture: ComponentFixture<EPreguntaCerradaComponent>;
    let service: EPreguntaCerradaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EPreguntaCerradaComponent],
      })
        .overrideTemplate(EPreguntaCerradaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EPreguntaCerradaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EPreguntaCerradaService);

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
      expect(comp.ePreguntaCerradas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

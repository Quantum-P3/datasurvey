import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EPreguntaAbiertaService } from '../service/e-pregunta-abierta.service';

import { EPreguntaAbiertaComponent } from './e-pregunta-abierta.component';

describe('Component Tests', () => {
  describe('EPreguntaAbierta Management Component', () => {
    let comp: EPreguntaAbiertaComponent;
    let fixture: ComponentFixture<EPreguntaAbiertaComponent>;
    let service: EPreguntaAbiertaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EPreguntaAbiertaComponent],
      })
        .overrideTemplate(EPreguntaAbiertaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EPreguntaAbiertaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EPreguntaAbiertaService);

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
      expect(comp.ePreguntaAbiertas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EPreguntaAbiertaRespuestaService } from '../service/e-pregunta-abierta-respuesta.service';

import { EPreguntaAbiertaRespuestaComponent } from './e-pregunta-abierta-respuesta.component';

describe('Component Tests', () => {
  describe('EPreguntaAbiertaRespuesta Management Component', () => {
    let comp: EPreguntaAbiertaRespuestaComponent;
    let fixture: ComponentFixture<EPreguntaAbiertaRespuestaComponent>;
    let service: EPreguntaAbiertaRespuestaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EPreguntaAbiertaRespuestaComponent],
      })
        .overrideTemplate(EPreguntaAbiertaRespuestaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EPreguntaAbiertaRespuestaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EPreguntaAbiertaRespuestaService);

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
      expect(comp.ePreguntaAbiertaRespuestas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

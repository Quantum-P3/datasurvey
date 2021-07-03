import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PPreguntaAbiertaService } from '../service/p-pregunta-abierta.service';

import { PPreguntaAbiertaComponent } from './p-pregunta-abierta.component';

describe('Component Tests', () => {
  describe('PPreguntaAbierta Management Component', () => {
    let comp: PPreguntaAbiertaComponent;
    let fixture: ComponentFixture<PPreguntaAbiertaComponent>;
    let service: PPreguntaAbiertaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PPreguntaAbiertaComponent],
      })
        .overrideTemplate(PPreguntaAbiertaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PPreguntaAbiertaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PPreguntaAbiertaService);

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
      expect(comp.pPreguntaAbiertas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EncuestaService } from '../service/encuesta.service';

import { EncuestaComponent } from './encuesta.component';

describe('Component Tests', () => {
  describe('Encuesta Management Component', () => {
    let comp: EncuestaComponent;
    let fixture: ComponentFixture<EncuestaComponent>;
    let service: EncuestaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EncuestaComponent],
      })
        .overrideTemplate(EncuestaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EncuestaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EncuestaService);

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
      expect(comp.encuestas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

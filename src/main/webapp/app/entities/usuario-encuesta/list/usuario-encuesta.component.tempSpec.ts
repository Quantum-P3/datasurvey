import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { UsuarioEncuestaService } from '../service/usuario-encuesta.service';

import { UsuarioEncuestaComponent } from './usuario-encuesta.component';

describe('Component Tests', () => {
  describe('UsuarioEncuesta Management Component', () => {
    let comp: UsuarioEncuestaComponent;
    let fixture: ComponentFixture<UsuarioEncuestaComponent>;
    let service: UsuarioEncuestaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UsuarioEncuestaComponent],
      })
        .overrideTemplate(UsuarioEncuestaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UsuarioEncuestaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(UsuarioEncuestaService);

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
      expect(comp.usuarioEncuestas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

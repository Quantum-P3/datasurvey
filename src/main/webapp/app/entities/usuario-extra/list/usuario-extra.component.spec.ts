import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { UsuarioExtraService } from '../service/usuario-extra.service';

import { UsuarioExtraComponent } from './usuario-extra.component';

describe('Component Tests', () => {
  describe('UsuarioExtra Management Component', () => {
    let comp: UsuarioExtraComponent;
    let fixture: ComponentFixture<UsuarioExtraComponent>;
    let service: UsuarioExtraService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [UsuarioExtraComponent],
      })
        .overrideTemplate(UsuarioExtraComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UsuarioExtraComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(UsuarioExtraService);

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
      expect(comp.usuarioExtras?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

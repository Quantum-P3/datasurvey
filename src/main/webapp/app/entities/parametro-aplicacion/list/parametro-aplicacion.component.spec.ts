import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ParametroAplicacionService } from '../service/parametro-aplicacion.service';

import { ParametroAplicacionComponent } from './parametro-aplicacion.component';

describe('Component Tests', () => {
  describe('ParametroAplicacion Management Component', () => {
    let comp: ParametroAplicacionComponent;
    let fixture: ComponentFixture<ParametroAplicacionComponent>;
    let service: ParametroAplicacionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ParametroAplicacionComponent],
      })
        .overrideTemplate(ParametroAplicacionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ParametroAplicacionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ParametroAplicacionService);

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
      expect(comp.parametroAplicacions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

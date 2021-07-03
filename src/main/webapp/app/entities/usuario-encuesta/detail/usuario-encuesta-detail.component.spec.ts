import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UsuarioEncuestaDetailComponent } from './usuario-encuesta-detail.component';

describe('Component Tests', () => {
  describe('UsuarioEncuesta Management Detail Component', () => {
    let comp: UsuarioEncuestaDetailComponent;
    let fixture: ComponentFixture<UsuarioEncuestaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UsuarioEncuestaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ usuarioEncuesta: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UsuarioEncuestaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UsuarioEncuestaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load usuarioEncuesta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.usuarioEncuesta).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

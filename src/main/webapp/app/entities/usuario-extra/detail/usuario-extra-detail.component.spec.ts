import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UsuarioExtraDetailComponent } from './usuario-extra-detail.component';

describe('Component Tests', () => {
  describe('UsuarioExtra Management Detail Component', () => {
    let comp: UsuarioExtraDetailComponent;
    let fixture: ComponentFixture<UsuarioExtraDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UsuarioExtraDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ usuarioExtra: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UsuarioExtraDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UsuarioExtraDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load usuarioExtra on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.usuarioExtra).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

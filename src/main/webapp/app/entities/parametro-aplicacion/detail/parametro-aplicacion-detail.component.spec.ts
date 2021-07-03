import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ParametroAplicacionDetailComponent } from './parametro-aplicacion-detail.component';

describe('Component Tests', () => {
  describe('ParametroAplicacion Management Detail Component', () => {
    let comp: ParametroAplicacionDetailComponent;
    let fixture: ComponentFixture<ParametroAplicacionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ParametroAplicacionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ parametroAplicacion: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ParametroAplicacionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParametroAplicacionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load parametroAplicacion on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.parametroAplicacion).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

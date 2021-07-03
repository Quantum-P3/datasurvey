import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EPreguntaCerradaOpcionDetailComponent } from './e-pregunta-cerrada-opcion-detail.component';

describe('Component Tests', () => {
  describe('EPreguntaCerradaOpcion Management Detail Component', () => {
    let comp: EPreguntaCerradaOpcionDetailComponent;
    let fixture: ComponentFixture<EPreguntaCerradaOpcionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EPreguntaCerradaOpcionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ePreguntaCerradaOpcion: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EPreguntaCerradaOpcionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EPreguntaCerradaOpcionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ePreguntaCerradaOpcion on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ePreguntaCerradaOpcion).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

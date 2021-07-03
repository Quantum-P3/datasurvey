import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EPreguntaCerradaDetailComponent } from './e-pregunta-cerrada-detail.component';

describe('Component Tests', () => {
  describe('EPreguntaCerrada Management Detail Component', () => {
    let comp: EPreguntaCerradaDetailComponent;
    let fixture: ComponentFixture<EPreguntaCerradaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EPreguntaCerradaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ePreguntaCerrada: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EPreguntaCerradaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EPreguntaCerradaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ePreguntaCerrada on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ePreguntaCerrada).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

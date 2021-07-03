import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PPreguntaCerradaDetailComponent } from './p-pregunta-cerrada-detail.component';

describe('Component Tests', () => {
  describe('PPreguntaCerrada Management Detail Component', () => {
    let comp: PPreguntaCerradaDetailComponent;
    let fixture: ComponentFixture<PPreguntaCerradaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PPreguntaCerradaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pPreguntaCerrada: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PPreguntaCerradaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PPreguntaCerradaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pPreguntaCerrada on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pPreguntaCerrada).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

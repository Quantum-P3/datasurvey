import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PPreguntaCerradaOpcionDetailComponent } from './p-pregunta-cerrada-opcion-detail.component';

describe('Component Tests', () => {
  describe('PPreguntaCerradaOpcion Management Detail Component', () => {
    let comp: PPreguntaCerradaOpcionDetailComponent;
    let fixture: ComponentFixture<PPreguntaCerradaOpcionDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PPreguntaCerradaOpcionDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pPreguntaCerradaOpcion: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PPreguntaCerradaOpcionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PPreguntaCerradaOpcionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pPreguntaCerradaOpcion on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pPreguntaCerradaOpcion).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

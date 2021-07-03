import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EPreguntaAbiertaDetailComponent } from './e-pregunta-abierta-detail.component';

describe('Component Tests', () => {
  describe('EPreguntaAbierta Management Detail Component', () => {
    let comp: EPreguntaAbiertaDetailComponent;
    let fixture: ComponentFixture<EPreguntaAbiertaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EPreguntaAbiertaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ePreguntaAbierta: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EPreguntaAbiertaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EPreguntaAbiertaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ePreguntaAbierta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ePreguntaAbierta).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

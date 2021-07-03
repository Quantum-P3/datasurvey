import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PPreguntaAbiertaDetailComponent } from './p-pregunta-abierta-detail.component';

describe('Component Tests', () => {
  describe('PPreguntaAbierta Management Detail Component', () => {
    let comp: PPreguntaAbiertaDetailComponent;
    let fixture: ComponentFixture<PPreguntaAbiertaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PPreguntaAbiertaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pPreguntaAbierta: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PPreguntaAbiertaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PPreguntaAbiertaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pPreguntaAbierta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pPreguntaAbierta).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EPreguntaAbiertaRespuestaDetailComponent } from './e-pregunta-abierta-respuesta-detail.component';

describe('Component Tests', () => {
  describe('EPreguntaAbiertaRespuesta Management Detail Component', () => {
    let comp: EPreguntaAbiertaRespuestaDetailComponent;
    let fixture: ComponentFixture<EPreguntaAbiertaRespuestaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EPreguntaAbiertaRespuestaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ ePreguntaAbiertaRespuesta: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EPreguntaAbiertaRespuestaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EPreguntaAbiertaRespuestaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ePreguntaAbiertaRespuesta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ePreguntaAbiertaRespuesta).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

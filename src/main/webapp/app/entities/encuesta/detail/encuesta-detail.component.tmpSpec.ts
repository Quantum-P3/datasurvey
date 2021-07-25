import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EncuestaDetailComponent } from './encuesta-detail.component';

describe('Component Tests', () => {
  describe('Encuesta Management Detail Component', () => {
    let comp: EncuestaDetailComponent;
    let fixture: ComponentFixture<EncuestaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EncuestaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ encuesta: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EncuestaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EncuestaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load encuesta on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.encuesta).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

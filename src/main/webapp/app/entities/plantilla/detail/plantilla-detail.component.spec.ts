import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlantillaDetailComponent } from './plantilla-detail.component';

describe('Component Tests', () => {
  describe('Plantilla Management Detail Component', () => {
    let comp: PlantillaDetailComponent;
    let fixture: ComponentFixture<PlantillaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PlantillaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ plantilla: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PlantillaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlantillaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load plantilla on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.plantilla).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});

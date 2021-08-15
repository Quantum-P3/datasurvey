import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { UsuarioPlantillasDetailComponent } from './usuario-plantillas-detail.component';

describe('Component Tests', () => {
  describe('Plantilla Management Detail Component', () => {
    let comp: UsuarioPlantillasDetailComponent;
    let fixture: ComponentFixture<UsuarioPlantillasDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [UsuarioPlantillasDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ plantilla: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(UsuarioPlantillasDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UsuarioPlantillasDetailComponent);
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

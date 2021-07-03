import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PlantillaService } from '../service/plantilla.service';

import { PlantillaComponent } from './plantilla.component';

describe('Component Tests', () => {
  describe('Plantilla Management Component', () => {
    let comp: PlantillaComponent;
    let fixture: ComponentFixture<PlantillaComponent>;
    let service: PlantillaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlantillaComponent],
      })
        .overrideTemplate(PlantillaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlantillaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PlantillaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.plantillas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

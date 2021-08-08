import { Plantilla } from '../plantilla.model';
import { ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PlantillaService } from '../service/plantilla.service';

import { PlantillaDeleteDialogComponent } from './plantilla-delete-dialog.component';
import { EstadoPlantilla } from '../../enumerations/estado-plantilla.model';

jest.mock('@ng-bootstrap/ng-bootstrap');

describe('Component Tests', () => {
  describe('Plantilla Management Delete Component', () => {
    let comp: PlantillaDeleteDialogComponent;
    let fixture: ComponentFixture<PlantillaDeleteDialogComponent>;
    let service: PlantillaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlantillaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PlantillaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlantillaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PlantillaService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));
          const pPlantilla = new Plantilla();

          pPlantilla.id = 123;
          pPlantilla.estado = EstadoPlantilla.DELETED;
          // WHEN
          comp.confirmDelete(pPlantilla);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});

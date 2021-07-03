jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ParametroAplicacionService } from '../service/parametro-aplicacion.service';

import { ParametroAplicacionDeleteDialogComponent } from './parametro-aplicacion-delete-dialog.component';

describe('Component Tests', () => {
  describe('ParametroAplicacion Management Delete Component', () => {
    let comp: ParametroAplicacionDeleteDialogComponent;
    let fixture: ComponentFixture<ParametroAplicacionDeleteDialogComponent>;
    let service: ParametroAplicacionService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ParametroAplicacionDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ParametroAplicacionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ParametroAplicacionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ParametroAplicacionService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
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

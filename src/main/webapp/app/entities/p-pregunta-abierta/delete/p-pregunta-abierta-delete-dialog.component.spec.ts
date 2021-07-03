jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PPreguntaAbiertaService } from '../service/p-pregunta-abierta.service';

import { PPreguntaAbiertaDeleteDialogComponent } from './p-pregunta-abierta-delete-dialog.component';

describe('Component Tests', () => {
  describe('PPreguntaAbierta Management Delete Component', () => {
    let comp: PPreguntaAbiertaDeleteDialogComponent;
    let fixture: ComponentFixture<PPreguntaAbiertaDeleteDialogComponent>;
    let service: PPreguntaAbiertaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PPreguntaAbiertaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PPreguntaAbiertaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PPreguntaAbiertaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PPreguntaAbiertaService);
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

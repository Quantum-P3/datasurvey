jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EPreguntaAbiertaRespuestaService } from '../service/e-pregunta-abierta-respuesta.service';

import { EPreguntaAbiertaRespuestaDeleteDialogComponent } from './e-pregunta-abierta-respuesta-delete-dialog.component';

describe('Component Tests', () => {
  describe('EPreguntaAbiertaRespuesta Management Delete Component', () => {
    let comp: EPreguntaAbiertaRespuestaDeleteDialogComponent;
    let fixture: ComponentFixture<EPreguntaAbiertaRespuestaDeleteDialogComponent>;
    let service: EPreguntaAbiertaRespuestaService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EPreguntaAbiertaRespuestaDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(EPreguntaAbiertaRespuestaDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EPreguntaAbiertaRespuestaDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EPreguntaAbiertaRespuestaService);
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

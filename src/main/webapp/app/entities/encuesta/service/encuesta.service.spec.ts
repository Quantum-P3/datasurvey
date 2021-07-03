import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { AccesoEncuesta } from 'app/entities/enumerations/acceso-encuesta.model';
import { EstadoEncuesta } from 'app/entities/enumerations/estado-encuesta.model';
import { IEncuesta, Encuesta } from '../encuesta.model';

import { EncuestaService } from './encuesta.service';

describe('Service Tests', () => {
  describe('Encuesta Service', () => {
    let service: EncuestaService;
    let httpMock: HttpTestingController;
    let elemDefault: IEncuesta;
    let expectedResult: IEncuesta | IEncuesta[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EncuestaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        descripcion: 'AAAAAAA',
        fechaCreacion: currentDate,
        fechaPublicacion: currentDate,
        fechaFinalizar: currentDate,
        fechaFinalizada: currentDate,
        calificacion: 0,
        acceso: AccesoEncuesta.PUBLIC,
        contrasenna: 'AAAAAAA',
        estado: EstadoEncuesta.DRAFT,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaPublicacion: currentDate.format(DATE_TIME_FORMAT),
            fechaFinalizar: currentDate.format(DATE_TIME_FORMAT),
            fechaFinalizada: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Encuesta', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaPublicacion: currentDate.format(DATE_TIME_FORMAT),
            fechaFinalizar: currentDate.format(DATE_TIME_FORMAT),
            fechaFinalizada: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaCreacion: currentDate,
            fechaPublicacion: currentDate,
            fechaFinalizar: currentDate,
            fechaFinalizada: currentDate,
          },
          returnedFromService
        );

        service.create(new Encuesta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Encuesta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            descripcion: 'BBBBBB',
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaPublicacion: currentDate.format(DATE_TIME_FORMAT),
            fechaFinalizar: currentDate.format(DATE_TIME_FORMAT),
            fechaFinalizada: currentDate.format(DATE_TIME_FORMAT),
            calificacion: 1,
            acceso: 'BBBBBB',
            contrasenna: 'BBBBBB',
            estado: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaCreacion: currentDate,
            fechaPublicacion: currentDate,
            fechaFinalizar: currentDate,
            fechaFinalizada: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Encuesta', () => {
        const patchObject = Object.assign(
          {
            nombre: 'BBBBBB',
            descripcion: 'BBBBBB',
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaPublicacion: currentDate.format(DATE_TIME_FORMAT),
          },
          new Encuesta()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaCreacion: currentDate,
            fechaPublicacion: currentDate,
            fechaFinalizar: currentDate,
            fechaFinalizada: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Encuesta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            descripcion: 'BBBBBB',
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaPublicacion: currentDate.format(DATE_TIME_FORMAT),
            fechaFinalizar: currentDate.format(DATE_TIME_FORMAT),
            fechaFinalizada: currentDate.format(DATE_TIME_FORMAT),
            calificacion: 1,
            acceso: 'BBBBBB',
            contrasenna: 'BBBBBB',
            estado: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaCreacion: currentDate,
            fechaPublicacion: currentDate,
            fechaFinalizar: currentDate,
            fechaFinalizada: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Encuesta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEncuestaToCollectionIfMissing', () => {
        it('should add a Encuesta to an empty array', () => {
          const encuesta: IEncuesta = { id: 123 };
          expectedResult = service.addEncuestaToCollectionIfMissing([], encuesta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(encuesta);
        });

        it('should not add a Encuesta to an array that contains it', () => {
          const encuesta: IEncuesta = { id: 123 };
          const encuestaCollection: IEncuesta[] = [
            {
              ...encuesta,
            },
            { id: 456 },
          ];
          expectedResult = service.addEncuestaToCollectionIfMissing(encuestaCollection, encuesta);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Encuesta to an array that doesn't contain it", () => {
          const encuesta: IEncuesta = { id: 123 };
          const encuestaCollection: IEncuesta[] = [{ id: 456 }];
          expectedResult = service.addEncuestaToCollectionIfMissing(encuestaCollection, encuesta);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(encuesta);
        });

        it('should add only unique Encuesta to an array', () => {
          const encuestaArray: IEncuesta[] = [{ id: 123 }, { id: 456 }, { id: 68819 }];
          const encuestaCollection: IEncuesta[] = [{ id: 123 }];
          expectedResult = service.addEncuestaToCollectionIfMissing(encuestaCollection, ...encuestaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const encuesta: IEncuesta = { id: 123 };
          const encuesta2: IEncuesta = { id: 456 };
          expectedResult = service.addEncuestaToCollectionIfMissing([], encuesta, encuesta2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(encuesta);
          expect(expectedResult).toContain(encuesta2);
        });

        it('should accept null and undefined values', () => {
          const encuesta: IEncuesta = { id: 123 };
          expectedResult = service.addEncuestaToCollectionIfMissing([], null, encuesta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(encuesta);
        });

        it('should return initial array if no Encuesta is added', () => {
          const encuestaCollection: IEncuesta[] = [{ id: 123 }];
          expectedResult = service.addEncuestaToCollectionIfMissing(encuestaCollection, undefined, null);
          expect(expectedResult).toEqual(encuestaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

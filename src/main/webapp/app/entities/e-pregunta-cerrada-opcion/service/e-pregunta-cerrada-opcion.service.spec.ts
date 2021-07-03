import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEPreguntaCerradaOpcion, EPreguntaCerradaOpcion } from '../e-pregunta-cerrada-opcion.model';

import { EPreguntaCerradaOpcionService } from './e-pregunta-cerrada-opcion.service';

describe('Service Tests', () => {
  describe('EPreguntaCerradaOpcion Service', () => {
    let service: EPreguntaCerradaOpcionService;
    let httpMock: HttpTestingController;
    let elemDefault: IEPreguntaCerradaOpcion;
    let expectedResult: IEPreguntaCerradaOpcion | IEPreguntaCerradaOpcion[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EPreguntaCerradaOpcionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        orden: 0,
        cantidad: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a EPreguntaCerradaOpcion', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new EPreguntaCerradaOpcion()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EPreguntaCerradaOpcion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            orden: 1,
            cantidad: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a EPreguntaCerradaOpcion', () => {
        const patchObject = Object.assign(
          {
            nombre: 'BBBBBB',
            orden: 1,
          },
          new EPreguntaCerradaOpcion()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EPreguntaCerradaOpcion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            orden: 1,
            cantidad: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a EPreguntaCerradaOpcion', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEPreguntaCerradaOpcionToCollectionIfMissing', () => {
        it('should add a EPreguntaCerradaOpcion to an empty array', () => {
          const ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion = { id: 123 };
          expectedResult = service.addEPreguntaCerradaOpcionToCollectionIfMissing([], ePreguntaCerradaOpcion);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ePreguntaCerradaOpcion);
        });

        it('should not add a EPreguntaCerradaOpcion to an array that contains it', () => {
          const ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion = { id: 123 };
          const ePreguntaCerradaOpcionCollection: IEPreguntaCerradaOpcion[] = [
            {
              ...ePreguntaCerradaOpcion,
            },
            { id: 456 },
          ];
          expectedResult = service.addEPreguntaCerradaOpcionToCollectionIfMissing(ePreguntaCerradaOpcionCollection, ePreguntaCerradaOpcion);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EPreguntaCerradaOpcion to an array that doesn't contain it", () => {
          const ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion = { id: 123 };
          const ePreguntaCerradaOpcionCollection: IEPreguntaCerradaOpcion[] = [{ id: 456 }];
          expectedResult = service.addEPreguntaCerradaOpcionToCollectionIfMissing(ePreguntaCerradaOpcionCollection, ePreguntaCerradaOpcion);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ePreguntaCerradaOpcion);
        });

        it('should add only unique EPreguntaCerradaOpcion to an array', () => {
          const ePreguntaCerradaOpcionArray: IEPreguntaCerradaOpcion[] = [{ id: 123 }, { id: 456 }, { id: 80190 }];
          const ePreguntaCerradaOpcionCollection: IEPreguntaCerradaOpcion[] = [{ id: 123 }];
          expectedResult = service.addEPreguntaCerradaOpcionToCollectionIfMissing(
            ePreguntaCerradaOpcionCollection,
            ...ePreguntaCerradaOpcionArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion = { id: 123 };
          const ePreguntaCerradaOpcion2: IEPreguntaCerradaOpcion = { id: 456 };
          expectedResult = service.addEPreguntaCerradaOpcionToCollectionIfMissing([], ePreguntaCerradaOpcion, ePreguntaCerradaOpcion2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ePreguntaCerradaOpcion);
          expect(expectedResult).toContain(ePreguntaCerradaOpcion2);
        });

        it('should accept null and undefined values', () => {
          const ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion = { id: 123 };
          expectedResult = service.addEPreguntaCerradaOpcionToCollectionIfMissing([], null, ePreguntaCerradaOpcion, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ePreguntaCerradaOpcion);
        });

        it('should return initial array if no EPreguntaCerradaOpcion is added', () => {
          const ePreguntaCerradaOpcionCollection: IEPreguntaCerradaOpcion[] = [{ id: 123 }];
          expectedResult = service.addEPreguntaCerradaOpcionToCollectionIfMissing(ePreguntaCerradaOpcionCollection, undefined, null);
          expect(expectedResult).toEqual(ePreguntaCerradaOpcionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

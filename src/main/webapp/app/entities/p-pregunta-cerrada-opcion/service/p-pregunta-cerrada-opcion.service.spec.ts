import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPPreguntaCerradaOpcion, PPreguntaCerradaOpcion } from '../p-pregunta-cerrada-opcion.model';

import { PPreguntaCerradaOpcionService } from './p-pregunta-cerrada-opcion.service';

describe('Service Tests', () => {
  describe('PPreguntaCerradaOpcion Service', () => {
    let service: PPreguntaCerradaOpcionService;
    let httpMock: HttpTestingController;
    let elemDefault: IPPreguntaCerradaOpcion;
    let expectedResult: IPPreguntaCerradaOpcion | IPPreguntaCerradaOpcion[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PPreguntaCerradaOpcionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        orden: 0,
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

      it('should create a PPreguntaCerradaOpcion', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PPreguntaCerradaOpcion()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PPreguntaCerradaOpcion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            orden: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PPreguntaCerradaOpcion', () => {
        const patchObject = Object.assign(
          {
            nombre: 'BBBBBB',
          },
          new PPreguntaCerradaOpcion()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PPreguntaCerradaOpcion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            orden: 1,
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

      it('should delete a PPreguntaCerradaOpcion', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPPreguntaCerradaOpcionToCollectionIfMissing', () => {
        it('should add a PPreguntaCerradaOpcion to an empty array', () => {
          const pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion = { id: 123 };
          expectedResult = service.addPPreguntaCerradaOpcionToCollectionIfMissing([], pPreguntaCerradaOpcion);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pPreguntaCerradaOpcion);
        });

        it('should not add a PPreguntaCerradaOpcion to an array that contains it', () => {
          const pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion = { id: 123 };
          const pPreguntaCerradaOpcionCollection: IPPreguntaCerradaOpcion[] = [
            {
              ...pPreguntaCerradaOpcion,
            },
            { id: 456 },
          ];
          expectedResult = service.addPPreguntaCerradaOpcionToCollectionIfMissing(pPreguntaCerradaOpcionCollection, pPreguntaCerradaOpcion);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PPreguntaCerradaOpcion to an array that doesn't contain it", () => {
          const pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion = { id: 123 };
          const pPreguntaCerradaOpcionCollection: IPPreguntaCerradaOpcion[] = [{ id: 456 }];
          expectedResult = service.addPPreguntaCerradaOpcionToCollectionIfMissing(pPreguntaCerradaOpcionCollection, pPreguntaCerradaOpcion);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pPreguntaCerradaOpcion);
        });

        it('should add only unique PPreguntaCerradaOpcion to an array', () => {
          const pPreguntaCerradaOpcionArray: IPPreguntaCerradaOpcion[] = [{ id: 123 }, { id: 456 }, { id: 45750 }];
          const pPreguntaCerradaOpcionCollection: IPPreguntaCerradaOpcion[] = [{ id: 123 }];
          expectedResult = service.addPPreguntaCerradaOpcionToCollectionIfMissing(
            pPreguntaCerradaOpcionCollection,
            ...pPreguntaCerradaOpcionArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion = { id: 123 };
          const pPreguntaCerradaOpcion2: IPPreguntaCerradaOpcion = { id: 456 };
          expectedResult = service.addPPreguntaCerradaOpcionToCollectionIfMissing([], pPreguntaCerradaOpcion, pPreguntaCerradaOpcion2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pPreguntaCerradaOpcion);
          expect(expectedResult).toContain(pPreguntaCerradaOpcion2);
        });

        it('should accept null and undefined values', () => {
          const pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion = { id: 123 };
          expectedResult = service.addPPreguntaCerradaOpcionToCollectionIfMissing([], null, pPreguntaCerradaOpcion, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pPreguntaCerradaOpcion);
        });

        it('should return initial array if no PPreguntaCerradaOpcion is added', () => {
          const pPreguntaCerradaOpcionCollection: IPPreguntaCerradaOpcion[] = [{ id: 123 }];
          expectedResult = service.addPPreguntaCerradaOpcionToCollectionIfMissing(pPreguntaCerradaOpcionCollection, undefined, null);
          expect(expectedResult).toEqual(pPreguntaCerradaOpcionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

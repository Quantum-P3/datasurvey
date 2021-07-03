import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEPreguntaAbierta, EPreguntaAbierta } from '../e-pregunta-abierta.model';

import { EPreguntaAbiertaService } from './e-pregunta-abierta.service';

describe('Service Tests', () => {
  describe('EPreguntaAbierta Service', () => {
    let service: EPreguntaAbiertaService;
    let httpMock: HttpTestingController;
    let elemDefault: IEPreguntaAbierta;
    let expectedResult: IEPreguntaAbierta | IEPreguntaAbierta[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EPreguntaAbiertaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        opcional: false,
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

      it('should create a EPreguntaAbierta', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new EPreguntaAbierta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EPreguntaAbierta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            opcional: true,
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

      it('should partial update a EPreguntaAbierta', () => {
        const patchObject = Object.assign(
          {
            nombre: 'BBBBBB',
            opcional: true,
            orden: 1,
          },
          new EPreguntaAbierta()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EPreguntaAbierta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            opcional: true,
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

      it('should delete a EPreguntaAbierta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEPreguntaAbiertaToCollectionIfMissing', () => {
        it('should add a EPreguntaAbierta to an empty array', () => {
          const ePreguntaAbierta: IEPreguntaAbierta = { id: 123 };
          expectedResult = service.addEPreguntaAbiertaToCollectionIfMissing([], ePreguntaAbierta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ePreguntaAbierta);
        });

        it('should not add a EPreguntaAbierta to an array that contains it', () => {
          const ePreguntaAbierta: IEPreguntaAbierta = { id: 123 };
          const ePreguntaAbiertaCollection: IEPreguntaAbierta[] = [
            {
              ...ePreguntaAbierta,
            },
            { id: 456 },
          ];
          expectedResult = service.addEPreguntaAbiertaToCollectionIfMissing(ePreguntaAbiertaCollection, ePreguntaAbierta);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EPreguntaAbierta to an array that doesn't contain it", () => {
          const ePreguntaAbierta: IEPreguntaAbierta = { id: 123 };
          const ePreguntaAbiertaCollection: IEPreguntaAbierta[] = [{ id: 456 }];
          expectedResult = service.addEPreguntaAbiertaToCollectionIfMissing(ePreguntaAbiertaCollection, ePreguntaAbierta);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ePreguntaAbierta);
        });

        it('should add only unique EPreguntaAbierta to an array', () => {
          const ePreguntaAbiertaArray: IEPreguntaAbierta[] = [{ id: 123 }, { id: 456 }, { id: 51823 }];
          const ePreguntaAbiertaCollection: IEPreguntaAbierta[] = [{ id: 123 }];
          expectedResult = service.addEPreguntaAbiertaToCollectionIfMissing(ePreguntaAbiertaCollection, ...ePreguntaAbiertaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ePreguntaAbierta: IEPreguntaAbierta = { id: 123 };
          const ePreguntaAbierta2: IEPreguntaAbierta = { id: 456 };
          expectedResult = service.addEPreguntaAbiertaToCollectionIfMissing([], ePreguntaAbierta, ePreguntaAbierta2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ePreguntaAbierta);
          expect(expectedResult).toContain(ePreguntaAbierta2);
        });

        it('should accept null and undefined values', () => {
          const ePreguntaAbierta: IEPreguntaAbierta = { id: 123 };
          expectedResult = service.addEPreguntaAbiertaToCollectionIfMissing([], null, ePreguntaAbierta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ePreguntaAbierta);
        });

        it('should return initial array if no EPreguntaAbierta is added', () => {
          const ePreguntaAbiertaCollection: IEPreguntaAbierta[] = [{ id: 123 }];
          expectedResult = service.addEPreguntaAbiertaToCollectionIfMissing(ePreguntaAbiertaCollection, undefined, null);
          expect(expectedResult).toEqual(ePreguntaAbiertaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

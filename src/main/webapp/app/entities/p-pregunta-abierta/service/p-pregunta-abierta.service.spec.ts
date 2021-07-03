import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPPreguntaAbierta, PPreguntaAbierta } from '../p-pregunta-abierta.model';

import { PPreguntaAbiertaService } from './p-pregunta-abierta.service';

describe('Service Tests', () => {
  describe('PPreguntaAbierta Service', () => {
    let service: PPreguntaAbiertaService;
    let httpMock: HttpTestingController;
    let elemDefault: IPPreguntaAbierta;
    let expectedResult: IPPreguntaAbierta | IPPreguntaAbierta[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PPreguntaAbiertaService);
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

      it('should create a PPreguntaAbierta', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PPreguntaAbierta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PPreguntaAbierta', () => {
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

      it('should partial update a PPreguntaAbierta', () => {
        const patchObject = Object.assign(
          {
            nombre: 'BBBBBB',
          },
          new PPreguntaAbierta()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PPreguntaAbierta', () => {
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

      it('should delete a PPreguntaAbierta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPPreguntaAbiertaToCollectionIfMissing', () => {
        it('should add a PPreguntaAbierta to an empty array', () => {
          const pPreguntaAbierta: IPPreguntaAbierta = { id: 123 };
          expectedResult = service.addPPreguntaAbiertaToCollectionIfMissing([], pPreguntaAbierta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pPreguntaAbierta);
        });

        it('should not add a PPreguntaAbierta to an array that contains it', () => {
          const pPreguntaAbierta: IPPreguntaAbierta = { id: 123 };
          const pPreguntaAbiertaCollection: IPPreguntaAbierta[] = [
            {
              ...pPreguntaAbierta,
            },
            { id: 456 },
          ];
          expectedResult = service.addPPreguntaAbiertaToCollectionIfMissing(pPreguntaAbiertaCollection, pPreguntaAbierta);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PPreguntaAbierta to an array that doesn't contain it", () => {
          const pPreguntaAbierta: IPPreguntaAbierta = { id: 123 };
          const pPreguntaAbiertaCollection: IPPreguntaAbierta[] = [{ id: 456 }];
          expectedResult = service.addPPreguntaAbiertaToCollectionIfMissing(pPreguntaAbiertaCollection, pPreguntaAbierta);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pPreguntaAbierta);
        });

        it('should add only unique PPreguntaAbierta to an array', () => {
          const pPreguntaAbiertaArray: IPPreguntaAbierta[] = [{ id: 123 }, { id: 456 }, { id: 34879 }];
          const pPreguntaAbiertaCollection: IPPreguntaAbierta[] = [{ id: 123 }];
          expectedResult = service.addPPreguntaAbiertaToCollectionIfMissing(pPreguntaAbiertaCollection, ...pPreguntaAbiertaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const pPreguntaAbierta: IPPreguntaAbierta = { id: 123 };
          const pPreguntaAbierta2: IPPreguntaAbierta = { id: 456 };
          expectedResult = service.addPPreguntaAbiertaToCollectionIfMissing([], pPreguntaAbierta, pPreguntaAbierta2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pPreguntaAbierta);
          expect(expectedResult).toContain(pPreguntaAbierta2);
        });

        it('should accept null and undefined values', () => {
          const pPreguntaAbierta: IPPreguntaAbierta = { id: 123 };
          expectedResult = service.addPPreguntaAbiertaToCollectionIfMissing([], null, pPreguntaAbierta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pPreguntaAbierta);
        });

        it('should return initial array if no PPreguntaAbierta is added', () => {
          const pPreguntaAbiertaCollection: IPPreguntaAbierta[] = [{ id: 123 }];
          expectedResult = service.addPPreguntaAbiertaToCollectionIfMissing(pPreguntaAbiertaCollection, undefined, null);
          expect(expectedResult).toEqual(pPreguntaAbiertaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

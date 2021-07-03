import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PreguntaCerradaTipo } from 'app/entities/enumerations/pregunta-cerrada-tipo.model';
import { IPPreguntaCerrada, PPreguntaCerrada } from '../p-pregunta-cerrada.model';

import { PPreguntaCerradaService } from './p-pregunta-cerrada.service';

describe('Service Tests', () => {
  describe('PPreguntaCerrada Service', () => {
    let service: PPreguntaCerradaService;
    let httpMock: HttpTestingController;
    let elemDefault: IPPreguntaCerrada;
    let expectedResult: IPPreguntaCerrada | IPPreguntaCerrada[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PPreguntaCerradaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        tipo: PreguntaCerradaTipo.SINGLE,
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

      it('should create a PPreguntaCerrada', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PPreguntaCerrada()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PPreguntaCerrada', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            tipo: 'BBBBBB',
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

      it('should partial update a PPreguntaCerrada', () => {
        const patchObject = Object.assign(
          {
            nombre: 'BBBBBB',
            tipo: 'BBBBBB',
            opcional: true,
          },
          new PPreguntaCerrada()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PPreguntaCerrada', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            tipo: 'BBBBBB',
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

      it('should delete a PPreguntaCerrada', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPPreguntaCerradaToCollectionIfMissing', () => {
        it('should add a PPreguntaCerrada to an empty array', () => {
          const pPreguntaCerrada: IPPreguntaCerrada = { id: 123 };
          expectedResult = service.addPPreguntaCerradaToCollectionIfMissing([], pPreguntaCerrada);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pPreguntaCerrada);
        });

        it('should not add a PPreguntaCerrada to an array that contains it', () => {
          const pPreguntaCerrada: IPPreguntaCerrada = { id: 123 };
          const pPreguntaCerradaCollection: IPPreguntaCerrada[] = [
            {
              ...pPreguntaCerrada,
            },
            { id: 456 },
          ];
          expectedResult = service.addPPreguntaCerradaToCollectionIfMissing(pPreguntaCerradaCollection, pPreguntaCerrada);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PPreguntaCerrada to an array that doesn't contain it", () => {
          const pPreguntaCerrada: IPPreguntaCerrada = { id: 123 };
          const pPreguntaCerradaCollection: IPPreguntaCerrada[] = [{ id: 456 }];
          expectedResult = service.addPPreguntaCerradaToCollectionIfMissing(pPreguntaCerradaCollection, pPreguntaCerrada);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pPreguntaCerrada);
        });

        it('should add only unique PPreguntaCerrada to an array', () => {
          const pPreguntaCerradaArray: IPPreguntaCerrada[] = [{ id: 123 }, { id: 456 }, { id: 39367 }];
          const pPreguntaCerradaCollection: IPPreguntaCerrada[] = [{ id: 123 }];
          expectedResult = service.addPPreguntaCerradaToCollectionIfMissing(pPreguntaCerradaCollection, ...pPreguntaCerradaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const pPreguntaCerrada: IPPreguntaCerrada = { id: 123 };
          const pPreguntaCerrada2: IPPreguntaCerrada = { id: 456 };
          expectedResult = service.addPPreguntaCerradaToCollectionIfMissing([], pPreguntaCerrada, pPreguntaCerrada2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(pPreguntaCerrada);
          expect(expectedResult).toContain(pPreguntaCerrada2);
        });

        it('should accept null and undefined values', () => {
          const pPreguntaCerrada: IPPreguntaCerrada = { id: 123 };
          expectedResult = service.addPPreguntaCerradaToCollectionIfMissing([], null, pPreguntaCerrada, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(pPreguntaCerrada);
        });

        it('should return initial array if no PPreguntaCerrada is added', () => {
          const pPreguntaCerradaCollection: IPPreguntaCerrada[] = [{ id: 123 }];
          expectedResult = service.addPPreguntaCerradaToCollectionIfMissing(pPreguntaCerradaCollection, undefined, null);
          expect(expectedResult).toEqual(pPreguntaCerradaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PreguntaCerradaTipo } from 'app/entities/enumerations/pregunta-cerrada-tipo.model';
import { IEPreguntaCerrada, EPreguntaCerrada } from '../e-pregunta-cerrada.model';

import { EPreguntaCerradaService } from './e-pregunta-cerrada.service';

describe('Service Tests', () => {
  describe('EPreguntaCerrada Service', () => {
    let service: EPreguntaCerradaService;
    let httpMock: HttpTestingController;
    let elemDefault: IEPreguntaCerrada;
    let expectedResult: IEPreguntaCerrada | IEPreguntaCerrada[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EPreguntaCerradaService);
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

      it('should create a EPreguntaCerrada', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new EPreguntaCerrada()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EPreguntaCerrada', () => {
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

      it('should partial update a EPreguntaCerrada', () => {
        const patchObject = Object.assign(
          {
            opcional: true,
          },
          new EPreguntaCerrada()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EPreguntaCerrada', () => {
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

      it('should delete a EPreguntaCerrada', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEPreguntaCerradaToCollectionIfMissing', () => {
        it('should add a EPreguntaCerrada to an empty array', () => {
          const ePreguntaCerrada: IEPreguntaCerrada = { id: 123 };
          expectedResult = service.addEPreguntaCerradaToCollectionIfMissing([], ePreguntaCerrada);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ePreguntaCerrada);
        });

        it('should not add a EPreguntaCerrada to an array that contains it', () => {
          const ePreguntaCerrada: IEPreguntaCerrada = { id: 123 };
          const ePreguntaCerradaCollection: IEPreguntaCerrada[] = [
            {
              ...ePreguntaCerrada,
            },
            { id: 456 },
          ];
          expectedResult = service.addEPreguntaCerradaToCollectionIfMissing(ePreguntaCerradaCollection, ePreguntaCerrada);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EPreguntaCerrada to an array that doesn't contain it", () => {
          const ePreguntaCerrada: IEPreguntaCerrada = { id: 123 };
          const ePreguntaCerradaCollection: IEPreguntaCerrada[] = [{ id: 456 }];
          expectedResult = service.addEPreguntaCerradaToCollectionIfMissing(ePreguntaCerradaCollection, ePreguntaCerrada);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ePreguntaCerrada);
        });

        it('should add only unique EPreguntaCerrada to an array', () => {
          const ePreguntaCerradaArray: IEPreguntaCerrada[] = [{ id: 123 }, { id: 456 }, { id: 98164 }];
          const ePreguntaCerradaCollection: IEPreguntaCerrada[] = [{ id: 123 }];
          expectedResult = service.addEPreguntaCerradaToCollectionIfMissing(ePreguntaCerradaCollection, ...ePreguntaCerradaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ePreguntaCerrada: IEPreguntaCerrada = { id: 123 };
          const ePreguntaCerrada2: IEPreguntaCerrada = { id: 456 };
          expectedResult = service.addEPreguntaCerradaToCollectionIfMissing([], ePreguntaCerrada, ePreguntaCerrada2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ePreguntaCerrada);
          expect(expectedResult).toContain(ePreguntaCerrada2);
        });

        it('should accept null and undefined values', () => {
          const ePreguntaCerrada: IEPreguntaCerrada = { id: 123 };
          expectedResult = service.addEPreguntaCerradaToCollectionIfMissing([], null, ePreguntaCerrada, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ePreguntaCerrada);
        });

        it('should return initial array if no EPreguntaCerrada is added', () => {
          const ePreguntaCerradaCollection: IEPreguntaCerrada[] = [{ id: 123 }];
          expectedResult = service.addEPreguntaCerradaToCollectionIfMissing(ePreguntaCerradaCollection, undefined, null);
          expect(expectedResult).toEqual(ePreguntaCerradaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

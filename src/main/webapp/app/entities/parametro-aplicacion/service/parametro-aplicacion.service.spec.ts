import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IParametroAplicacion, ParametroAplicacion } from '../parametro-aplicacion.model';

import { ParametroAplicacionService } from './parametro-aplicacion.service';

describe('Service Tests', () => {
  describe('ParametroAplicacion Service', () => {
    let service: ParametroAplicacionService;
    let httpMock: HttpTestingController;
    let elemDefault: IParametroAplicacion;
    let expectedResult: IParametroAplicacion | IParametroAplicacion[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ParametroAplicacionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        maxDiasEncuesta: 0,
        minDiasEncuesta: 0,
        maxCantidadPreguntas: 0,
        minCantidadPreguntas: 0,
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

      it('should create a ParametroAplicacion', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ParametroAplicacion()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ParametroAplicacion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            maxDiasEncuesta: 1,
            minDiasEncuesta: 1,
            maxCantidadPreguntas: 1,
            minCantidadPreguntas: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ParametroAplicacion', () => {
        const patchObject = Object.assign(
          {
            minDiasEncuesta: 1,
            minCantidadPreguntas: 1,
          },
          new ParametroAplicacion()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ParametroAplicacion', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            maxDiasEncuesta: 1,
            minDiasEncuesta: 1,
            maxCantidadPreguntas: 1,
            minCantidadPreguntas: 1,
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

      it('should delete a ParametroAplicacion', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addParametroAplicacionToCollectionIfMissing', () => {
        it('should add a ParametroAplicacion to an empty array', () => {
          const parametroAplicacion: IParametroAplicacion = { id: 123 };
          expectedResult = service.addParametroAplicacionToCollectionIfMissing([], parametroAplicacion);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(parametroAplicacion);
        });

        it('should not add a ParametroAplicacion to an array that contains it', () => {
          const parametroAplicacion: IParametroAplicacion = { id: 123 };
          const parametroAplicacionCollection: IParametroAplicacion[] = [
            {
              ...parametroAplicacion,
            },
            { id: 456 },
          ];
          expectedResult = service.addParametroAplicacionToCollectionIfMissing(parametroAplicacionCollection, parametroAplicacion);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ParametroAplicacion to an array that doesn't contain it", () => {
          const parametroAplicacion: IParametroAplicacion = { id: 123 };
          const parametroAplicacionCollection: IParametroAplicacion[] = [{ id: 456 }];
          expectedResult = service.addParametroAplicacionToCollectionIfMissing(parametroAplicacionCollection, parametroAplicacion);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(parametroAplicacion);
        });

        it('should add only unique ParametroAplicacion to an array', () => {
          const parametroAplicacionArray: IParametroAplicacion[] = [{ id: 123 }, { id: 456 }, { id: 22111 }];
          const parametroAplicacionCollection: IParametroAplicacion[] = [{ id: 123 }];
          expectedResult = service.addParametroAplicacionToCollectionIfMissing(parametroAplicacionCollection, ...parametroAplicacionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const parametroAplicacion: IParametroAplicacion = { id: 123 };
          const parametroAplicacion2: IParametroAplicacion = { id: 456 };
          expectedResult = service.addParametroAplicacionToCollectionIfMissing([], parametroAplicacion, parametroAplicacion2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(parametroAplicacion);
          expect(expectedResult).toContain(parametroAplicacion2);
        });

        it('should accept null and undefined values', () => {
          const parametroAplicacion: IParametroAplicacion = { id: 123 };
          expectedResult = service.addParametroAplicacionToCollectionIfMissing([], null, parametroAplicacion, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(parametroAplicacion);
        });

        it('should return initial array if no ParametroAplicacion is added', () => {
          const parametroAplicacionCollection: IParametroAplicacion[] = [{ id: 123 }];
          expectedResult = service.addParametroAplicacionToCollectionIfMissing(parametroAplicacionCollection, undefined, null);
          expect(expectedResult).toEqual(parametroAplicacionCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

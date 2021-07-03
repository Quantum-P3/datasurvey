import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEPreguntaAbiertaRespuesta, EPreguntaAbiertaRespuesta } from '../e-pregunta-abierta-respuesta.model';

import { EPreguntaAbiertaRespuestaService } from './e-pregunta-abierta-respuesta.service';

describe('Service Tests', () => {
  describe('EPreguntaAbiertaRespuesta Service', () => {
    let service: EPreguntaAbiertaRespuestaService;
    let httpMock: HttpTestingController;
    let elemDefault: IEPreguntaAbiertaRespuesta;
    let expectedResult: IEPreguntaAbiertaRespuesta | IEPreguntaAbiertaRespuesta[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EPreguntaAbiertaRespuestaService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        respuesta: 'AAAAAAA',
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

      it('should create a EPreguntaAbiertaRespuesta', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new EPreguntaAbiertaRespuesta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EPreguntaAbiertaRespuesta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            respuesta: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a EPreguntaAbiertaRespuesta', () => {
        const patchObject = Object.assign(
          {
            respuesta: 'BBBBBB',
          },
          new EPreguntaAbiertaRespuesta()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EPreguntaAbiertaRespuesta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            respuesta: 'BBBBBB',
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

      it('should delete a EPreguntaAbiertaRespuesta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEPreguntaAbiertaRespuestaToCollectionIfMissing', () => {
        it('should add a EPreguntaAbiertaRespuesta to an empty array', () => {
          const ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta = { id: 123 };
          expectedResult = service.addEPreguntaAbiertaRespuestaToCollectionIfMissing([], ePreguntaAbiertaRespuesta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ePreguntaAbiertaRespuesta);
        });

        it('should not add a EPreguntaAbiertaRespuesta to an array that contains it', () => {
          const ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta = { id: 123 };
          const ePreguntaAbiertaRespuestaCollection: IEPreguntaAbiertaRespuesta[] = [
            {
              ...ePreguntaAbiertaRespuesta,
            },
            { id: 456 },
          ];
          expectedResult = service.addEPreguntaAbiertaRespuestaToCollectionIfMissing(
            ePreguntaAbiertaRespuestaCollection,
            ePreguntaAbiertaRespuesta
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EPreguntaAbiertaRespuesta to an array that doesn't contain it", () => {
          const ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta = { id: 123 };
          const ePreguntaAbiertaRespuestaCollection: IEPreguntaAbiertaRespuesta[] = [{ id: 456 }];
          expectedResult = service.addEPreguntaAbiertaRespuestaToCollectionIfMissing(
            ePreguntaAbiertaRespuestaCollection,
            ePreguntaAbiertaRespuesta
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ePreguntaAbiertaRespuesta);
        });

        it('should add only unique EPreguntaAbiertaRespuesta to an array', () => {
          const ePreguntaAbiertaRespuestaArray: IEPreguntaAbiertaRespuesta[] = [{ id: 123 }, { id: 456 }, { id: 61207 }];
          const ePreguntaAbiertaRespuestaCollection: IEPreguntaAbiertaRespuesta[] = [{ id: 123 }];
          expectedResult = service.addEPreguntaAbiertaRespuestaToCollectionIfMissing(
            ePreguntaAbiertaRespuestaCollection,
            ...ePreguntaAbiertaRespuestaArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta = { id: 123 };
          const ePreguntaAbiertaRespuesta2: IEPreguntaAbiertaRespuesta = { id: 456 };
          expectedResult = service.addEPreguntaAbiertaRespuestaToCollectionIfMissing(
            [],
            ePreguntaAbiertaRespuesta,
            ePreguntaAbiertaRespuesta2
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(ePreguntaAbiertaRespuesta);
          expect(expectedResult).toContain(ePreguntaAbiertaRespuesta2);
        });

        it('should accept null and undefined values', () => {
          const ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta = { id: 123 };
          expectedResult = service.addEPreguntaAbiertaRespuestaToCollectionIfMissing([], null, ePreguntaAbiertaRespuesta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(ePreguntaAbiertaRespuesta);
        });

        it('should return initial array if no EPreguntaAbiertaRespuesta is added', () => {
          const ePreguntaAbiertaRespuestaCollection: IEPreguntaAbiertaRespuesta[] = [{ id: 123 }];
          expectedResult = service.addEPreguntaAbiertaRespuestaToCollectionIfMissing(ePreguntaAbiertaRespuestaCollection, undefined, null);
          expect(expectedResult).toEqual(ePreguntaAbiertaRespuestaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

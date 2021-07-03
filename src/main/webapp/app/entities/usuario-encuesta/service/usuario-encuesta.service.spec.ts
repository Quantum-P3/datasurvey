import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { RolColaborador } from 'app/entities/enumerations/rol-colaborador.model';
import { EstadoColaborador } from 'app/entities/enumerations/estado-colaborador.model';
import { IUsuarioEncuesta, UsuarioEncuesta } from '../usuario-encuesta.model';

import { UsuarioEncuestaService } from './usuario-encuesta.service';

describe('Service Tests', () => {
  describe('UsuarioEncuesta Service', () => {
    let service: UsuarioEncuestaService;
    let httpMock: HttpTestingController;
    let elemDefault: IUsuarioEncuesta;
    let expectedResult: IUsuarioEncuesta | IUsuarioEncuesta[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UsuarioEncuestaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        rol: RolColaborador.READ,
        estado: EstadoColaborador.PENDING,
        fechaAgregado: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaAgregado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a UsuarioEncuesta', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaAgregado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaAgregado: currentDate,
          },
          returnedFromService
        );

        service.create(new UsuarioEncuesta()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a UsuarioEncuesta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            rol: 'BBBBBB',
            estado: 'BBBBBB',
            fechaAgregado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaAgregado: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a UsuarioEncuesta', () => {
        const patchObject = Object.assign(
          {
            rol: 'BBBBBB',
            estado: 'BBBBBB',
          },
          new UsuarioEncuesta()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaAgregado: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of UsuarioEncuesta', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            rol: 'BBBBBB',
            estado: 'BBBBBB',
            fechaAgregado: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaAgregado: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a UsuarioEncuesta', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUsuarioEncuestaToCollectionIfMissing', () => {
        it('should add a UsuarioEncuesta to an empty array', () => {
          const usuarioEncuesta: IUsuarioEncuesta = { id: 123 };
          expectedResult = service.addUsuarioEncuestaToCollectionIfMissing([], usuarioEncuesta);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(usuarioEncuesta);
        });

        it('should not add a UsuarioEncuesta to an array that contains it', () => {
          const usuarioEncuesta: IUsuarioEncuesta = { id: 123 };
          const usuarioEncuestaCollection: IUsuarioEncuesta[] = [
            {
              ...usuarioEncuesta,
            },
            { id: 456 },
          ];
          expectedResult = service.addUsuarioEncuestaToCollectionIfMissing(usuarioEncuestaCollection, usuarioEncuesta);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a UsuarioEncuesta to an array that doesn't contain it", () => {
          const usuarioEncuesta: IUsuarioEncuesta = { id: 123 };
          const usuarioEncuestaCollection: IUsuarioEncuesta[] = [{ id: 456 }];
          expectedResult = service.addUsuarioEncuestaToCollectionIfMissing(usuarioEncuestaCollection, usuarioEncuesta);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(usuarioEncuesta);
        });

        it('should add only unique UsuarioEncuesta to an array', () => {
          const usuarioEncuestaArray: IUsuarioEncuesta[] = [{ id: 123 }, { id: 456 }, { id: 52103 }];
          const usuarioEncuestaCollection: IUsuarioEncuesta[] = [{ id: 123 }];
          expectedResult = service.addUsuarioEncuestaToCollectionIfMissing(usuarioEncuestaCollection, ...usuarioEncuestaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const usuarioEncuesta: IUsuarioEncuesta = { id: 123 };
          const usuarioEncuesta2: IUsuarioEncuesta = { id: 456 };
          expectedResult = service.addUsuarioEncuestaToCollectionIfMissing([], usuarioEncuesta, usuarioEncuesta2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(usuarioEncuesta);
          expect(expectedResult).toContain(usuarioEncuesta2);
        });

        it('should accept null and undefined values', () => {
          const usuarioEncuesta: IUsuarioEncuesta = { id: 123 };
          expectedResult = service.addUsuarioEncuestaToCollectionIfMissing([], null, usuarioEncuesta, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(usuarioEncuesta);
        });

        it('should return initial array if no UsuarioEncuesta is added', () => {
          const usuarioEncuestaCollection: IUsuarioEncuesta[] = [{ id: 123 }];
          expectedResult = service.addUsuarioEncuestaToCollectionIfMissing(usuarioEncuestaCollection, undefined, null);
          expect(expectedResult).toEqual(usuarioEncuestaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

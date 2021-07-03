import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { EstadoUsuario } from 'app/entities/enumerations/estado-usuario.model';
import { IUsuarioExtra, UsuarioExtra } from '../usuario-extra.model';

import { UsuarioExtraService } from './usuario-extra.service';

describe('Service Tests', () => {
  describe('UsuarioExtra Service', () => {
    let service: UsuarioExtraService;
    let httpMock: HttpTestingController;
    let elemDefault: IUsuarioExtra;
    let expectedResult: IUsuarioExtra | IUsuarioExtra[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UsuarioExtraService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        iconoPerfil: 'AAAAAAA',
        fechaNacimiento: currentDate,
        estado: EstadoUsuario.ACTIVE,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaNacimiento: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a UsuarioExtra', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaNacimiento: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );

        service.create(new UsuarioExtra()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a UsuarioExtra', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            iconoPerfil: 'BBBBBB',
            fechaNacimiento: currentDate.format(DATE_TIME_FORMAT),
            estado: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a UsuarioExtra', () => {
        const patchObject = Object.assign(
          {
            nombre: 'BBBBBB',
            iconoPerfil: 'BBBBBB',
          },
          new UsuarioExtra()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of UsuarioExtra', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            iconoPerfil: 'BBBBBB',
            fechaNacimiento: currentDate.format(DATE_TIME_FORMAT),
            estado: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a UsuarioExtra', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUsuarioExtraToCollectionIfMissing', () => {
        it('should add a UsuarioExtra to an empty array', () => {
          const usuarioExtra: IUsuarioExtra = { id: 123 };
          expectedResult = service.addUsuarioExtraToCollectionIfMissing([], usuarioExtra);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(usuarioExtra);
        });

        it('should not add a UsuarioExtra to an array that contains it', () => {
          const usuarioExtra: IUsuarioExtra = { id: 123 };
          const usuarioExtraCollection: IUsuarioExtra[] = [
            {
              ...usuarioExtra,
            },
            { id: 456 },
          ];
          expectedResult = service.addUsuarioExtraToCollectionIfMissing(usuarioExtraCollection, usuarioExtra);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a UsuarioExtra to an array that doesn't contain it", () => {
          const usuarioExtra: IUsuarioExtra = { id: 123 };
          const usuarioExtraCollection: IUsuarioExtra[] = [{ id: 456 }];
          expectedResult = service.addUsuarioExtraToCollectionIfMissing(usuarioExtraCollection, usuarioExtra);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(usuarioExtra);
        });

        it('should add only unique UsuarioExtra to an array', () => {
          const usuarioExtraArray: IUsuarioExtra[] = [{ id: 123 }, { id: 456 }, { id: 53810 }];
          const usuarioExtraCollection: IUsuarioExtra[] = [{ id: 123 }];
          expectedResult = service.addUsuarioExtraToCollectionIfMissing(usuarioExtraCollection, ...usuarioExtraArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const usuarioExtra: IUsuarioExtra = { id: 123 };
          const usuarioExtra2: IUsuarioExtra = { id: 456 };
          expectedResult = service.addUsuarioExtraToCollectionIfMissing([], usuarioExtra, usuarioExtra2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(usuarioExtra);
          expect(expectedResult).toContain(usuarioExtra2);
        });

        it('should accept null and undefined values', () => {
          const usuarioExtra: IUsuarioExtra = { id: 123 };
          expectedResult = service.addUsuarioExtraToCollectionIfMissing([], null, usuarioExtra, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(usuarioExtra);
        });

        it('should return initial array if no UsuarioExtra is added', () => {
          const usuarioExtraCollection: IUsuarioExtra[] = [{ id: 123 }];
          expectedResult = service.addUsuarioExtraToCollectionIfMissing(usuarioExtraCollection, undefined, null);
          expect(expectedResult).toEqual(usuarioExtraCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { EstadoPlantilla } from 'app/entities/enumerations/estado-plantilla.model';
import { IPlantilla, Plantilla } from '../plantilla.model';

import { PlantillaService } from './plantilla.service';

describe('Service Tests', () => {
  describe('Plantilla Service', () => {
    let service: PlantillaService;
    let httpMock: HttpTestingController;
    let elemDefault: IPlantilla;
    let expectedResult: IPlantilla | IPlantilla[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PlantillaService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        nombre: 'AAAAAAA',
        descripcion: 'AAAAAAA',
        fechaCreacion: currentDate,
        fechaPublicacionTienda: currentDate,
        estado: EstadoPlantilla.DRAFT,
        precio: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaPublicacionTienda: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Plantilla', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaPublicacionTienda: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaCreacion: currentDate,
            fechaPublicacionTienda: currentDate,
          },
          returnedFromService
        );

        service.create(new Plantilla()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Plantilla', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            descripcion: 'BBBBBB',
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaPublicacionTienda: currentDate.format(DATE_TIME_FORMAT),
            estado: 'BBBBBB',
            precio: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaCreacion: currentDate,
            fechaPublicacionTienda: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Plantilla', () => {
        const patchObject = Object.assign(
          {
            descripcion: 'BBBBBB',
            estado: 'BBBBBB',
          },
          new Plantilla()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaCreacion: currentDate,
            fechaPublicacionTienda: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Plantilla', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nombre: 'BBBBBB',
            descripcion: 'BBBBBB',
            fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
            fechaPublicacionTienda: currentDate.format(DATE_TIME_FORMAT),
            estado: 'BBBBBB',
            precio: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaCreacion: currentDate,
            fechaPublicacionTienda: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Plantilla', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPlantillaToCollectionIfMissing', () => {
        it('should add a Plantilla to an empty array', () => {
          const plantilla: IPlantilla = { id: 123 };
          expectedResult = service.addPlantillaToCollectionIfMissing([], plantilla);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(plantilla);
        });

        it('should not add a Plantilla to an array that contains it', () => {
          const plantilla: IPlantilla = { id: 123 };
          const plantillaCollection: IPlantilla[] = [
            {
              ...plantilla,
            },
            { id: 456 },
          ];
          expectedResult = service.addPlantillaToCollectionIfMissing(plantillaCollection, plantilla);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Plantilla to an array that doesn't contain it", () => {
          const plantilla: IPlantilla = { id: 123 };
          const plantillaCollection: IPlantilla[] = [{ id: 456 }];
          expectedResult = service.addPlantillaToCollectionIfMissing(plantillaCollection, plantilla);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(plantilla);
        });

        it('should add only unique Plantilla to an array', () => {
          const plantillaArray: IPlantilla[] = [{ id: 123 }, { id: 456 }, { id: 99638 }];
          const plantillaCollection: IPlantilla[] = [{ id: 123 }];
          expectedResult = service.addPlantillaToCollectionIfMissing(plantillaCollection, ...plantillaArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const plantilla: IPlantilla = { id: 123 };
          const plantilla2: IPlantilla = { id: 456 };
          expectedResult = service.addPlantillaToCollectionIfMissing([], plantilla, plantilla2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(plantilla);
          expect(expectedResult).toContain(plantilla2);
        });

        it('should accept null and undefined values', () => {
          const plantilla: IPlantilla = { id: 123 };
          expectedResult = service.addPlantillaToCollectionIfMissing([], null, plantilla, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(plantilla);
        });

        it('should return initial array if no Plantilla is added', () => {
          const plantillaCollection: IPlantilla[] = [{ id: 123 }];
          expectedResult = service.addPlantillaToCollectionIfMissing(plantillaCollection, undefined, null);
          expect(expectedResult).toEqual(plantillaCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});

jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPlantilla, Plantilla } from '../plantilla.model';
import { PlantillaService } from '../service/plantilla.service';

import { PlantillaRoutingResolveService } from './plantilla-routing-resolve.service';

describe('Service Tests', () => {
  describe('Plantilla routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PlantillaRoutingResolveService;
    let service: PlantillaService;
    let resultPlantilla: IPlantilla | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PlantillaRoutingResolveService);
      service = TestBed.inject(PlantillaService);
      resultPlantilla = undefined;
    });

    describe('resolve', () => {
      it('should return IPlantilla returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlantilla = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPlantilla).toEqual({ id: 123 });
      });

      it('should return new IPlantilla if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlantilla = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPlantilla).toEqual(new Plantilla());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Plantilla })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPlantilla = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPlantilla).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

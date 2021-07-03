jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPPreguntaCerradaOpcion, PPreguntaCerradaOpcion } from '../p-pregunta-cerrada-opcion.model';
import { PPreguntaCerradaOpcionService } from '../service/p-pregunta-cerrada-opcion.service';

import { PPreguntaCerradaOpcionRoutingResolveService } from './p-pregunta-cerrada-opcion-routing-resolve.service';

describe('Service Tests', () => {
  describe('PPreguntaCerradaOpcion routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PPreguntaCerradaOpcionRoutingResolveService;
    let service: PPreguntaCerradaOpcionService;
    let resultPPreguntaCerradaOpcion: IPPreguntaCerradaOpcion | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PPreguntaCerradaOpcionRoutingResolveService);
      service = TestBed.inject(PPreguntaCerradaOpcionService);
      resultPPreguntaCerradaOpcion = undefined;
    });

    describe('resolve', () => {
      it('should return IPPreguntaCerradaOpcion returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPPreguntaCerradaOpcion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPPreguntaCerradaOpcion).toEqual({ id: 123 });
      });

      it('should return new IPPreguntaCerradaOpcion if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPPreguntaCerradaOpcion = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPPreguntaCerradaOpcion).toEqual(new PPreguntaCerradaOpcion());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PPreguntaCerradaOpcion })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPPreguntaCerradaOpcion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPPreguntaCerradaOpcion).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

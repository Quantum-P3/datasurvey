jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEPreguntaCerradaOpcion, EPreguntaCerradaOpcion } from '../e-pregunta-cerrada-opcion.model';
import { EPreguntaCerradaOpcionService } from '../service/e-pregunta-cerrada-opcion.service';

import { EPreguntaCerradaOpcionRoutingResolveService } from './e-pregunta-cerrada-opcion-routing-resolve.service';

describe('Service Tests', () => {
  describe('EPreguntaCerradaOpcion routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EPreguntaCerradaOpcionRoutingResolveService;
    let service: EPreguntaCerradaOpcionService;
    let resultEPreguntaCerradaOpcion: IEPreguntaCerradaOpcion | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EPreguntaCerradaOpcionRoutingResolveService);
      service = TestBed.inject(EPreguntaCerradaOpcionService);
      resultEPreguntaCerradaOpcion = undefined;
    });

    describe('resolve', () => {
      it('should return IEPreguntaCerradaOpcion returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEPreguntaCerradaOpcion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEPreguntaCerradaOpcion).toEqual({ id: 123 });
      });

      it('should return new IEPreguntaCerradaOpcion if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEPreguntaCerradaOpcion = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEPreguntaCerradaOpcion).toEqual(new EPreguntaCerradaOpcion());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EPreguntaCerradaOpcion })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEPreguntaCerradaOpcion = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEPreguntaCerradaOpcion).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

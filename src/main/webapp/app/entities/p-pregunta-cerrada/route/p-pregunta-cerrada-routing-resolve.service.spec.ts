jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPPreguntaCerrada, PPreguntaCerrada } from '../p-pregunta-cerrada.model';
import { PPreguntaCerradaService } from '../service/p-pregunta-cerrada.service';

import { PPreguntaCerradaRoutingResolveService } from './p-pregunta-cerrada-routing-resolve.service';

describe('Service Tests', () => {
  describe('PPreguntaCerrada routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PPreguntaCerradaRoutingResolveService;
    let service: PPreguntaCerradaService;
    let resultPPreguntaCerrada: IPPreguntaCerrada | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PPreguntaCerradaRoutingResolveService);
      service = TestBed.inject(PPreguntaCerradaService);
      resultPPreguntaCerrada = undefined;
    });

    describe('resolve', () => {
      it('should return IPPreguntaCerrada returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPPreguntaCerrada = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPPreguntaCerrada).toEqual({ id: 123 });
      });

      it('should return new IPPreguntaCerrada if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPPreguntaCerrada = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPPreguntaCerrada).toEqual(new PPreguntaCerrada());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PPreguntaCerrada })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPPreguntaCerrada = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPPreguntaCerrada).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

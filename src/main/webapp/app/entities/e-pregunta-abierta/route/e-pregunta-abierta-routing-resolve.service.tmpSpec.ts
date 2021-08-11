jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEPreguntaAbierta, EPreguntaAbierta } from '../e-pregunta-abierta.model';
import { EPreguntaAbiertaService } from '../service/e-pregunta-abierta.service';

import { EPreguntaAbiertaRoutingResolveService } from './e-pregunta-abierta-routing-resolve.service';

describe('Service Tests', () => {
  describe('EPreguntaAbierta routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EPreguntaAbiertaRoutingResolveService;
    let service: EPreguntaAbiertaService;
    let resultEPreguntaAbierta: IEPreguntaAbierta | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EPreguntaAbiertaRoutingResolveService);
      service = TestBed.inject(EPreguntaAbiertaService);
      resultEPreguntaAbierta = undefined;
    });

    describe('resolve', () => {
      it('should return IEPreguntaAbierta returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEPreguntaAbierta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEPreguntaAbierta).toEqual({ id: 123 });
      });

      it('should return new IEPreguntaAbierta if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEPreguntaAbierta = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEPreguntaAbierta).toEqual(new EPreguntaAbierta());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EPreguntaAbierta })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEPreguntaAbierta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEPreguntaAbierta).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

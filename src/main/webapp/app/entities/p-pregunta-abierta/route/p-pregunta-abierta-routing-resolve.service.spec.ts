jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPPreguntaAbierta, PPreguntaAbierta } from '../p-pregunta-abierta.model';
import { PPreguntaAbiertaService } from '../service/p-pregunta-abierta.service';

import { PPreguntaAbiertaRoutingResolveService } from './p-pregunta-abierta-routing-resolve.service';

describe('Service Tests', () => {
  describe('PPreguntaAbierta routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PPreguntaAbiertaRoutingResolveService;
    let service: PPreguntaAbiertaService;
    let resultPPreguntaAbierta: IPPreguntaAbierta | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PPreguntaAbiertaRoutingResolveService);
      service = TestBed.inject(PPreguntaAbiertaService);
      resultPPreguntaAbierta = undefined;
    });

    describe('resolve', () => {
      it('should return IPPreguntaAbierta returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPPreguntaAbierta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPPreguntaAbierta).toEqual({ id: 123 });
      });

      it('should return new IPPreguntaAbierta if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPPreguntaAbierta = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPPreguntaAbierta).toEqual(new PPreguntaAbierta());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PPreguntaAbierta })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPPreguntaAbierta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPPreguntaAbierta).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEncuesta, Encuesta } from '../encuesta.model';
import { EncuestaService } from '../service/encuesta.service';

import { EncuestaRoutingResolveService } from './encuesta-routing-resolve.service';

describe('Service Tests', () => {
  describe('Encuesta routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EncuestaRoutingResolveService;
    let service: EncuestaService;
    let resultEncuesta: IEncuesta | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EncuestaRoutingResolveService);
      service = TestBed.inject(EncuestaService);
      resultEncuesta = undefined;
    });

    describe('resolve', () => {
      it('should return IEncuesta returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEncuesta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEncuesta).toEqual({ id: 123 });
      });

      it('should return new IEncuesta if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEncuesta = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEncuesta).toEqual(new Encuesta());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Encuesta })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEncuesta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEncuesta).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUsuarioEncuesta, UsuarioEncuesta } from '../usuario-encuesta.model';
import { UsuarioEncuestaService } from '../service/usuario-encuesta.service';

import { UsuarioEncuestaRoutingResolveService } from './usuario-encuesta-routing-resolve.service';

describe('Service Tests', () => {
  describe('UsuarioEncuesta routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UsuarioEncuestaRoutingResolveService;
    let service: UsuarioEncuestaService;
    let resultUsuarioEncuesta: IUsuarioEncuesta | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UsuarioEncuestaRoutingResolveService);
      service = TestBed.inject(UsuarioEncuestaService);
      resultUsuarioEncuesta = undefined;
    });

    describe('resolve', () => {
      it('should return IUsuarioEncuesta returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUsuarioEncuesta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUsuarioEncuesta).toEqual({ id: 123 });
      });

      it('should return new IUsuarioEncuesta if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUsuarioEncuesta = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUsuarioEncuesta).toEqual(new UsuarioEncuesta());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UsuarioEncuesta })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUsuarioEncuesta = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUsuarioEncuesta).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

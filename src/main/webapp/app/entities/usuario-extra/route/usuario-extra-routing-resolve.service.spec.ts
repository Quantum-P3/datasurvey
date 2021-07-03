jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUsuarioExtra, UsuarioExtra } from '../usuario-extra.model';
import { UsuarioExtraService } from '../service/usuario-extra.service';

import { UsuarioExtraRoutingResolveService } from './usuario-extra-routing-resolve.service';

describe('Service Tests', () => {
  describe('UsuarioExtra routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UsuarioExtraRoutingResolveService;
    let service: UsuarioExtraService;
    let resultUsuarioExtra: IUsuarioExtra | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UsuarioExtraRoutingResolveService);
      service = TestBed.inject(UsuarioExtraService);
      resultUsuarioExtra = undefined;
    });

    describe('resolve', () => {
      it('should return IUsuarioExtra returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUsuarioExtra = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUsuarioExtra).toEqual({ id: 123 });
      });

      it('should return new IUsuarioExtra if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUsuarioExtra = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUsuarioExtra).toEqual(new UsuarioExtra());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as UsuarioExtra })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUsuarioExtra = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUsuarioExtra).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

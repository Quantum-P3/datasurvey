import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ParametroAplicacionComponent } from '../list/parametro-aplicacion.component';
import { ParametroAplicacionDetailComponent } from '../detail/parametro-aplicacion-detail.component';
import { ParametroAplicacionUpdateComponent } from '../update/parametro-aplicacion-update.component';
import { ParametroAplicacionRoutingResolveService } from './parametro-aplicacion-routing-resolve.service';

const parametroAplicacionRoute: Routes = [
  {
    path: '',
    component: ParametroAplicacionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParametroAplicacionDetailComponent,
    resolve: {
      parametroAplicacion: ParametroAplicacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParametroAplicacionUpdateComponent,
    resolve: {
      parametroAplicacion: ParametroAplicacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParametroAplicacionUpdateComponent,
    resolve: {
      parametroAplicacion: ParametroAplicacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(parametroAplicacionRoute)],
  exports: [RouterModule],
})
export class ParametroAplicacionRoutingModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UsuarioExtraComponent } from '../list/usuario-extra.component';
import { UsuarioExtraDetailComponent } from '../detail/usuario-extra-detail.component';
import { UsuarioExtraUpdateComponent } from '../update/usuario-extra-update.component';
import { UsuarioExtraRoutingResolveService } from './usuario-extra-routing-resolve.service';

const usuarioExtraRoute: Routes = [
  {
    path: '',
    component: UsuarioExtraComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UsuarioExtraDetailComponent,
    resolve: {
      usuarioExtra: UsuarioExtraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UsuarioExtraUpdateComponent,
    resolve: {
      usuarioExtra: UsuarioExtraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UsuarioExtraUpdateComponent,
    resolve: {
      usuarioExtra: UsuarioExtraRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(usuarioExtraRoute)],
  exports: [RouterModule],
})
export class UsuarioExtraRoutingModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlantillaComponent } from '../list/plantilla.component';
import { PlantillaDetailComponent } from '../detail/plantilla-detail.component';
import { PlantillaUpdateComponent } from '../update/plantilla-update.component';
import { PlantillaRoutingResolveService } from './plantilla-routing-resolve.service';

const plantillaRoute: Routes = [
  {
    path: '',
    component: PlantillaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlantillaDetailComponent,
    resolve: {
      plantilla: PlantillaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlantillaUpdateComponent,
    resolve: {
      plantilla: PlantillaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlantillaUpdateComponent,
    resolve: {
      plantilla: PlantillaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(plantillaRoute)],
  exports: [RouterModule],
})
export class PlantillaRoutingModule {}

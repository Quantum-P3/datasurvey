import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DashboardUserComponent } from '../dashboard-user/dashboard-user.component';
import { DashboardAdminComponent } from '../dashboard-admin/dashboard-admin.component';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';

const dashboardRoute: Routes = [
  {
    path: 'admin',
    component: DashboardAdminComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'user',
    component: DashboardUserComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dashboardRoute)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {}

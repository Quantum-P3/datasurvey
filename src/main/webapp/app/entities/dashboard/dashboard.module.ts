import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardUserComponent } from './dashboard-user/dashboard-user.component';
import { DashboardAdminComponent } from './dashboard-admin/dashboard-admin.component';
import { SharedModule } from '../../shared/shared.module';
import { PlantillaRoutingModule } from '../plantilla/route/plantilla-routing.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  declarations: [DashboardUserComponent, DashboardAdminComponent],
  imports: [CommonModule, SharedModule, PlantillaRoutingModule, FontAwesomeModule],
})
export class DashboardModule {}

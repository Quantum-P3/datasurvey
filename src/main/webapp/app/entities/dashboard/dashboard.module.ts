import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DashboardUserComponent } from './dashboard-user/dashboard-user.component';
import { DashboardAdminComponent } from './dashboard-admin/dashboard-admin.component';
import { SharedModule } from '../../shared/shared.module';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { DashboardRoutingModule } from './route/dashboard-routing.module';

@NgModule({
  declarations: [DashboardUserComponent, DashboardAdminComponent],
  imports: [CommonModule, SharedModule, DashboardRoutingModule, FontAwesomeModule],
})
export class DashboardModule {}

import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ParametroAplicacionComponent } from './list/parametro-aplicacion.component';
import { ParametroAplicacionDetailComponent } from './detail/parametro-aplicacion-detail.component';
import { ParametroAplicacionUpdateComponent } from './update/parametro-aplicacion-update.component';
import { ParametroAplicacionDeleteDialogComponent } from './delete/parametro-aplicacion-delete-dialog.component';
import { ParametroAplicacionRoutingModule } from './route/parametro-aplicacion-routing.module';

@NgModule({
  imports: [SharedModule, ParametroAplicacionRoutingModule],
  declarations: [
    ParametroAplicacionComponent,
    ParametroAplicacionDetailComponent,
    ParametroAplicacionUpdateComponent,
    ParametroAplicacionDeleteDialogComponent,
  ],
  entryComponents: [ParametroAplicacionDeleteDialogComponent],
})
export class ParametroAplicacionModule {}

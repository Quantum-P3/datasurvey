import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PPreguntaCerradaComponent } from './list/p-pregunta-cerrada.component';
import { PPreguntaCerradaDetailComponent } from './detail/p-pregunta-cerrada-detail.component';
import { PPreguntaCerradaUpdateComponent } from './update/p-pregunta-cerrada-update.component';
import { PPreguntaCerradaDeleteDialogComponent } from './delete/p-pregunta-cerrada-delete-dialog.component';
import { PPreguntaCerradaRoutingModule } from './route/p-pregunta-cerrada-routing.module';

@NgModule({
  imports: [SharedModule, PPreguntaCerradaRoutingModule],
  declarations: [
    PPreguntaCerradaComponent,
    PPreguntaCerradaDetailComponent,
    PPreguntaCerradaUpdateComponent,
    PPreguntaCerradaDeleteDialogComponent,
  ],
  entryComponents: [PPreguntaCerradaDeleteDialogComponent],
})
export class PPreguntaCerradaModule {}

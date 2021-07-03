import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PPreguntaCerradaOpcionComponent } from './list/p-pregunta-cerrada-opcion.component';
import { PPreguntaCerradaOpcionDetailComponent } from './detail/p-pregunta-cerrada-opcion-detail.component';
import { PPreguntaCerradaOpcionUpdateComponent } from './update/p-pregunta-cerrada-opcion-update.component';
import { PPreguntaCerradaOpcionDeleteDialogComponent } from './delete/p-pregunta-cerrada-opcion-delete-dialog.component';
import { PPreguntaCerradaOpcionRoutingModule } from './route/p-pregunta-cerrada-opcion-routing.module';

@NgModule({
  imports: [SharedModule, PPreguntaCerradaOpcionRoutingModule],
  declarations: [
    PPreguntaCerradaOpcionComponent,
    PPreguntaCerradaOpcionDetailComponent,
    PPreguntaCerradaOpcionUpdateComponent,
    PPreguntaCerradaOpcionDeleteDialogComponent,
  ],
  entryComponents: [PPreguntaCerradaOpcionDeleteDialogComponent],
})
export class PPreguntaCerradaOpcionModule {}

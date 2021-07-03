import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EPreguntaCerradaOpcionComponent } from './list/e-pregunta-cerrada-opcion.component';
import { EPreguntaCerradaOpcionDetailComponent } from './detail/e-pregunta-cerrada-opcion-detail.component';
import { EPreguntaCerradaOpcionUpdateComponent } from './update/e-pregunta-cerrada-opcion-update.component';
import { EPreguntaCerradaOpcionDeleteDialogComponent } from './delete/e-pregunta-cerrada-opcion-delete-dialog.component';
import { EPreguntaCerradaOpcionRoutingModule } from './route/e-pregunta-cerrada-opcion-routing.module';

@NgModule({
  imports: [SharedModule, EPreguntaCerradaOpcionRoutingModule],
  declarations: [
    EPreguntaCerradaOpcionComponent,
    EPreguntaCerradaOpcionDetailComponent,
    EPreguntaCerradaOpcionUpdateComponent,
    EPreguntaCerradaOpcionDeleteDialogComponent,
  ],
  entryComponents: [EPreguntaCerradaOpcionDeleteDialogComponent],
})
export class EPreguntaCerradaOpcionModule {}

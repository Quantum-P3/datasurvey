import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EPreguntaCerradaComponent } from './list/e-pregunta-cerrada.component';
import { EPreguntaCerradaDetailComponent } from './detail/e-pregunta-cerrada-detail.component';
import { EPreguntaCerradaUpdateComponent } from './update/e-pregunta-cerrada-update.component';
import { EPreguntaCerradaDeleteDialogComponent } from './delete/e-pregunta-cerrada-delete-dialog.component';
import { EPreguntaCerradaRoutingModule } from './route/e-pregunta-cerrada-routing.module';

@NgModule({
  imports: [SharedModule, EPreguntaCerradaRoutingModule],
  declarations: [
    EPreguntaCerradaComponent,
    EPreguntaCerradaDetailComponent,
    EPreguntaCerradaUpdateComponent,
    EPreguntaCerradaDeleteDialogComponent,
  ],
  entryComponents: [EPreguntaCerradaDeleteDialogComponent],
})
export class EPreguntaCerradaModule {}

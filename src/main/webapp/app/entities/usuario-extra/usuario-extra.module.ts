import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UsuarioExtraComponent } from './list/usuario-extra.component';
import { UsuarioExtraDetailComponent } from './detail/usuario-extra-detail.component';
import { UsuarioExtraUpdateComponent } from './update/usuario-extra-update.component';
import { UsuarioExtraDeleteDialogComponent } from './delete/usuario-extra-delete-dialog.component';
import { UsuarioExtraRoutingModule } from './route/usuario-extra-routing.module';

@NgModule({
  imports: [SharedModule, UsuarioExtraRoutingModule],
  declarations: [UsuarioExtraComponent, UsuarioExtraDetailComponent, UsuarioExtraUpdateComponent, UsuarioExtraDeleteDialogComponent],
  entryComponents: [UsuarioExtraDeleteDialogComponent],
})
export class UsuarioExtraModule {}

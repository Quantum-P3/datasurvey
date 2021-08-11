import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from 'app/shared/shared.module';
import { TIENDA_PLANTILLA_ROUTE } from './listar-tienda-plantilla.route';
import { ListarTiendaPlantillaComponent } from './listar-tienda-plantilla.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([TIENDA_PLANTILLA_ROUTE])],
  declarations: [ListarTiendaPlantillaComponent],
})
export class ListarPlantillaTiendaModule {}

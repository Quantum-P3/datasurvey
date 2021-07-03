import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlantillaComponent } from './list/plantilla.component';
import { PlantillaDetailComponent } from './detail/plantilla-detail.component';
import { PlantillaUpdateComponent } from './update/plantilla-update.component';
import { PlantillaDeleteDialogComponent } from './delete/plantilla-delete-dialog.component';
import { PlantillaRoutingModule } from './route/plantilla-routing.module';

@NgModule({
  imports: [SharedModule, PlantillaRoutingModule],
  declarations: [PlantillaComponent, PlantillaDetailComponent, PlantillaUpdateComponent, PlantillaDeleteDialogComponent],
  entryComponents: [PlantillaDeleteDialogComponent],
})
export class PlantillaModule {}

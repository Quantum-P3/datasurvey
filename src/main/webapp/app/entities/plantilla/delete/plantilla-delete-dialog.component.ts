import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlantilla } from '../plantilla.model';
import { PlantillaService } from '../service/plantilla.service';
import { EstadoPlantilla } from '../../enumerations/estado-plantilla.model';

@Component({
  templateUrl: './plantilla-delete-dialog.component.html',
})
export class PlantillaDeleteDialogComponent {
  plantilla?: IPlantilla;

  constructor(protected plantillaService: PlantillaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(pPlantilla: IPlantilla): void {
    pPlantilla.estado = EstadoPlantilla.DELETED;
    this.plantillaService.update(pPlantilla).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

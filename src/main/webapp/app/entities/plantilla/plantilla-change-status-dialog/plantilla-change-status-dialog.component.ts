import { Component } from '@angular/core';
import { PlantillaService } from '../service/plantilla.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EstadoPlantilla } from '../../enumerations/estado-plantilla.model';
import { IPlantilla } from '../plantilla.model';

import { faExchangeAlt } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-plantilla-change-status-dialog',
  templateUrl: './plantilla-change-status-dialog.component.html',
  styleUrls: ['./plantilla-change-status-dialog.component.scss'],
})
export class PlantillaChangeStatusDialogComponent {
  plantilla?: IPlantilla;

  faExchangeAlt = faExchangeAlt;
  constructor(protected plantillaService: PlantillaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmChangeStatus(pPlantilla: IPlantilla) {
    if (this.plantilla) {
      if (pPlantilla.estado === EstadoPlantilla.DISABLED) {
        this.plantilla.estado = EstadoPlantilla.DRAFT;
      } else {
        this.plantilla.estado = EstadoPlantilla.DISABLED;
      }
      this.plantillaService.update(this.plantilla).subscribe(() => {
        this.activeModal.close('updated');
      });
    }
  }
}

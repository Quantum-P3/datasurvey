import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlantilla } from '../plantilla.model';
import { PlantillaService } from '../service/plantilla.service';
import { PlantillaDeleteDialogComponent } from '../delete/plantilla-delete-dialog.component';

@Component({
  selector: 'jhi-plantilla',
  templateUrl: './plantilla.component.html',
})
export class PlantillaComponent implements OnInit {
  plantillas?: IPlantilla[];
  isLoading = false;

  constructor(protected plantillaService: PlantillaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.plantillaService.query().subscribe(
      (res: HttpResponse<IPlantilla[]>) => {
        this.isLoading = false;
        this.plantillas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPlantilla): number {
    return item.id!;
  }

  delete(plantilla: IPlantilla): void {
    const modalRef = this.modalService.open(PlantillaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plantilla = plantilla;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

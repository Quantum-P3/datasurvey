import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlantilla } from '../plantilla.model';

@Component({
  selector: 'jhi-plantilla-detail',
  templateUrl: './plantilla-detail.component.html',
})
export class PlantillaDetailComponent implements OnInit {
  plantilla: IPlantilla | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plantilla }) => {
      this.plantilla = plantilla;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEncuesta } from '../encuesta.model';

@Component({
  selector: 'jhi-encuesta-detail',
  templateUrl: './encuesta-detail.component.html',
})
export class EncuestaDetailComponent implements OnInit {
  encuesta: IEncuesta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ encuesta }) => {
      this.encuesta = encuesta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

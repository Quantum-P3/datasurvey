import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPPreguntaCerradaOpcion } from '../p-pregunta-cerrada-opcion.model';

@Component({
  selector: 'jhi-p-pregunta-cerrada-opcion-detail',
  templateUrl: './p-pregunta-cerrada-opcion-detail.component.html',
})
export class PPreguntaCerradaOpcionDetailComponent implements OnInit {
  pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pPreguntaCerradaOpcion }) => {
      this.pPreguntaCerradaOpcion = pPreguntaCerradaOpcion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEPreguntaCerradaOpcion } from '../e-pregunta-cerrada-opcion.model';

@Component({
  selector: 'jhi-e-pregunta-cerrada-opcion-detail',
  templateUrl: './e-pregunta-cerrada-opcion-detail.component.html',
})
export class EPreguntaCerradaOpcionDetailComponent implements OnInit {
  ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ePreguntaCerradaOpcion }) => {
      this.ePreguntaCerradaOpcion = ePreguntaCerradaOpcion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

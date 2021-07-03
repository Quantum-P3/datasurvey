import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEPreguntaCerrada } from '../e-pregunta-cerrada.model';

@Component({
  selector: 'jhi-e-pregunta-cerrada-detail',
  templateUrl: './e-pregunta-cerrada-detail.component.html',
})
export class EPreguntaCerradaDetailComponent implements OnInit {
  ePreguntaCerrada: IEPreguntaCerrada | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ePreguntaCerrada }) => {
      this.ePreguntaCerrada = ePreguntaCerrada;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

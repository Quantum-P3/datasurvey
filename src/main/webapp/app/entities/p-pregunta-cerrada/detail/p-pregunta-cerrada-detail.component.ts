import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPPreguntaCerrada } from '../p-pregunta-cerrada.model';

@Component({
  selector: 'jhi-p-pregunta-cerrada-detail',
  templateUrl: './p-pregunta-cerrada-detail.component.html',
})
export class PPreguntaCerradaDetailComponent implements OnInit {
  pPreguntaCerrada: IPPreguntaCerrada | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pPreguntaCerrada }) => {
      this.pPreguntaCerrada = pPreguntaCerrada;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPPreguntaAbierta } from '../p-pregunta-abierta.model';

@Component({
  selector: 'jhi-p-pregunta-abierta-detail',
  templateUrl: './p-pregunta-abierta-detail.component.html',
})
export class PPreguntaAbiertaDetailComponent implements OnInit {
  pPreguntaAbierta: IPPreguntaAbierta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pPreguntaAbierta }) => {
      this.pPreguntaAbierta = pPreguntaAbierta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

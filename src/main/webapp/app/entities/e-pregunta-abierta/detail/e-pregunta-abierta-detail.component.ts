import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEPreguntaAbierta } from '../e-pregunta-abierta.model';

@Component({
  selector: 'jhi-e-pregunta-abierta-detail',
  templateUrl: './e-pregunta-abierta-detail.component.html',
})
export class EPreguntaAbiertaDetailComponent implements OnInit {
  ePreguntaAbierta: IEPreguntaAbierta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ePreguntaAbierta }) => {
      this.ePreguntaAbierta = ePreguntaAbierta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

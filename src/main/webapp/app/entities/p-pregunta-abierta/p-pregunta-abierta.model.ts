import { IPlantilla } from 'app/entities/plantilla/plantilla.model';

export interface IPPreguntaAbierta {
  id?: number;
  nombre?: string;
  opcional?: boolean;
  orden?: number;
  plantilla?: IPlantilla | null;
}

export class PPreguntaAbierta implements IPPreguntaAbierta {
  constructor(
    public id?: number,
    public nombre?: string,
    public opcional?: boolean,
    public orden?: number,
    public plantilla?: IPlantilla | null
  ) {
    this.opcional = this.opcional ?? false;
  }
}

export function getPPreguntaAbiertaIdentifier(pPreguntaAbierta: IPPreguntaAbierta): number | undefined {
  return pPreguntaAbierta.id;
}

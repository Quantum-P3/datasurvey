import { IPPreguntaCerradaOpcion } from 'app/entities/p-pregunta-cerrada-opcion/p-pregunta-cerrada-opcion.model';
import { IPlantilla } from 'app/entities/plantilla/plantilla.model';
import { PreguntaCerradaTipo } from 'app/entities/enumerations/pregunta-cerrada-tipo.model';

export interface IPPreguntaCerrada {
  id?: number;
  nombre?: string;
  tipo?: PreguntaCerradaTipo;
  opcional?: boolean;
  orden?: number;
  pPreguntaCerradaOpcions?: IPPreguntaCerradaOpcion[] | null;
  plantilla?: IPlantilla | null;
}

export class PPreguntaCerrada implements IPPreguntaCerrada {
  constructor(
    public id?: number,
    public nombre?: string,
    public tipo?: PreguntaCerradaTipo,
    public opcional?: boolean,
    public orden?: number,
    public pPreguntaCerradaOpcions?: IPPreguntaCerradaOpcion[] | null,
    public plantilla?: IPlantilla | null
  ) {
    this.opcional = this.opcional ?? false;
  }
}

export function getPPreguntaCerradaIdentifier(pPreguntaCerrada: IPPreguntaCerrada): number | undefined {
  return pPreguntaCerrada.id;
}

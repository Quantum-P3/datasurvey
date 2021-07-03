import { IEPreguntaCerradaOpcion } from 'app/entities/e-pregunta-cerrada-opcion/e-pregunta-cerrada-opcion.model';
import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { PreguntaCerradaTipo } from 'app/entities/enumerations/pregunta-cerrada-tipo.model';

export interface IEPreguntaCerrada {
  id?: number;
  nombre?: string;
  tipo?: PreguntaCerradaTipo;
  opcional?: boolean;
  orden?: number;
  ePreguntaCerradaOpcions?: IEPreguntaCerradaOpcion[] | null;
  encuesta?: IEncuesta | null;
}

export class EPreguntaCerrada implements IEPreguntaCerrada {
  constructor(
    public id?: number,
    public nombre?: string,
    public tipo?: PreguntaCerradaTipo,
    public opcional?: boolean,
    public orden?: number,
    public ePreguntaCerradaOpcions?: IEPreguntaCerradaOpcion[] | null,
    public encuesta?: IEncuesta | null
  ) {
    this.opcional = this.opcional ?? false;
  }
}

export function getEPreguntaCerradaIdentifier(ePreguntaCerrada: IEPreguntaCerrada): number | undefined {
  return ePreguntaCerrada.id;
}

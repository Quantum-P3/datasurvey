import { IEPreguntaCerrada } from 'app/entities/e-pregunta-cerrada/e-pregunta-cerrada.model';

export interface IEPreguntaCerradaOpcion {
  id?: number;
  nombre?: string;
  orden?: number;
  cantidad?: number;
  ePreguntaCerrada?: IEPreguntaCerrada | null;
}

export class EPreguntaCerradaOpcion implements IEPreguntaCerradaOpcion {
  constructor(
    public id?: number,
    public nombre?: string,
    public orden?: number,
    public cantidad?: number,
    public ePreguntaCerrada?: IEPreguntaCerrada | null
  ) {}
}

export function getEPreguntaCerradaOpcionIdentifier(ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion): number | undefined {
  return ePreguntaCerradaOpcion.id;
}

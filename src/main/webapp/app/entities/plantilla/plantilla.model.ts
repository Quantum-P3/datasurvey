import * as dayjs from 'dayjs';
import { IPPreguntaCerrada } from 'app/entities/p-pregunta-cerrada/p-pregunta-cerrada.model';
import { IPPreguntaAbierta } from 'app/entities/p-pregunta-abierta/p-pregunta-abierta.model';
import { ICategoria } from 'app/entities/categoria/categoria.model';
import { IUsuarioExtra } from 'app/entities/usuario-extra/usuario-extra.model';
import { EstadoPlantilla } from 'app/entities/enumerations/estado-plantilla.model';

export interface IPlantilla {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  fechaCreacion?: dayjs.Dayjs;
  fechaPublicacionTienda?: dayjs.Dayjs | null;
  estado?: EstadoPlantilla;
  precio?: number;
  pPreguntaCerradas?: IPPreguntaCerrada[] | null;
  pPreguntaAbiertas?: IPPreguntaAbierta[] | null;
  categoria?: ICategoria | null;
  usuarioExtras?: IUsuarioExtra[] | null;
}

export class Plantilla implements IPlantilla {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: string | null,
    public fechaCreacion?: dayjs.Dayjs,
    public fechaPublicacionTienda?: dayjs.Dayjs | null,
    public estado?: EstadoPlantilla,
    public precio?: number,
    public pPreguntaCerradas?: IPPreguntaCerrada[] | null,
    public pPreguntaAbiertas?: IPPreguntaAbierta[] | null,
    public categoria?: ICategoria | null,
    public usuarioExtras?: IUsuarioExtra[] | null
  ) {}
}

export function getPlantillaIdentifier(plantilla: IPlantilla): number | undefined {
  return plantilla.id;
}

import { IEncuesta } from 'app/entities/encuesta/encuesta.model';
import { IPlantilla } from 'app/entities/plantilla/plantilla.model';
import { EstadoCategoria } from 'app/entities/enumerations/estado-categoria.model';

export interface ICategoria {
  id?: number;
  nombre?: string;
  estado?: EstadoCategoria;
  encuestas?: IEncuesta[] | null;
  plantillas?: IPlantilla[] | null;
}

export class Categoria implements ICategoria {
  constructor(
    public id?: number,
    public nombre?: string,
    public estado?: EstadoCategoria,
    public encuestas?: IEncuesta[] | null,
    public plantillas?: IPlantilla[] | null
  ) {}
}

export function getCategoriaIdentifier(categoria: ICategoria): number | undefined {
  return categoria.id;
}

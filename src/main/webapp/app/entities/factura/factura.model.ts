import * as dayjs from 'dayjs';

export interface IFactura {
  id?: number;
  nombreUsuario?: string;
  nombrePlantilla?: string;
  costo?: number;
  fecha?: dayjs.Dayjs;
}

export class Factura implements IFactura {
  constructor(
    public id?: number,
    public nombreUsuario?: string,
    public nombrePlantilla?: string,
    public costo?: number,
    public fecha?: dayjs.Dayjs
  ) {}
}

export function getFacturaIdentifier(factura: IFactura): number | undefined {
  return factura.id;
}

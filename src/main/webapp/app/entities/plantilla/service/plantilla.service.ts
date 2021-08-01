import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlantilla, getPlantillaIdentifier } from '../plantilla.model';

export type EntityResponseType = HttpResponse<IPlantilla>;
export type EntityArrayResponseType = HttpResponse<IPlantilla[]>;

@Injectable({ providedIn: 'root' })
export class PlantillaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/plantillas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(plantilla: IPlantilla): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plantilla);
    return this.http
      .post<IPlantilla>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(plantilla: IPlantilla): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plantilla);
    return this.http
      .put<IPlantilla>(`${this.resourceUrl}/${getPlantillaIdentifier(plantilla) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(plantilla: IPlantilla): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(plantilla);
    return this.http
      .patch<IPlantilla>(`${this.resourceUrl}/${getPlantillaIdentifier(plantilla) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPlantilla>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findPlantilla(id: number): Observable<IPlantilla> {
    return this.http.get<IPlantilla>(`${this.resourceUrl}/${id}`);
  }

  findQuestions(id: number): Observable<EntityResponseType> {
    return this.http
      .get<any>(`${this.resourceUrl}/preguntas/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findQuestionsOptions(id: number): Observable<EntityResponseType> {
    return this.http
      .get<any>(`${this.resourceUrl}/preguntas-opciones/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPlantilla[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlantillaToCollectionIfMissing(
    plantillaCollection: IPlantilla[],
    ...plantillasToCheck: (IPlantilla | null | undefined)[]
  ): IPlantilla[] {
    const plantillas: IPlantilla[] = plantillasToCheck.filter(isPresent);
    if (plantillas.length > 0) {
      const plantillaCollectionIdentifiers = plantillaCollection.map(plantillaItem => getPlantillaIdentifier(plantillaItem)!);
      const plantillasToAdd = plantillas.filter(plantillaItem => {
        const plantillaIdentifier = getPlantillaIdentifier(plantillaItem);
        if (plantillaIdentifier == null || plantillaCollectionIdentifiers.includes(plantillaIdentifier)) {
          return false;
        }
        plantillaCollectionIdentifiers.push(plantillaIdentifier);
        return true;
      });
      return [...plantillasToAdd, ...plantillaCollection];
    }
    return plantillaCollection;
  }

  protected convertDateFromClient(plantilla: IPlantilla): IPlantilla {
    return Object.assign({}, plantilla, {
      fechaCreacion: plantilla.fechaCreacion?.isValid() ? plantilla.fechaCreacion.toJSON() : undefined,
      fechaPublicacionTienda: plantilla.fechaPublicacionTienda?.isValid() ? plantilla.fechaPublicacionTienda.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaCreacion = res.body.fechaCreacion ? dayjs(res.body.fechaCreacion) : undefined;
      res.body.fechaPublicacionTienda = res.body.fechaPublicacionTienda ? dayjs(res.body.fechaPublicacionTienda) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((plantilla: IPlantilla) => {
        plantilla.fechaCreacion = plantilla.fechaCreacion ? dayjs(plantilla.fechaCreacion) : undefined;
        plantilla.fechaPublicacionTienda = plantilla.fechaPublicacionTienda ? dayjs(plantilla.fechaPublicacionTienda) : undefined;
      });
    }
    return res;
  }
}

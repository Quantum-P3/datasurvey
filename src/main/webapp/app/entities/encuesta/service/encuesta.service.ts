import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEncuesta, getEncuestaIdentifier } from '../encuesta.model';

export type EntityResponseType = HttpResponse<IEncuesta>;
export type EntityArrayResponseType = HttpResponse<IEncuesta[]>;

@Injectable({ providedIn: 'root' })
export class EncuestaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/encuestas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(encuesta: IEncuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(encuesta);
    return this.http
      .post<IEncuesta>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(encuesta: IEncuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(encuesta);
    return this.http
      .put<IEncuesta>(`${this.resourceUrl}/${getEncuestaIdentifier(encuesta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(encuesta: IEncuesta): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(encuesta);
    return this.http
      .patch<IEncuesta>(`${this.resourceUrl}/${getEncuestaIdentifier(encuesta) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: Number): Observable<EntityResponseType> {
    return this.http
      .get<IEncuesta>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findEncuesta(id: number): Observable<IEncuesta> {
    return this.http.get<IEncuesta>(`${this.resourceUrl}/${id}`);
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

  findEncuesta(id: number): Observable<IEncuesta> {
    return this.http.get<IEncuesta>(`${this.resourceUrl}/${id}`);
  }

  deleteEncuesta(encuesta: IEncuesta): Observable<EntityResponseType> {
    //const copy = this.convertDateFromClient(encuesta);
    return this.http.put<IEncuesta>(`${this.resourceUrl}/${getEncuestaIdentifier(encuesta) as number}`, encuesta, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEncuesta[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEncuestaToCollectionIfMissing(encuestaCollection: IEncuesta[], ...encuestasToCheck: (IEncuesta | null | undefined)[]): IEncuesta[] {
    const encuestas: IEncuesta[] = encuestasToCheck.filter(isPresent);
    if (encuestas.length > 0) {
      const encuestaCollectionIdentifiers = encuestaCollection.map(encuestaItem => getEncuestaIdentifier(encuestaItem)!);
      const encuestasToAdd = encuestas.filter(encuestaItem => {
        const encuestaIdentifier = getEncuestaIdentifier(encuestaItem);
        if (encuestaIdentifier == null || encuestaCollectionIdentifiers.includes(encuestaIdentifier)) {
          return false;
        }
        encuestaCollectionIdentifiers.push(encuestaIdentifier);
        return true;
      });
      return [...encuestasToAdd, ...encuestaCollection];
    }
    return encuestaCollection;
  }

  protected convertDateFromClient(encuesta: IEncuesta): IEncuesta {
    return Object.assign({}, encuesta, {
      fechaCreacion: encuesta.fechaCreacion?.isValid() ? encuesta.fechaCreacion.toJSON() : undefined,
      fechaPublicacion: encuesta.fechaPublicacion?.isValid() ? encuesta.fechaPublicacion.toJSON() : undefined,
      fechaFinalizar: encuesta.fechaFinalizar?.isValid() ? encuesta.fechaFinalizar.toJSON() : undefined,
      fechaFinalizada: encuesta.fechaFinalizada?.isValid() ? encuesta.fechaFinalizada.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaCreacion = res.body.fechaCreacion ? dayjs(res.body.fechaCreacion) : undefined;
      res.body.fechaPublicacion = res.body.fechaPublicacion ? dayjs(res.body.fechaPublicacion) : undefined;
      res.body.fechaFinalizar = res.body.fechaFinalizar ? dayjs(res.body.fechaFinalizar) : undefined;
      res.body.fechaFinalizada = res.body.fechaFinalizada ? dayjs(res.body.fechaFinalizada) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((encuesta: IEncuesta) => {
        encuesta.fechaCreacion = encuesta.fechaCreacion ? dayjs(encuesta.fechaCreacion) : undefined;
        encuesta.fechaPublicacion = encuesta.fechaPublicacion ? dayjs(encuesta.fechaPublicacion) : undefined;
        encuesta.fechaFinalizar = encuesta.fechaFinalizar ? dayjs(encuesta.fechaFinalizar) : undefined;
        encuesta.fechaFinalizada = encuesta.fechaFinalizada ? dayjs(encuesta.fechaFinalizada) : undefined;
      });
    }
    return res;
  }
}

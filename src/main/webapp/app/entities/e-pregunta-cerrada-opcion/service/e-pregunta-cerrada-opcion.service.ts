import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEPreguntaCerradaOpcion, getEPreguntaCerradaOpcionIdentifier } from '../e-pregunta-cerrada-opcion.model';

export type EntityResponseType = HttpResponse<IEPreguntaCerradaOpcion>;
export type EntityArrayResponseType = HttpResponse<IEPreguntaCerradaOpcion[]>;

@Injectable({ providedIn: 'root' })
export class EPreguntaCerradaOpcionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/e-pregunta-cerrada-opcions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  updateCount(id: number) {
    debugger;
    return this.http.post(`${this.resourceUrl}/count/${id}`, id, { observe: 'response' });
  }

  create(ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion, preguntaId?: number): Observable<EntityResponseType> {
    return this.http.post<IEPreguntaCerradaOpcion>(`${this.resourceUrl}/${preguntaId}`, ePreguntaCerradaOpcion, { observe: 'response' });
  }

  update(ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion): Observable<EntityResponseType> {
    return this.http.put<IEPreguntaCerradaOpcion>(
      `${this.resourceUrl}/${getEPreguntaCerradaOpcionIdentifier(ePreguntaCerradaOpcion) as number}`,
      ePreguntaCerradaOpcion,
      { observe: 'response' }
    );
  }

  partialUpdate(ePreguntaCerradaOpcion: IEPreguntaCerradaOpcion): Observable<EntityResponseType> {
    return this.http.patch<IEPreguntaCerradaOpcion>(
      `${this.resourceUrl}/${getEPreguntaCerradaOpcionIdentifier(ePreguntaCerradaOpcion) as number}`,
      ePreguntaCerradaOpcion,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEPreguntaCerradaOpcion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEPreguntaCerradaOpcion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  deleteMany(ids: number[]): Observable<EntityResponseType> {
    return this.http.post<IEPreguntaCerradaOpcion>(`${this.resourceUrl}/deleteMany`, ids, { observe: 'response' });
  }

  addEPreguntaCerradaOpcionToCollectionIfMissing(
    ePreguntaCerradaOpcionCollection: IEPreguntaCerradaOpcion[],
    ...ePreguntaCerradaOpcionsToCheck: (IEPreguntaCerradaOpcion | null | undefined)[]
  ): IEPreguntaCerradaOpcion[] {
    const ePreguntaCerradaOpcions: IEPreguntaCerradaOpcion[] = ePreguntaCerradaOpcionsToCheck.filter(isPresent);
    if (ePreguntaCerradaOpcions.length > 0) {
      const ePreguntaCerradaOpcionCollectionIdentifiers = ePreguntaCerradaOpcionCollection.map(
        ePreguntaCerradaOpcionItem => getEPreguntaCerradaOpcionIdentifier(ePreguntaCerradaOpcionItem)!
      );
      const ePreguntaCerradaOpcionsToAdd = ePreguntaCerradaOpcions.filter(ePreguntaCerradaOpcionItem => {
        const ePreguntaCerradaOpcionIdentifier = getEPreguntaCerradaOpcionIdentifier(ePreguntaCerradaOpcionItem);
        if (
          ePreguntaCerradaOpcionIdentifier == null ||
          ePreguntaCerradaOpcionCollectionIdentifiers.includes(ePreguntaCerradaOpcionIdentifier)
        ) {
          return false;
        }
        ePreguntaCerradaOpcionCollectionIdentifiers.push(ePreguntaCerradaOpcionIdentifier);
        return true;
      });
      return [...ePreguntaCerradaOpcionsToAdd, ...ePreguntaCerradaOpcionCollection];
    }
    return ePreguntaCerradaOpcionCollection;
  }
}

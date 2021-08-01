import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPPreguntaCerradaOpcion, getPPreguntaCerradaOpcionIdentifier } from '../p-pregunta-cerrada-opcion.model';

export type EntityResponseType = HttpResponse<IPPreguntaCerradaOpcion>;
export type EntityArrayResponseType = HttpResponse<IPPreguntaCerradaOpcion[]>;

@Injectable({ providedIn: 'root' })
export class PPreguntaCerradaOpcionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/p-pregunta-cerrada-opcions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion, preguntaId?: number): Observable<EntityResponseType> {
    return this.http.post<IPPreguntaCerradaOpcion>(`${this.resourceUrl}/${preguntaId}`, pPreguntaCerradaOpcion, { observe: 'response' });
  }

  update(pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion): Observable<EntityResponseType> {
    return this.http.put<IPPreguntaCerradaOpcion>(
      `${this.resourceUrl}/${getPPreguntaCerradaOpcionIdentifier(pPreguntaCerradaOpcion) as number}`,
      pPreguntaCerradaOpcion,
      { observe: 'response' }
    );
  }

  partialUpdate(pPreguntaCerradaOpcion: IPPreguntaCerradaOpcion): Observable<EntityResponseType> {
    return this.http.patch<IPPreguntaCerradaOpcion>(
      `${this.resourceUrl}/${getPPreguntaCerradaOpcionIdentifier(pPreguntaCerradaOpcion) as number}`,
      pPreguntaCerradaOpcion,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPPreguntaCerradaOpcion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPPreguntaCerradaOpcion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  deleteMany(ids: number[]): Observable<EntityResponseType> {
    return this.http.post<IPPreguntaCerradaOpcion>(`${this.resourceUrl}/deleteMany`, ids, { observe: 'response' });
  }

  addPPreguntaCerradaOpcionToCollectionIfMissing(
    pPreguntaCerradaOpcionCollection: IPPreguntaCerradaOpcion[],
    ...pPreguntaCerradaOpcionsToCheck: (IPPreguntaCerradaOpcion | null | undefined)[]
  ): IPPreguntaCerradaOpcion[] {
    const pPreguntaCerradaOpcions: IPPreguntaCerradaOpcion[] = pPreguntaCerradaOpcionsToCheck.filter(isPresent);
    if (pPreguntaCerradaOpcions.length > 0) {
      const pPreguntaCerradaOpcionCollectionIdentifiers = pPreguntaCerradaOpcionCollection.map(
        pPreguntaCerradaOpcionItem => getPPreguntaCerradaOpcionIdentifier(pPreguntaCerradaOpcionItem)!
      );
      const pPreguntaCerradaOpcionsToAdd = pPreguntaCerradaOpcions.filter(pPreguntaCerradaOpcionItem => {
        const pPreguntaCerradaOpcionIdentifier = getPPreguntaCerradaOpcionIdentifier(pPreguntaCerradaOpcionItem);
        if (
          pPreguntaCerradaOpcionIdentifier == null ||
          pPreguntaCerradaOpcionCollectionIdentifiers.includes(pPreguntaCerradaOpcionIdentifier)
        ) {
          return false;
        }
        pPreguntaCerradaOpcionCollectionIdentifiers.push(pPreguntaCerradaOpcionIdentifier);
        return true;
      });
      return [...pPreguntaCerradaOpcionsToAdd, ...pPreguntaCerradaOpcionCollection];
    }
    return pPreguntaCerradaOpcionCollection;
  }
}

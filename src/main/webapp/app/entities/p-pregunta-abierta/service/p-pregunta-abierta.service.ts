import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPPreguntaAbierta, getPPreguntaAbiertaIdentifier } from '../p-pregunta-abierta.model';

export type EntityResponseType = HttpResponse<IPPreguntaAbierta>;
export type EntityArrayResponseType = HttpResponse<IPPreguntaAbierta[]>;

@Injectable({ providedIn: 'root' })
export class PPreguntaAbiertaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/p-pregunta-abiertas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pPreguntaAbierta: IPPreguntaAbierta): Observable<EntityResponseType> {
    return this.http.post<IPPreguntaAbierta>(this.resourceUrl, pPreguntaAbierta, { observe: 'response' });
  }

  update(pPreguntaAbierta: IPPreguntaAbierta): Observable<EntityResponseType> {
    return this.http.put<IPPreguntaAbierta>(
      `${this.resourceUrl}/${getPPreguntaAbiertaIdentifier(pPreguntaAbierta) as number}`,
      pPreguntaAbierta,
      { observe: 'response' }
    );
  }

  partialUpdate(pPreguntaAbierta: IPPreguntaAbierta): Observable<EntityResponseType> {
    return this.http.patch<IPPreguntaAbierta>(
      `${this.resourceUrl}/${getPPreguntaAbiertaIdentifier(pPreguntaAbierta) as number}`,
      pPreguntaAbierta,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPPreguntaAbierta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPPreguntaAbierta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPPreguntaAbiertaToCollectionIfMissing(
    pPreguntaAbiertaCollection: IPPreguntaAbierta[],
    ...pPreguntaAbiertasToCheck: (IPPreguntaAbierta | null | undefined)[]
  ): IPPreguntaAbierta[] {
    const pPreguntaAbiertas: IPPreguntaAbierta[] = pPreguntaAbiertasToCheck.filter(isPresent);
    if (pPreguntaAbiertas.length > 0) {
      const pPreguntaAbiertaCollectionIdentifiers = pPreguntaAbiertaCollection.map(
        pPreguntaAbiertaItem => getPPreguntaAbiertaIdentifier(pPreguntaAbiertaItem)!
      );
      const pPreguntaAbiertasToAdd = pPreguntaAbiertas.filter(pPreguntaAbiertaItem => {
        const pPreguntaAbiertaIdentifier = getPPreguntaAbiertaIdentifier(pPreguntaAbiertaItem);
        if (pPreguntaAbiertaIdentifier == null || pPreguntaAbiertaCollectionIdentifiers.includes(pPreguntaAbiertaIdentifier)) {
          return false;
        }
        pPreguntaAbiertaCollectionIdentifiers.push(pPreguntaAbiertaIdentifier);
        return true;
      });
      return [...pPreguntaAbiertasToAdd, ...pPreguntaAbiertaCollection];
    }
    return pPreguntaAbiertaCollection;
  }
}

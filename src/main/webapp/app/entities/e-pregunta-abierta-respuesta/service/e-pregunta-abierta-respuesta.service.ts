import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEPreguntaAbiertaRespuesta, getEPreguntaAbiertaRespuestaIdentifier } from '../e-pregunta-abierta-respuesta.model';

export type EntityResponseType = HttpResponse<IEPreguntaAbiertaRespuesta>;
export type EntityArrayResponseType = HttpResponse<IEPreguntaAbiertaRespuesta[]>;

@Injectable({ providedIn: 'root' })
export class EPreguntaAbiertaRespuestaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/e-pregunta-abierta-respuestas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta): Observable<EntityResponseType> {
    return this.http.post<IEPreguntaAbiertaRespuesta>(this.resourceUrl, ePreguntaAbiertaRespuesta, { observe: 'response' });
  }

  update(ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta): Observable<EntityResponseType> {
    return this.http.put<IEPreguntaAbiertaRespuesta>(
      `${this.resourceUrl}/${getEPreguntaAbiertaRespuestaIdentifier(ePreguntaAbiertaRespuesta) as number}`,
      ePreguntaAbiertaRespuesta,
      { observe: 'response' }
    );
  }

  partialUpdate(ePreguntaAbiertaRespuesta: IEPreguntaAbiertaRespuesta): Observable<EntityResponseType> {
    return this.http.patch<IEPreguntaAbiertaRespuesta>(
      `${this.resourceUrl}/${getEPreguntaAbiertaRespuestaIdentifier(ePreguntaAbiertaRespuesta) as number}`,
      ePreguntaAbiertaRespuesta,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEPreguntaAbiertaRespuesta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEPreguntaAbiertaRespuesta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEPreguntaAbiertaRespuestaToCollectionIfMissing(
    ePreguntaAbiertaRespuestaCollection: IEPreguntaAbiertaRespuesta[],
    ...ePreguntaAbiertaRespuestasToCheck: (IEPreguntaAbiertaRespuesta | null | undefined)[]
  ): IEPreguntaAbiertaRespuesta[] {
    const ePreguntaAbiertaRespuestas: IEPreguntaAbiertaRespuesta[] = ePreguntaAbiertaRespuestasToCheck.filter(isPresent);
    if (ePreguntaAbiertaRespuestas.length > 0) {
      const ePreguntaAbiertaRespuestaCollectionIdentifiers = ePreguntaAbiertaRespuestaCollection.map(
        ePreguntaAbiertaRespuestaItem => getEPreguntaAbiertaRespuestaIdentifier(ePreguntaAbiertaRespuestaItem)!
      );
      const ePreguntaAbiertaRespuestasToAdd = ePreguntaAbiertaRespuestas.filter(ePreguntaAbiertaRespuestaItem => {
        const ePreguntaAbiertaRespuestaIdentifier = getEPreguntaAbiertaRespuestaIdentifier(ePreguntaAbiertaRespuestaItem);
        if (
          ePreguntaAbiertaRespuestaIdentifier == null ||
          ePreguntaAbiertaRespuestaCollectionIdentifiers.includes(ePreguntaAbiertaRespuestaIdentifier)
        ) {
          return false;
        }
        ePreguntaAbiertaRespuestaCollectionIdentifiers.push(ePreguntaAbiertaRespuestaIdentifier);
        return true;
      });
      return [...ePreguntaAbiertaRespuestasToAdd, ...ePreguntaAbiertaRespuestaCollection];
    }
    return ePreguntaAbiertaRespuestaCollection;
  }
}

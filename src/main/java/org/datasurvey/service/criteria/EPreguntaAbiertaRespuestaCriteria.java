package org.datasurvey.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.datasurvey.domain.EPreguntaAbiertaRespuesta} entity. This class is used
 * in {@link org.datasurvey.web.rest.EPreguntaAbiertaRespuestaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /e-pregunta-abierta-respuestas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EPreguntaAbiertaRespuestaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter respuesta;

    private LongFilter ePreguntaAbiertaId;

    public EPreguntaAbiertaRespuestaCriteria() {}

    public EPreguntaAbiertaRespuestaCriteria(EPreguntaAbiertaRespuestaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.respuesta = other.respuesta == null ? null : other.respuesta.copy();
        this.ePreguntaAbiertaId = other.ePreguntaAbiertaId == null ? null : other.ePreguntaAbiertaId.copy();
    }

    @Override
    public EPreguntaAbiertaRespuestaCriteria copy() {
        return new EPreguntaAbiertaRespuestaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRespuesta() {
        return respuesta;
    }

    public StringFilter respuesta() {
        if (respuesta == null) {
            respuesta = new StringFilter();
        }
        return respuesta;
    }

    public void setRespuesta(StringFilter respuesta) {
        this.respuesta = respuesta;
    }

    public LongFilter getEPreguntaAbiertaId() {
        return ePreguntaAbiertaId;
    }

    public LongFilter ePreguntaAbiertaId() {
        if (ePreguntaAbiertaId == null) {
            ePreguntaAbiertaId = new LongFilter();
        }
        return ePreguntaAbiertaId;
    }

    public void setEPreguntaAbiertaId(LongFilter ePreguntaAbiertaId) {
        this.ePreguntaAbiertaId = ePreguntaAbiertaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EPreguntaAbiertaRespuestaCriteria that = (EPreguntaAbiertaRespuestaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(respuesta, that.respuesta) &&
            Objects.equals(ePreguntaAbiertaId, that.ePreguntaAbiertaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, respuesta, ePreguntaAbiertaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EPreguntaAbiertaRespuestaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (respuesta != null ? "respuesta=" + respuesta + ", " : "") +
            (ePreguntaAbiertaId != null ? "ePreguntaAbiertaId=" + ePreguntaAbiertaId + ", " : "") +
            "}";
    }
}

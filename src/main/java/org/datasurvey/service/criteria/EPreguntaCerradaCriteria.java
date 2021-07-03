package org.datasurvey.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.datasurvey.domain.enumeration.PreguntaCerradaTipo;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.datasurvey.domain.EPreguntaCerrada} entity. This class is used
 * in {@link org.datasurvey.web.rest.EPreguntaCerradaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /e-pregunta-cerradas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EPreguntaCerradaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PreguntaCerradaTipo
     */
    public static class PreguntaCerradaTipoFilter extends Filter<PreguntaCerradaTipo> {

        public PreguntaCerradaTipoFilter() {}

        public PreguntaCerradaTipoFilter(PreguntaCerradaTipoFilter filter) {
            super(filter);
        }

        @Override
        public PreguntaCerradaTipoFilter copy() {
            return new PreguntaCerradaTipoFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private PreguntaCerradaTipoFilter tipo;

    private BooleanFilter opcional;

    private IntegerFilter orden;

    private LongFilter ePreguntaCerradaOpcionId;

    private LongFilter encuestaId;

    public EPreguntaCerradaCriteria() {}

    public EPreguntaCerradaCriteria(EPreguntaCerradaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.opcional = other.opcional == null ? null : other.opcional.copy();
        this.orden = other.orden == null ? null : other.orden.copy();
        this.ePreguntaCerradaOpcionId = other.ePreguntaCerradaOpcionId == null ? null : other.ePreguntaCerradaOpcionId.copy();
        this.encuestaId = other.encuestaId == null ? null : other.encuestaId.copy();
    }

    @Override
    public EPreguntaCerradaCriteria copy() {
        return new EPreguntaCerradaCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public StringFilter nombre() {
        if (nombre == null) {
            nombre = new StringFilter();
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public PreguntaCerradaTipoFilter getTipo() {
        return tipo;
    }

    public PreguntaCerradaTipoFilter tipo() {
        if (tipo == null) {
            tipo = new PreguntaCerradaTipoFilter();
        }
        return tipo;
    }

    public void setTipo(PreguntaCerradaTipoFilter tipo) {
        this.tipo = tipo;
    }

    public BooleanFilter getOpcional() {
        return opcional;
    }

    public BooleanFilter opcional() {
        if (opcional == null) {
            opcional = new BooleanFilter();
        }
        return opcional;
    }

    public void setOpcional(BooleanFilter opcional) {
        this.opcional = opcional;
    }

    public IntegerFilter getOrden() {
        return orden;
    }

    public IntegerFilter orden() {
        if (orden == null) {
            orden = new IntegerFilter();
        }
        return orden;
    }

    public void setOrden(IntegerFilter orden) {
        this.orden = orden;
    }

    public LongFilter getEPreguntaCerradaOpcionId() {
        return ePreguntaCerradaOpcionId;
    }

    public LongFilter ePreguntaCerradaOpcionId() {
        if (ePreguntaCerradaOpcionId == null) {
            ePreguntaCerradaOpcionId = new LongFilter();
        }
        return ePreguntaCerradaOpcionId;
    }

    public void setEPreguntaCerradaOpcionId(LongFilter ePreguntaCerradaOpcionId) {
        this.ePreguntaCerradaOpcionId = ePreguntaCerradaOpcionId;
    }

    public LongFilter getEncuestaId() {
        return encuestaId;
    }

    public LongFilter encuestaId() {
        if (encuestaId == null) {
            encuestaId = new LongFilter();
        }
        return encuestaId;
    }

    public void setEncuestaId(LongFilter encuestaId) {
        this.encuestaId = encuestaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EPreguntaCerradaCriteria that = (EPreguntaCerradaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(opcional, that.opcional) &&
            Objects.equals(orden, that.orden) &&
            Objects.equals(ePreguntaCerradaOpcionId, that.ePreguntaCerradaOpcionId) &&
            Objects.equals(encuestaId, that.encuestaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, tipo, opcional, orden, ePreguntaCerradaOpcionId, encuestaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EPreguntaCerradaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (tipo != null ? "tipo=" + tipo + ", " : "") +
            (opcional != null ? "opcional=" + opcional + ", " : "") +
            (orden != null ? "orden=" + orden + ", " : "") +
            (ePreguntaCerradaOpcionId != null ? "ePreguntaCerradaOpcionId=" + ePreguntaCerradaOpcionId + ", " : "") +
            (encuestaId != null ? "encuestaId=" + encuestaId + ", " : "") +
            "}";
    }
}

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
 * Criteria class for the {@link org.datasurvey.domain.ParametroAplicacion} entity. This class is used
 * in {@link org.datasurvey.web.rest.ParametroAplicacionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /parametro-aplicacions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ParametroAplicacionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter maxDiasEncuesta;

    private IntegerFilter minDiasEncuesta;

    private IntegerFilter maxCantidadPreguntas;

    private IntegerFilter minCantidadPreguntas;

    public ParametroAplicacionCriteria() {}

    public ParametroAplicacionCriteria(ParametroAplicacionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.maxDiasEncuesta = other.maxDiasEncuesta == null ? null : other.maxDiasEncuesta.copy();
        this.minDiasEncuesta = other.minDiasEncuesta == null ? null : other.minDiasEncuesta.copy();
        this.maxCantidadPreguntas = other.maxCantidadPreguntas == null ? null : other.maxCantidadPreguntas.copy();
        this.minCantidadPreguntas = other.minCantidadPreguntas == null ? null : other.minCantidadPreguntas.copy();
    }

    @Override
    public ParametroAplicacionCriteria copy() {
        return new ParametroAplicacionCriteria(this);
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

    public IntegerFilter getMaxDiasEncuesta() {
        return maxDiasEncuesta;
    }

    public IntegerFilter maxDiasEncuesta() {
        if (maxDiasEncuesta == null) {
            maxDiasEncuesta = new IntegerFilter();
        }
        return maxDiasEncuesta;
    }

    public void setMaxDiasEncuesta(IntegerFilter maxDiasEncuesta) {
        this.maxDiasEncuesta = maxDiasEncuesta;
    }

    public IntegerFilter getMinDiasEncuesta() {
        return minDiasEncuesta;
    }

    public IntegerFilter minDiasEncuesta() {
        if (minDiasEncuesta == null) {
            minDiasEncuesta = new IntegerFilter();
        }
        return minDiasEncuesta;
    }

    public void setMinDiasEncuesta(IntegerFilter minDiasEncuesta) {
        this.minDiasEncuesta = minDiasEncuesta;
    }

    public IntegerFilter getMaxCantidadPreguntas() {
        return maxCantidadPreguntas;
    }

    public IntegerFilter maxCantidadPreguntas() {
        if (maxCantidadPreguntas == null) {
            maxCantidadPreguntas = new IntegerFilter();
        }
        return maxCantidadPreguntas;
    }

    public void setMaxCantidadPreguntas(IntegerFilter maxCantidadPreguntas) {
        this.maxCantidadPreguntas = maxCantidadPreguntas;
    }

    public IntegerFilter getMinCantidadPreguntas() {
        return minCantidadPreguntas;
    }

    public IntegerFilter minCantidadPreguntas() {
        if (minCantidadPreguntas == null) {
            minCantidadPreguntas = new IntegerFilter();
        }
        return minCantidadPreguntas;
    }

    public void setMinCantidadPreguntas(IntegerFilter minCantidadPreguntas) {
        this.minCantidadPreguntas = minCantidadPreguntas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ParametroAplicacionCriteria that = (ParametroAplicacionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(maxDiasEncuesta, that.maxDiasEncuesta) &&
            Objects.equals(minDiasEncuesta, that.minDiasEncuesta) &&
            Objects.equals(maxCantidadPreguntas, that.maxCantidadPreguntas) &&
            Objects.equals(minCantidadPreguntas, that.minCantidadPreguntas)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, maxDiasEncuesta, minDiasEncuesta, maxCantidadPreguntas, minCantidadPreguntas);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParametroAplicacionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (maxDiasEncuesta != null ? "maxDiasEncuesta=" + maxDiasEncuesta + ", " : "") +
            (minDiasEncuesta != null ? "minDiasEncuesta=" + minDiasEncuesta + ", " : "") +
            (maxCantidadPreguntas != null ? "maxCantidadPreguntas=" + maxCantidadPreguntas + ", " : "") +
            (minCantidadPreguntas != null ? "minCantidadPreguntas=" + minCantidadPreguntas + ", " : "") +
            "}";
    }
}

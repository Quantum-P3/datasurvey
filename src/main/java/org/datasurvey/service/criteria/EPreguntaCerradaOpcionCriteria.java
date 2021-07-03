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
 * Criteria class for the {@link org.datasurvey.domain.EPreguntaCerradaOpcion} entity. This class is used
 * in {@link org.datasurvey.web.rest.EPreguntaCerradaOpcionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /e-pregunta-cerrada-opcions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EPreguntaCerradaOpcionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private IntegerFilter orden;

    private IntegerFilter cantidad;

    private LongFilter ePreguntaCerradaId;

    public EPreguntaCerradaOpcionCriteria() {}

    public EPreguntaCerradaOpcionCriteria(EPreguntaCerradaOpcionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.orden = other.orden == null ? null : other.orden.copy();
        this.cantidad = other.cantidad == null ? null : other.cantidad.copy();
        this.ePreguntaCerradaId = other.ePreguntaCerradaId == null ? null : other.ePreguntaCerradaId.copy();
    }

    @Override
    public EPreguntaCerradaOpcionCriteria copy() {
        return new EPreguntaCerradaOpcionCriteria(this);
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

    public IntegerFilter getCantidad() {
        return cantidad;
    }

    public IntegerFilter cantidad() {
        if (cantidad == null) {
            cantidad = new IntegerFilter();
        }
        return cantidad;
    }

    public void setCantidad(IntegerFilter cantidad) {
        this.cantidad = cantidad;
    }

    public LongFilter getEPreguntaCerradaId() {
        return ePreguntaCerradaId;
    }

    public LongFilter ePreguntaCerradaId() {
        if (ePreguntaCerradaId == null) {
            ePreguntaCerradaId = new LongFilter();
        }
        return ePreguntaCerradaId;
    }

    public void setEPreguntaCerradaId(LongFilter ePreguntaCerradaId) {
        this.ePreguntaCerradaId = ePreguntaCerradaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EPreguntaCerradaOpcionCriteria that = (EPreguntaCerradaOpcionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(orden, that.orden) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(ePreguntaCerradaId, that.ePreguntaCerradaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, orden, cantidad, ePreguntaCerradaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EPreguntaCerradaOpcionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (orden != null ? "orden=" + orden + ", " : "") +
            (cantidad != null ? "cantidad=" + cantidad + ", " : "") +
            (ePreguntaCerradaId != null ? "ePreguntaCerradaId=" + ePreguntaCerradaId + ", " : "") +
            "}";
    }
}

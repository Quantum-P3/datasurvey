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
 * Criteria class for the {@link org.datasurvey.domain.PPreguntaAbierta} entity. This class is used
 * in {@link org.datasurvey.web.rest.PPreguntaAbiertaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /p-pregunta-abiertas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PPreguntaAbiertaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private BooleanFilter opcional;

    private IntegerFilter orden;

    private LongFilter plantillaId;

    public PPreguntaAbiertaCriteria() {}

    public PPreguntaAbiertaCriteria(PPreguntaAbiertaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.opcional = other.opcional == null ? null : other.opcional.copy();
        this.orden = other.orden == null ? null : other.orden.copy();
        this.plantillaId = other.plantillaId == null ? null : other.plantillaId.copy();
    }

    @Override
    public PPreguntaAbiertaCriteria copy() {
        return new PPreguntaAbiertaCriteria(this);
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

    public LongFilter getPlantillaId() {
        return plantillaId;
    }

    public LongFilter plantillaId() {
        if (plantillaId == null) {
            plantillaId = new LongFilter();
        }
        return plantillaId;
    }

    public void setPlantillaId(LongFilter plantillaId) {
        this.plantillaId = plantillaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PPreguntaAbiertaCriteria that = (PPreguntaAbiertaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(opcional, that.opcional) &&
            Objects.equals(orden, that.orden) &&
            Objects.equals(plantillaId, that.plantillaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, opcional, orden, plantillaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PPreguntaAbiertaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (opcional != null ? "opcional=" + opcional + ", " : "") +
            (orden != null ? "orden=" + orden + ", " : "") +
            (plantillaId != null ? "plantillaId=" + plantillaId + ", " : "") +
            "}";
    }
}

package org.datasurvey.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.datasurvey.domain.enumeration.EstadoCategoria;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link org.datasurvey.domain.Categoria} entity. This class is used
 * in {@link org.datasurvey.web.rest.CategoriaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /categorias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CategoriaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstadoCategoria
     */
    public static class EstadoCategoriaFilter extends Filter<EstadoCategoria> {

        public EstadoCategoriaFilter() {}

        public EstadoCategoriaFilter(EstadoCategoriaFilter filter) {
            super(filter);
        }

        @Override
        public EstadoCategoriaFilter copy() {
            return new EstadoCategoriaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private EstadoCategoriaFilter estado;

    private LongFilter encuestaId;

    private LongFilter plantillaId;

    public CategoriaCriteria() {}

    public CategoriaCriteria(CategoriaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.encuestaId = other.encuestaId == null ? null : other.encuestaId.copy();
        this.plantillaId = other.plantillaId == null ? null : other.plantillaId.copy();
    }

    @Override
    public CategoriaCriteria copy() {
        return new CategoriaCriteria(this);
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

    public EstadoCategoriaFilter getEstado() {
        return estado;
    }

    public EstadoCategoriaFilter estado() {
        if (estado == null) {
            estado = new EstadoCategoriaFilter();
        }
        return estado;
    }

    public void setEstado(EstadoCategoriaFilter estado) {
        this.estado = estado;
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
        final CategoriaCriteria that = (CategoriaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(encuestaId, that.encuestaId) &&
            Objects.equals(plantillaId, that.plantillaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, estado, encuestaId, plantillaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoriaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (encuestaId != null ? "encuestaId=" + encuestaId + ", " : "") +
            (plantillaId != null ? "plantillaId=" + plantillaId + ", " : "") +
            "}";
    }
}

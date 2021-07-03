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
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link org.datasurvey.domain.Factura} entity. This class is used
 * in {@link org.datasurvey.web.rest.FacturaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /facturas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FacturaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombreUsuario;

    private StringFilter nombrePlantilla;

    private DoubleFilter costo;

    private ZonedDateTimeFilter fecha;

    public FacturaCriteria() {}

    public FacturaCriteria(FacturaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombreUsuario = other.nombreUsuario == null ? null : other.nombreUsuario.copy();
        this.nombrePlantilla = other.nombrePlantilla == null ? null : other.nombrePlantilla.copy();
        this.costo = other.costo == null ? null : other.costo.copy();
        this.fecha = other.fecha == null ? null : other.fecha.copy();
    }

    @Override
    public FacturaCriteria copy() {
        return new FacturaCriteria(this);
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

    public StringFilter getNombreUsuario() {
        return nombreUsuario;
    }

    public StringFilter nombreUsuario() {
        if (nombreUsuario == null) {
            nombreUsuario = new StringFilter();
        }
        return nombreUsuario;
    }

    public void setNombreUsuario(StringFilter nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public StringFilter getNombrePlantilla() {
        return nombrePlantilla;
    }

    public StringFilter nombrePlantilla() {
        if (nombrePlantilla == null) {
            nombrePlantilla = new StringFilter();
        }
        return nombrePlantilla;
    }

    public void setNombrePlantilla(StringFilter nombrePlantilla) {
        this.nombrePlantilla = nombrePlantilla;
    }

    public DoubleFilter getCosto() {
        return costo;
    }

    public DoubleFilter costo() {
        if (costo == null) {
            costo = new DoubleFilter();
        }
        return costo;
    }

    public void setCosto(DoubleFilter costo) {
        this.costo = costo;
    }

    public ZonedDateTimeFilter getFecha() {
        return fecha;
    }

    public ZonedDateTimeFilter fecha() {
        if (fecha == null) {
            fecha = new ZonedDateTimeFilter();
        }
        return fecha;
    }

    public void setFecha(ZonedDateTimeFilter fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FacturaCriteria that = (FacturaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombreUsuario, that.nombreUsuario) &&
            Objects.equals(nombrePlantilla, that.nombrePlantilla) &&
            Objects.equals(costo, that.costo) &&
            Objects.equals(fecha, that.fecha)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombreUsuario, nombrePlantilla, costo, fecha);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FacturaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombreUsuario != null ? "nombreUsuario=" + nombreUsuario + ", " : "") +
            (nombrePlantilla != null ? "nombrePlantilla=" + nombrePlantilla + ", " : "") +
            (costo != null ? "costo=" + costo + ", " : "") +
            (fecha != null ? "fecha=" + fecha + ", " : "") +
            "}";
    }
}

package org.datasurvey.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.datasurvey.domain.enumeration.EstadoPlantilla;
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
 * Criteria class for the {@link org.datasurvey.domain.Plantilla} entity. This class is used
 * in {@link org.datasurvey.web.rest.PlantillaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plantillas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlantillaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstadoPlantilla
     */
    public static class EstadoPlantillaFilter extends Filter<EstadoPlantilla> {

        public EstadoPlantillaFilter() {}

        public EstadoPlantillaFilter(EstadoPlantillaFilter filter) {
            super(filter);
        }

        @Override
        public EstadoPlantillaFilter copy() {
            return new EstadoPlantillaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private ZonedDateTimeFilter fechaCreacion;

    private ZonedDateTimeFilter fechaPublicacionTienda;

    private EstadoPlantillaFilter estado;

    private DoubleFilter precio;

    private LongFilter pPreguntaCerradaId;

    private LongFilter pPreguntaAbiertaId;

    private LongFilter categoriaId;

    private LongFilter usuarioExtraId;

    public PlantillaCriteria() {}

    public PlantillaCriteria(PlantillaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.fechaCreacion = other.fechaCreacion == null ? null : other.fechaCreacion.copy();
        this.fechaPublicacionTienda = other.fechaPublicacionTienda == null ? null : other.fechaPublicacionTienda.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.precio = other.precio == null ? null : other.precio.copy();
        this.pPreguntaCerradaId = other.pPreguntaCerradaId == null ? null : other.pPreguntaCerradaId.copy();
        this.pPreguntaAbiertaId = other.pPreguntaAbiertaId == null ? null : other.pPreguntaAbiertaId.copy();
        this.categoriaId = other.categoriaId == null ? null : other.categoriaId.copy();
        this.usuarioExtraId = other.usuarioExtraId == null ? null : other.usuarioExtraId.copy();
    }

    @Override
    public PlantillaCriteria copy() {
        return new PlantillaCriteria(this);
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

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            descripcion = new StringFilter();
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public ZonedDateTimeFilter getFechaCreacion() {
        return fechaCreacion;
    }

    public ZonedDateTimeFilter fechaCreacion() {
        if (fechaCreacion == null) {
            fechaCreacion = new ZonedDateTimeFilter();
        }
        return fechaCreacion;
    }

    public void setFechaCreacion(ZonedDateTimeFilter fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ZonedDateTimeFilter getFechaPublicacionTienda() {
        return fechaPublicacionTienda;
    }

    public ZonedDateTimeFilter fechaPublicacionTienda() {
        if (fechaPublicacionTienda == null) {
            fechaPublicacionTienda = new ZonedDateTimeFilter();
        }
        return fechaPublicacionTienda;
    }

    public void setFechaPublicacionTienda(ZonedDateTimeFilter fechaPublicacionTienda) {
        this.fechaPublicacionTienda = fechaPublicacionTienda;
    }

    public EstadoPlantillaFilter getEstado() {
        return estado;
    }

    public EstadoPlantillaFilter estado() {
        if (estado == null) {
            estado = new EstadoPlantillaFilter();
        }
        return estado;
    }

    public void setEstado(EstadoPlantillaFilter estado) {
        this.estado = estado;
    }

    public DoubleFilter getPrecio() {
        return precio;
    }

    public DoubleFilter precio() {
        if (precio == null) {
            precio = new DoubleFilter();
        }
        return precio;
    }

    public void setPrecio(DoubleFilter precio) {
        this.precio = precio;
    }

    public LongFilter getPPreguntaCerradaId() {
        return pPreguntaCerradaId;
    }

    public LongFilter pPreguntaCerradaId() {
        if (pPreguntaCerradaId == null) {
            pPreguntaCerradaId = new LongFilter();
        }
        return pPreguntaCerradaId;
    }

    public void setPPreguntaCerradaId(LongFilter pPreguntaCerradaId) {
        this.pPreguntaCerradaId = pPreguntaCerradaId;
    }

    public LongFilter getPPreguntaAbiertaId() {
        return pPreguntaAbiertaId;
    }

    public LongFilter pPreguntaAbiertaId() {
        if (pPreguntaAbiertaId == null) {
            pPreguntaAbiertaId = new LongFilter();
        }
        return pPreguntaAbiertaId;
    }

    public void setPPreguntaAbiertaId(LongFilter pPreguntaAbiertaId) {
        this.pPreguntaAbiertaId = pPreguntaAbiertaId;
    }

    public LongFilter getCategoriaId() {
        return categoriaId;
    }

    public LongFilter categoriaId() {
        if (categoriaId == null) {
            categoriaId = new LongFilter();
        }
        return categoriaId;
    }

    public void setCategoriaId(LongFilter categoriaId) {
        this.categoriaId = categoriaId;
    }

    public LongFilter getUsuarioExtraId() {
        return usuarioExtraId;
    }

    public LongFilter usuarioExtraId() {
        if (usuarioExtraId == null) {
            usuarioExtraId = new LongFilter();
        }
        return usuarioExtraId;
    }

    public void setUsuarioExtraId(LongFilter usuarioExtraId) {
        this.usuarioExtraId = usuarioExtraId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlantillaCriteria that = (PlantillaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(fechaCreacion, that.fechaCreacion) &&
            Objects.equals(fechaPublicacionTienda, that.fechaPublicacionTienda) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(precio, that.precio) &&
            Objects.equals(pPreguntaCerradaId, that.pPreguntaCerradaId) &&
            Objects.equals(pPreguntaAbiertaId, that.pPreguntaAbiertaId) &&
            Objects.equals(categoriaId, that.categoriaId) &&
            Objects.equals(usuarioExtraId, that.usuarioExtraId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nombre,
            descripcion,
            fechaCreacion,
            fechaPublicacionTienda,
            estado,
            precio,
            pPreguntaCerradaId,
            pPreguntaAbiertaId,
            categoriaId,
            usuarioExtraId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlantillaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (fechaCreacion != null ? "fechaCreacion=" + fechaCreacion + ", " : "") +
            (fechaPublicacionTienda != null ? "fechaPublicacionTienda=" + fechaPublicacionTienda + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (precio != null ? "precio=" + precio + ", " : "") +
            (pPreguntaCerradaId != null ? "pPreguntaCerradaId=" + pPreguntaCerradaId + ", " : "") +
            (pPreguntaAbiertaId != null ? "pPreguntaAbiertaId=" + pPreguntaAbiertaId + ", " : "") +
            (categoriaId != null ? "categoriaId=" + categoriaId + ", " : "") +
            (usuarioExtraId != null ? "usuarioExtraId=" + usuarioExtraId + ", " : "") +
            "}";
    }
}

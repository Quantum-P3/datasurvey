package org.datasurvey.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.datasurvey.domain.enumeration.AccesoEncuesta;
import org.datasurvey.domain.enumeration.EstadoEncuesta;
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
 * Criteria class for the {@link org.datasurvey.domain.Encuesta} entity. This class is used
 * in {@link org.datasurvey.web.rest.EncuestaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /encuestas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EncuestaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering AccesoEncuesta
     */
    public static class AccesoEncuestaFilter extends Filter<AccesoEncuesta> {

        public AccesoEncuestaFilter() {}

        public AccesoEncuestaFilter(AccesoEncuestaFilter filter) {
            super(filter);
        }

        @Override
        public AccesoEncuestaFilter copy() {
            return new AccesoEncuestaFilter(this);
        }
    }

    /**
     * Class for filtering EstadoEncuesta
     */
    public static class EstadoEncuestaFilter extends Filter<EstadoEncuesta> {

        public EstadoEncuestaFilter() {}

        public EstadoEncuestaFilter(EstadoEncuestaFilter filter) {
            super(filter);
        }

        @Override
        public EstadoEncuestaFilter copy() {
            return new EstadoEncuestaFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter descripcion;

    private ZonedDateTimeFilter fechaCreacion;

    private ZonedDateTimeFilter fechaPublicacion;

    private ZonedDateTimeFilter fechaFinalizar;

    private ZonedDateTimeFilter fechaFinalizada;

    private DoubleFilter calificacion;

    private AccesoEncuestaFilter acceso;

    private StringFilter contrasenna;

    private EstadoEncuestaFilter estado;

    private LongFilter usuarioEncuestaId;

    private LongFilter ePreguntaAbiertaId;

    private LongFilter ePreguntaCerradaId;

    private LongFilter categoriaId;

    private LongFilter usuarioExtraId;

    public EncuestaCriteria() {}

    public EncuestaCriteria(EncuestaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.descripcion = other.descripcion == null ? null : other.descripcion.copy();
        this.fechaCreacion = other.fechaCreacion == null ? null : other.fechaCreacion.copy();
        this.fechaPublicacion = other.fechaPublicacion == null ? null : other.fechaPublicacion.copy();
        this.fechaFinalizar = other.fechaFinalizar == null ? null : other.fechaFinalizar.copy();
        this.fechaFinalizada = other.fechaFinalizada == null ? null : other.fechaFinalizada.copy();
        this.calificacion = other.calificacion == null ? null : other.calificacion.copy();
        this.acceso = other.acceso == null ? null : other.acceso.copy();
        this.contrasenna = other.contrasenna == null ? null : other.contrasenna.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.usuarioEncuestaId = other.usuarioEncuestaId == null ? null : other.usuarioEncuestaId.copy();
        this.ePreguntaAbiertaId = other.ePreguntaAbiertaId == null ? null : other.ePreguntaAbiertaId.copy();
        this.ePreguntaCerradaId = other.ePreguntaCerradaId == null ? null : other.ePreguntaCerradaId.copy();
        this.categoriaId = other.categoriaId == null ? null : other.categoriaId.copy();
        this.usuarioExtraId = other.usuarioExtraId == null ? null : other.usuarioExtraId.copy();
    }

    @Override
    public EncuestaCriteria copy() {
        return new EncuestaCriteria(this);
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

    public ZonedDateTimeFilter getFechaPublicacion() {
        return fechaPublicacion;
    }

    public ZonedDateTimeFilter fechaPublicacion() {
        if (fechaPublicacion == null) {
            fechaPublicacion = new ZonedDateTimeFilter();
        }
        return fechaPublicacion;
    }

    public void setFechaPublicacion(ZonedDateTimeFilter fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public ZonedDateTimeFilter getFechaFinalizar() {
        return fechaFinalizar;
    }

    public ZonedDateTimeFilter fechaFinalizar() {
        if (fechaFinalizar == null) {
            fechaFinalizar = new ZonedDateTimeFilter();
        }
        return fechaFinalizar;
    }

    public void setFechaFinalizar(ZonedDateTimeFilter fechaFinalizar) {
        this.fechaFinalizar = fechaFinalizar;
    }

    public ZonedDateTimeFilter getFechaFinalizada() {
        return fechaFinalizada;
    }

    public ZonedDateTimeFilter fechaFinalizada() {
        if (fechaFinalizada == null) {
            fechaFinalizada = new ZonedDateTimeFilter();
        }
        return fechaFinalizada;
    }

    public void setFechaFinalizada(ZonedDateTimeFilter fechaFinalizada) {
        this.fechaFinalizada = fechaFinalizada;
    }

    public DoubleFilter getCalificacion() {
        return calificacion;
    }

    public DoubleFilter calificacion() {
        if (calificacion == null) {
            calificacion = new DoubleFilter();
        }
        return calificacion;
    }

    public void setCalificacion(DoubleFilter calificacion) {
        this.calificacion = calificacion;
    }

    public AccesoEncuestaFilter getAcceso() {
        return acceso;
    }

    public AccesoEncuestaFilter acceso() {
        if (acceso == null) {
            acceso = new AccesoEncuestaFilter();
        }
        return acceso;
    }

    public void setAcceso(AccesoEncuestaFilter acceso) {
        this.acceso = acceso;
    }

    public StringFilter getContrasenna() {
        return contrasenna;
    }

    public StringFilter contrasenna() {
        if (contrasenna == null) {
            contrasenna = new StringFilter();
        }
        return contrasenna;
    }

    public void setContrasenna(StringFilter contrasenna) {
        this.contrasenna = contrasenna;
    }

    public EstadoEncuestaFilter getEstado() {
        return estado;
    }

    public EstadoEncuestaFilter estado() {
        if (estado == null) {
            estado = new EstadoEncuestaFilter();
        }
        return estado;
    }

    public void setEstado(EstadoEncuestaFilter estado) {
        this.estado = estado;
    }

    public LongFilter getUsuarioEncuestaId() {
        return usuarioEncuestaId;
    }

    public LongFilter usuarioEncuestaId() {
        if (usuarioEncuestaId == null) {
            usuarioEncuestaId = new LongFilter();
        }
        return usuarioEncuestaId;
    }

    public void setUsuarioEncuestaId(LongFilter usuarioEncuestaId) {
        this.usuarioEncuestaId = usuarioEncuestaId;
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
        final EncuestaCriteria that = (EncuestaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(fechaCreacion, that.fechaCreacion) &&
            Objects.equals(fechaPublicacion, that.fechaPublicacion) &&
            Objects.equals(fechaFinalizar, that.fechaFinalizar) &&
            Objects.equals(fechaFinalizada, that.fechaFinalizada) &&
            Objects.equals(calificacion, that.calificacion) &&
            Objects.equals(acceso, that.acceso) &&
            Objects.equals(contrasenna, that.contrasenna) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(usuarioEncuestaId, that.usuarioEncuestaId) &&
            Objects.equals(ePreguntaAbiertaId, that.ePreguntaAbiertaId) &&
            Objects.equals(ePreguntaCerradaId, that.ePreguntaCerradaId) &&
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
            fechaPublicacion,
            fechaFinalizar,
            fechaFinalizada,
            calificacion,
            acceso,
            contrasenna,
            estado,
            usuarioEncuestaId,
            ePreguntaAbiertaId,
            ePreguntaCerradaId,
            categoriaId,
            usuarioExtraId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EncuestaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (descripcion != null ? "descripcion=" + descripcion + ", " : "") +
            (fechaCreacion != null ? "fechaCreacion=" + fechaCreacion + ", " : "") +
            (fechaPublicacion != null ? "fechaPublicacion=" + fechaPublicacion + ", " : "") +
            (fechaFinalizar != null ? "fechaFinalizar=" + fechaFinalizar + ", " : "") +
            (fechaFinalizada != null ? "fechaFinalizada=" + fechaFinalizada + ", " : "") +
            (calificacion != null ? "calificacion=" + calificacion + ", " : "") +
            (acceso != null ? "acceso=" + acceso + ", " : "") +
            (contrasenna != null ? "contrasenna=" + contrasenna + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (usuarioEncuestaId != null ? "usuarioEncuestaId=" + usuarioEncuestaId + ", " : "") +
            (ePreguntaAbiertaId != null ? "ePreguntaAbiertaId=" + ePreguntaAbiertaId + ", " : "") +
            (ePreguntaCerradaId != null ? "ePreguntaCerradaId=" + ePreguntaCerradaId + ", " : "") +
            (categoriaId != null ? "categoriaId=" + categoriaId + ", " : "") +
            (usuarioExtraId != null ? "usuarioExtraId=" + usuarioExtraId + ", " : "") +
            "}";
    }
}

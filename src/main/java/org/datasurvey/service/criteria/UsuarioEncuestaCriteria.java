package org.datasurvey.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.datasurvey.domain.enumeration.EstadoColaborador;
import org.datasurvey.domain.enumeration.RolColaborador;
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
 * Criteria class for the {@link org.datasurvey.domain.UsuarioEncuesta} entity. This class is used
 * in {@link org.datasurvey.web.rest.UsuarioEncuestaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuario-encuestas?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UsuarioEncuestaCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RolColaborador
     */
    public static class RolColaboradorFilter extends Filter<RolColaborador> {

        public RolColaboradorFilter() {}

        public RolColaboradorFilter(RolColaboradorFilter filter) {
            super(filter);
        }

        @Override
        public RolColaboradorFilter copy() {
            return new RolColaboradorFilter(this);
        }
    }

    /**
     * Class for filtering EstadoColaborador
     */
    public static class EstadoColaboradorFilter extends Filter<EstadoColaborador> {

        public EstadoColaboradorFilter() {}

        public EstadoColaboradorFilter(EstadoColaboradorFilter filter) {
            super(filter);
        }

        @Override
        public EstadoColaboradorFilter copy() {
            return new EstadoColaboradorFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private RolColaboradorFilter rol;

    private EstadoColaboradorFilter estado;

    private ZonedDateTimeFilter fechaAgregado;

    private LongFilter usuarioExtraId;

    private LongFilter encuestaId;

    public UsuarioEncuestaCriteria() {}

    public UsuarioEncuestaCriteria(UsuarioEncuestaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.rol = other.rol == null ? null : other.rol.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.fechaAgregado = other.fechaAgregado == null ? null : other.fechaAgregado.copy();
        this.usuarioExtraId = other.usuarioExtraId == null ? null : other.usuarioExtraId.copy();
        this.encuestaId = other.encuestaId == null ? null : other.encuestaId.copy();
    }

    @Override
    public UsuarioEncuestaCriteria copy() {
        return new UsuarioEncuestaCriteria(this);
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

    public RolColaboradorFilter getRol() {
        return rol;
    }

    public RolColaboradorFilter rol() {
        if (rol == null) {
            rol = new RolColaboradorFilter();
        }
        return rol;
    }

    public void setRol(RolColaboradorFilter rol) {
        this.rol = rol;
    }

    public EstadoColaboradorFilter getEstado() {
        return estado;
    }

    public EstadoColaboradorFilter estado() {
        if (estado == null) {
            estado = new EstadoColaboradorFilter();
        }
        return estado;
    }

    public void setEstado(EstadoColaboradorFilter estado) {
        this.estado = estado;
    }

    public ZonedDateTimeFilter getFechaAgregado() {
        return fechaAgregado;
    }

    public ZonedDateTimeFilter fechaAgregado() {
        if (fechaAgregado == null) {
            fechaAgregado = new ZonedDateTimeFilter();
        }
        return fechaAgregado;
    }

    public void setFechaAgregado(ZonedDateTimeFilter fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
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
        final UsuarioEncuestaCriteria that = (UsuarioEncuestaCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(rol, that.rol) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(fechaAgregado, that.fechaAgregado) &&
            Objects.equals(usuarioExtraId, that.usuarioExtraId) &&
            Objects.equals(encuestaId, that.encuestaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rol, estado, fechaAgregado, usuarioExtraId, encuestaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioEncuestaCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (rol != null ? "rol=" + rol + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (fechaAgregado != null ? "fechaAgregado=" + fechaAgregado + ", " : "") +
            (usuarioExtraId != null ? "usuarioExtraId=" + usuarioExtraId + ", " : "") +
            (encuestaId != null ? "encuestaId=" + encuestaId + ", " : "") +
            "}";
    }
}

package org.datasurvey.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.datasurvey.domain.enumeration.EstadoUsuario;
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
 * Criteria class for the {@link org.datasurvey.domain.UsuarioExtra} entity. This class is used
 * in {@link org.datasurvey.web.rest.UsuarioExtraResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /usuario-extras?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class UsuarioExtraCriteria implements Serializable, Criteria {

    /**
     * Class for filtering EstadoUsuario
     */
    public static class EstadoUsuarioFilter extends Filter<EstadoUsuario> {

        public EstadoUsuarioFilter() {}

        public EstadoUsuarioFilter(EstadoUsuarioFilter filter) {
            super(filter);
        }

        @Override
        public EstadoUsuarioFilter copy() {
            return new EstadoUsuarioFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nombre;

    private StringFilter iconoPerfil;

    private ZonedDateTimeFilter fechaNacimiento;

    private EstadoUsuarioFilter estado;

    private LongFilter userId;

    private LongFilter encuestaId;

    private LongFilter usuarioEncuestaId;

    private LongFilter plantillaId;

    public UsuarioExtraCriteria() {}

    public UsuarioExtraCriteria(UsuarioExtraCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nombre = other.nombre == null ? null : other.nombre.copy();
        this.iconoPerfil = other.iconoPerfil == null ? null : other.iconoPerfil.copy();
        this.fechaNacimiento = other.fechaNacimiento == null ? null : other.fechaNacimiento.copy();
        this.estado = other.estado == null ? null : other.estado.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.encuestaId = other.encuestaId == null ? null : other.encuestaId.copy();
        this.usuarioEncuestaId = other.usuarioEncuestaId == null ? null : other.usuarioEncuestaId.copy();
        this.plantillaId = other.plantillaId == null ? null : other.plantillaId.copy();
    }

    @Override
    public UsuarioExtraCriteria copy() {
        return new UsuarioExtraCriteria(this);
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

    public StringFilter getIconoPerfil() {
        return iconoPerfil;
    }

    public StringFilter iconoPerfil() {
        if (iconoPerfil == null) {
            iconoPerfil = new StringFilter();
        }
        return iconoPerfil;
    }

    public void setIconoPerfil(StringFilter iconoPerfil) {
        this.iconoPerfil = iconoPerfil;
    }

    public ZonedDateTimeFilter getFechaNacimiento() {
        return fechaNacimiento;
    }

    public ZonedDateTimeFilter fechaNacimiento() {
        if (fechaNacimiento == null) {
            fechaNacimiento = new ZonedDateTimeFilter();
        }
        return fechaNacimiento;
    }

    public void setFechaNacimiento(ZonedDateTimeFilter fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public EstadoUsuarioFilter getEstado() {
        return estado;
    }

    public EstadoUsuarioFilter estado() {
        if (estado == null) {
            estado = new EstadoUsuarioFilter();
        }
        return estado;
    }

    public void setEstado(EstadoUsuarioFilter estado) {
        this.estado = estado;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
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
        final UsuarioExtraCriteria that = (UsuarioExtraCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(iconoPerfil, that.iconoPerfil) &&
            Objects.equals(fechaNacimiento, that.fechaNacimiento) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(encuestaId, that.encuestaId) &&
            Objects.equals(usuarioEncuestaId, that.usuarioEncuestaId) &&
            Objects.equals(plantillaId, that.plantillaId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, iconoPerfil, fechaNacimiento, estado, userId, encuestaId, usuarioEncuestaId, plantillaId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioExtraCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nombre != null ? "nombre=" + nombre + ", " : "") +
            (iconoPerfil != null ? "iconoPerfil=" + iconoPerfil + ", " : "") +
            (fechaNacimiento != null ? "fechaNacimiento=" + fechaNacimiento + ", " : "") +
            (estado != null ? "estado=" + estado + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (encuestaId != null ? "encuestaId=" + encuestaId + ", " : "") +
            (usuarioEncuestaId != null ? "usuarioEncuestaId=" + usuarioEncuestaId + ", " : "") +
            (plantillaId != null ? "plantillaId=" + plantillaId + ", " : "") +
            "}";
    }
}

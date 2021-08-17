package org.datasurvey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.datasurvey.domain.enumeration.EstadoColaborador;
import org.datasurvey.domain.enumeration.RolColaborador;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UsuarioEncuesta.
 */
@Entity
@Table(name = "usuario_encuesta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UsuarioEncuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private RolColaborador rol;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoColaborador estado;

    @NotNull
    @Column(name = "fecha_agregado", nullable = false)
    private ZonedDateTime fechaAgregado;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "encuestas", "usuarioEncuestas", "plantillas" }, allowSetters = true)
    private UsuarioExtra usuarioExtra;

    @ManyToOne
    @JsonIgnoreProperties(value = { "usuarioEncuestas", "usuarioExtra" }, allowSetters = true)
    private Encuesta encuesta;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioEncuesta id(Long id) {
        this.id = id;
        return this;
    }

    public RolColaborador getRol() {
        return this.rol;
    }

    public UsuarioEncuesta rol(RolColaborador rol) {
        this.rol = rol;
        return this;
    }

    public void setRol(RolColaborador rol) {
        this.rol = rol;
    }

    public EstadoColaborador getEstado() {
        return this.estado;
    }

    public UsuarioEncuesta estado(EstadoColaborador estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoColaborador estado) {
        this.estado = estado;
    }

    public ZonedDateTime getFechaAgregado() {
        return this.fechaAgregado;
    }

    public UsuarioEncuesta fechaAgregado(ZonedDateTime fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
        return this;
    }

    public void setFechaAgregado(ZonedDateTime fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    public UsuarioExtra getUsuarioExtra() {
        return this.usuarioExtra;
    }

    public UsuarioEncuesta usuarioExtra(UsuarioExtra usuarioExtra) {
        this.setUsuarioExtra(usuarioExtra);
        return this;
    }

    public void setUsuarioExtra(UsuarioExtra usuarioExtra) {
        this.usuarioExtra = usuarioExtra;
    }

    public Encuesta getEncuesta() {
        return this.encuesta;
    }

    public UsuarioEncuesta encuesta(Encuesta encuesta) {
        this.setEncuesta(encuesta);
        return this;
    }

    public void setEncuesta(Encuesta encuesta) {
        this.encuesta = encuesta;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioEncuesta)) {
            return false;
        }
        return id != null && id.equals(((UsuarioEncuesta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioEncuesta{" +
            "id=" + getId() +
            ", rol='" + getRol() + "'" +
            ", estado='" + getEstado() + "'" +
            ", fechaAgregado='" + getFechaAgregado() + "'" +
            "}";
    }
}

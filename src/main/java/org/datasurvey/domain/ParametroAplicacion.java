package org.datasurvey.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ParametroAplicacion.
 */
@Entity
@Table(name = "parametro_aplicacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ParametroAplicacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "max_dias_encuesta", nullable = false)
    private Integer maxDiasEncuesta;

    @NotNull
    @Column(name = "min_dias_encuesta", nullable = false)
    private Integer minDiasEncuesta;

    @NotNull
    @Column(name = "max_cantidad_preguntas", nullable = false)
    private Integer maxCantidadPreguntas;

    @NotNull
    @Column(name = "min_cantidad_preguntas", nullable = false)
    private Integer minCantidadPreguntas;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParametroAplicacion id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getMaxDiasEncuesta() {
        return this.maxDiasEncuesta;
    }

    public ParametroAplicacion maxDiasEncuesta(Integer maxDiasEncuesta) {
        this.maxDiasEncuesta = maxDiasEncuesta;
        return this;
    }

    public void setMaxDiasEncuesta(Integer maxDiasEncuesta) {
        this.maxDiasEncuesta = maxDiasEncuesta;
    }

    public Integer getMinDiasEncuesta() {
        return this.minDiasEncuesta;
    }

    public ParametroAplicacion minDiasEncuesta(Integer minDiasEncuesta) {
        this.minDiasEncuesta = minDiasEncuesta;
        return this;
    }

    public void setMinDiasEncuesta(Integer minDiasEncuesta) {
        this.minDiasEncuesta = minDiasEncuesta;
    }

    public Integer getMaxCantidadPreguntas() {
        return this.maxCantidadPreguntas;
    }

    public ParametroAplicacion maxCantidadPreguntas(Integer maxCantidadPreguntas) {
        this.maxCantidadPreguntas = maxCantidadPreguntas;
        return this;
    }

    public void setMaxCantidadPreguntas(Integer maxCantidadPreguntas) {
        this.maxCantidadPreguntas = maxCantidadPreguntas;
    }

    public Integer getMinCantidadPreguntas() {
        return this.minCantidadPreguntas;
    }

    public ParametroAplicacion minCantidadPreguntas(Integer minCantidadPreguntas) {
        this.minCantidadPreguntas = minCantidadPreguntas;
        return this;
    }

    public void setMinCantidadPreguntas(Integer minCantidadPreguntas) {
        this.minCantidadPreguntas = minCantidadPreguntas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParametroAplicacion)) {
            return false;
        }
        return id != null && id.equals(((ParametroAplicacion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParametroAplicacion{" +
            "id=" + getId() +
            ", maxDiasEncuesta=" + getMaxDiasEncuesta() +
            ", minDiasEncuesta=" + getMinDiasEncuesta() +
            ", maxCantidadPreguntas=" + getMaxCantidadPreguntas() +
            ", minCantidadPreguntas=" + getMinCantidadPreguntas() +
            "}";
    }
}

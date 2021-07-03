package org.datasurvey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PPreguntaAbierta.
 */
@Entity
@Table(name = "p_pregunta_abierta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PPreguntaAbierta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre", length = 500, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "opcional", nullable = false)
    private Boolean opcional;

    @NotNull
    @Column(name = "orden", nullable = false)
    private Integer orden;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pPreguntaCerradas", "pPreguntaAbiertas", "categoria", "usuarioExtras" }, allowSetters = true)
    private Plantilla plantilla;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PPreguntaAbierta id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public PPreguntaAbierta nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getOpcional() {
        return this.opcional;
    }

    public PPreguntaAbierta opcional(Boolean opcional) {
        this.opcional = opcional;
        return this;
    }

    public void setOpcional(Boolean opcional) {
        this.opcional = opcional;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public PPreguntaAbierta orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Plantilla getPlantilla() {
        return this.plantilla;
    }

    public PPreguntaAbierta plantilla(Plantilla plantilla) {
        this.setPlantilla(plantilla);
        return this;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PPreguntaAbierta)) {
            return false;
        }
        return id != null && id.equals(((PPreguntaAbierta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PPreguntaAbierta{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", opcional='" + getOpcional() + "'" +
            ", orden=" + getOrden() +
            "}";
    }
}

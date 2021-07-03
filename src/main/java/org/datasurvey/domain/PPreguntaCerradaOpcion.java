package org.datasurvey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PPreguntaCerradaOpcion.
 */
@Entity
@Table(name = "p_pregunta_cerrada_opcion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PPreguntaCerradaOpcion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre", length = 500, nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "orden", nullable = false)
    private Integer orden;

    @ManyToOne
    @JsonIgnoreProperties(value = { "pPreguntaCerradaOpcions", "plantilla" }, allowSetters = true)
    private PPreguntaCerrada pPreguntaCerrada;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PPreguntaCerradaOpcion id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public PPreguntaCerradaOpcion nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public PPreguntaCerradaOpcion orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public PPreguntaCerrada getPPreguntaCerrada() {
        return this.pPreguntaCerrada;
    }

    public PPreguntaCerradaOpcion pPreguntaCerrada(PPreguntaCerrada pPreguntaCerrada) {
        this.setPPreguntaCerrada(pPreguntaCerrada);
        return this;
    }

    public void setPPreguntaCerrada(PPreguntaCerrada pPreguntaCerrada) {
        this.pPreguntaCerrada = pPreguntaCerrada;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PPreguntaCerradaOpcion)) {
            return false;
        }
        return id != null && id.equals(((PPreguntaCerradaOpcion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PPreguntaCerradaOpcion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", orden=" + getOrden() +
            "}";
    }
}

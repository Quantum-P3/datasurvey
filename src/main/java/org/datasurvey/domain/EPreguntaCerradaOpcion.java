package org.datasurvey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EPreguntaCerradaOpcion.
 */
@Entity
@Table(name = "e_pregunta_cerrada_opcion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EPreguntaCerradaOpcion implements Serializable {

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

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ePreguntaCerradaOpcions", "encuesta" }, allowSetters = true)
    private EPreguntaCerrada ePreguntaCerrada;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EPreguntaCerradaOpcion id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public EPreguntaCerradaOpcion nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public EPreguntaCerradaOpcion orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public EPreguntaCerradaOpcion cantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public EPreguntaCerrada getEPreguntaCerrada() {
        return this.ePreguntaCerrada;
    }

    public EPreguntaCerradaOpcion ePreguntaCerrada(EPreguntaCerrada ePreguntaCerrada) {
        this.setEPreguntaCerrada(ePreguntaCerrada);
        return this;
    }

    public void setEPreguntaCerrada(EPreguntaCerrada ePreguntaCerrada) {
        this.ePreguntaCerrada = ePreguntaCerrada;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EPreguntaCerradaOpcion)) {
            return false;
        }
        return id != null && id.equals(((EPreguntaCerradaOpcion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EPreguntaCerradaOpcion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", orden=" + getOrden() +
            ", cantidad=" + getCantidad() +
            "}";
    }
}

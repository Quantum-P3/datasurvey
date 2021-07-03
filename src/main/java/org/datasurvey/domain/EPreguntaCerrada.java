package org.datasurvey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.datasurvey.domain.enumeration.PreguntaCerradaTipo;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EPreguntaCerrada.
 */
@Entity
@Table(name = "e_pregunta_cerrada")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EPreguntaCerrada implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 500)
    @Column(name = "nombre", length = 500, nullable = false)
    private String nombre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private PreguntaCerradaTipo tipo;

    @NotNull
    @Column(name = "opcional", nullable = false)
    private Boolean opcional;

    @NotNull
    @Column(name = "orden", nullable = false)
    private Integer orden;

    @OneToMany(mappedBy = "ePreguntaCerrada")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ePreguntaCerrada" }, allowSetters = true)
    private Set<EPreguntaCerradaOpcion> ePreguntaCerradaOpcions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "usuarioEncuestas", "ePreguntaAbiertas", "ePreguntaCerradas", "categoria", "usuarioExtra" },
        allowSetters = true
    )
    private Encuesta encuesta;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EPreguntaCerrada id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public EPreguntaCerrada nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public PreguntaCerradaTipo getTipo() {
        return this.tipo;
    }

    public EPreguntaCerrada tipo(PreguntaCerradaTipo tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(PreguntaCerradaTipo tipo) {
        this.tipo = tipo;
    }

    public Boolean getOpcional() {
        return this.opcional;
    }

    public EPreguntaCerrada opcional(Boolean opcional) {
        this.opcional = opcional;
        return this;
    }

    public void setOpcional(Boolean opcional) {
        this.opcional = opcional;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public EPreguntaCerrada orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Set<EPreguntaCerradaOpcion> getEPreguntaCerradaOpcions() {
        return this.ePreguntaCerradaOpcions;
    }

    public EPreguntaCerrada ePreguntaCerradaOpcions(Set<EPreguntaCerradaOpcion> ePreguntaCerradaOpcions) {
        this.setEPreguntaCerradaOpcions(ePreguntaCerradaOpcions);
        return this;
    }

    public EPreguntaCerrada addEPreguntaCerradaOpcion(EPreguntaCerradaOpcion ePreguntaCerradaOpcion) {
        this.ePreguntaCerradaOpcions.add(ePreguntaCerradaOpcion);
        ePreguntaCerradaOpcion.setEPreguntaCerrada(this);
        return this;
    }

    public EPreguntaCerrada removeEPreguntaCerradaOpcion(EPreguntaCerradaOpcion ePreguntaCerradaOpcion) {
        this.ePreguntaCerradaOpcions.remove(ePreguntaCerradaOpcion);
        ePreguntaCerradaOpcion.setEPreguntaCerrada(null);
        return this;
    }

    public void setEPreguntaCerradaOpcions(Set<EPreguntaCerradaOpcion> ePreguntaCerradaOpcions) {
        if (this.ePreguntaCerradaOpcions != null) {
            this.ePreguntaCerradaOpcions.forEach(i -> i.setEPreguntaCerrada(null));
        }
        if (ePreguntaCerradaOpcions != null) {
            ePreguntaCerradaOpcions.forEach(i -> i.setEPreguntaCerrada(this));
        }
        this.ePreguntaCerradaOpcions = ePreguntaCerradaOpcions;
    }

    public Encuesta getEncuesta() {
        return this.encuesta;
    }

    public EPreguntaCerrada encuesta(Encuesta encuesta) {
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
        if (!(o instanceof EPreguntaCerrada)) {
            return false;
        }
        return id != null && id.equals(((EPreguntaCerrada) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EPreguntaCerrada{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", opcional='" + getOpcional() + "'" +
            ", orden=" + getOrden() +
            "}";
    }
}

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
 * A PPreguntaCerrada.
 */
@Entity
@Table(name = "p_pregunta_cerrada")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PPreguntaCerrada implements Serializable {

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

    @OneToMany(mappedBy = "pPreguntaCerrada")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pPreguntaCerrada" }, allowSetters = true)
    private Set<PPreguntaCerradaOpcion> pPreguntaCerradaOpcions = new HashSet<>();

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

    public PPreguntaCerrada id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public PPreguntaCerrada nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public PreguntaCerradaTipo getTipo() {
        return this.tipo;
    }

    public PPreguntaCerrada tipo(PreguntaCerradaTipo tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(PreguntaCerradaTipo tipo) {
        this.tipo = tipo;
    }

    public Boolean getOpcional() {
        return this.opcional;
    }

    public PPreguntaCerrada opcional(Boolean opcional) {
        this.opcional = opcional;
        return this;
    }

    public void setOpcional(Boolean opcional) {
        this.opcional = opcional;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public PPreguntaCerrada orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Set<PPreguntaCerradaOpcion> getPPreguntaCerradaOpcions() {
        return this.pPreguntaCerradaOpcions;
    }

    public PPreguntaCerrada pPreguntaCerradaOpcions(Set<PPreguntaCerradaOpcion> pPreguntaCerradaOpcions) {
        this.setPPreguntaCerradaOpcions(pPreguntaCerradaOpcions);
        return this;
    }

    public PPreguntaCerrada addPPreguntaCerradaOpcion(PPreguntaCerradaOpcion pPreguntaCerradaOpcion) {
        this.pPreguntaCerradaOpcions.add(pPreguntaCerradaOpcion);
        pPreguntaCerradaOpcion.setPPreguntaCerrada(this);
        return this;
    }

    public PPreguntaCerrada removePPreguntaCerradaOpcion(PPreguntaCerradaOpcion pPreguntaCerradaOpcion) {
        this.pPreguntaCerradaOpcions.remove(pPreguntaCerradaOpcion);
        pPreguntaCerradaOpcion.setPPreguntaCerrada(null);
        return this;
    }

    public void setPPreguntaCerradaOpcions(Set<PPreguntaCerradaOpcion> pPreguntaCerradaOpcions) {
        if (this.pPreguntaCerradaOpcions != null) {
            this.pPreguntaCerradaOpcions.forEach(i -> i.setPPreguntaCerrada(null));
        }
        if (pPreguntaCerradaOpcions != null) {
            pPreguntaCerradaOpcions.forEach(i -> i.setPPreguntaCerrada(this));
        }
        this.pPreguntaCerradaOpcions = pPreguntaCerradaOpcions;
    }

    public Plantilla getPlantilla() {
        return this.plantilla;
    }

    public PPreguntaCerrada plantilla(Plantilla plantilla) {
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
        if (!(o instanceof PPreguntaCerrada)) {
            return false;
        }
        return id != null && id.equals(((PPreguntaCerrada) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PPreguntaCerrada{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", opcional='" + getOpcional() + "'" +
            ", orden=" + getOrden() +
            "}";
    }
}

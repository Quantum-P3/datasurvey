package org.datasurvey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EPreguntaAbierta.
 */
@Entity
@Table(name = "e_pregunta_abierta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EPreguntaAbierta implements Serializable {

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

    @OneToMany(mappedBy = "ePreguntaAbierta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ePreguntaAbierta" }, allowSetters = true)
    private Set<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestas = new HashSet<>();

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

    public EPreguntaAbierta id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public EPreguntaAbierta nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getOpcional() {
        return this.opcional;
    }

    public EPreguntaAbierta opcional(Boolean opcional) {
        this.opcional = opcional;
        return this;
    }

    public void setOpcional(Boolean opcional) {
        this.opcional = opcional;
    }

    public Integer getOrden() {
        return this.orden;
    }

    public EPreguntaAbierta orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Set<EPreguntaAbiertaRespuesta> getEPreguntaAbiertaRespuestas() {
        return this.ePreguntaAbiertaRespuestas;
    }

    public EPreguntaAbierta ePreguntaAbiertaRespuestas(Set<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestas) {
        this.setEPreguntaAbiertaRespuestas(ePreguntaAbiertaRespuestas);
        return this;
    }

    public EPreguntaAbierta addEPreguntaAbiertaRespuesta(EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta) {
        this.ePreguntaAbiertaRespuestas.add(ePreguntaAbiertaRespuesta);
        ePreguntaAbiertaRespuesta.setEPreguntaAbierta(this);
        return this;
    }

    public EPreguntaAbierta removeEPreguntaAbiertaRespuesta(EPreguntaAbiertaRespuesta ePreguntaAbiertaRespuesta) {
        this.ePreguntaAbiertaRespuestas.remove(ePreguntaAbiertaRespuesta);
        ePreguntaAbiertaRespuesta.setEPreguntaAbierta(null);
        return this;
    }

    public void setEPreguntaAbiertaRespuestas(Set<EPreguntaAbiertaRespuesta> ePreguntaAbiertaRespuestas) {
        if (this.ePreguntaAbiertaRespuestas != null) {
            this.ePreguntaAbiertaRespuestas.forEach(i -> i.setEPreguntaAbierta(null));
        }
        if (ePreguntaAbiertaRespuestas != null) {
            ePreguntaAbiertaRespuestas.forEach(i -> i.setEPreguntaAbierta(this));
        }
        this.ePreguntaAbiertaRespuestas = ePreguntaAbiertaRespuestas;
    }

    public Encuesta getEncuesta() {
        return this.encuesta;
    }

    public EPreguntaAbierta encuesta(Encuesta encuesta) {
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
        if (!(o instanceof EPreguntaAbierta)) {
            return false;
        }
        return id != null && id.equals(((EPreguntaAbierta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EPreguntaAbierta{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", opcional='" + getOpcional() + "'" +
            ", orden=" + getOrden() +
            "}";
    }
}

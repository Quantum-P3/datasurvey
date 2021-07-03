package org.datasurvey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.datasurvey.domain.enumeration.EstadoCategoria;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Categoria.
 */
@Entity
@Table(name = "categoria")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Categoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCategoria estado;

    @OneToMany(mappedBy = "categoria")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "usuarioEncuestas", "ePreguntaAbiertas", "ePreguntaCerradas", "categoria", "usuarioExtra" },
        allowSetters = true
    )
    private Set<Encuesta> encuestas = new HashSet<>();

    @OneToMany(mappedBy = "categoria")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pPreguntaCerradas", "pPreguntaAbiertas", "categoria", "usuarioExtras" }, allowSetters = true)
    private Set<Plantilla> plantillas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categoria id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Categoria nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public EstadoCategoria getEstado() {
        return this.estado;
    }

    public Categoria estado(EstadoCategoria estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoCategoria estado) {
        this.estado = estado;
    }

    public Set<Encuesta> getEncuestas() {
        return this.encuestas;
    }

    public Categoria encuestas(Set<Encuesta> encuestas) {
        this.setEncuestas(encuestas);
        return this;
    }

    public Categoria addEncuesta(Encuesta encuesta) {
        this.encuestas.add(encuesta);
        encuesta.setCategoria(this);
        return this;
    }

    public Categoria removeEncuesta(Encuesta encuesta) {
        this.encuestas.remove(encuesta);
        encuesta.setCategoria(null);
        return this;
    }

    public void setEncuestas(Set<Encuesta> encuestas) {
        if (this.encuestas != null) {
            this.encuestas.forEach(i -> i.setCategoria(null));
        }
        if (encuestas != null) {
            encuestas.forEach(i -> i.setCategoria(this));
        }
        this.encuestas = encuestas;
    }

    public Set<Plantilla> getPlantillas() {
        return this.plantillas;
    }

    public Categoria plantillas(Set<Plantilla> plantillas) {
        this.setPlantillas(plantillas);
        return this;
    }

    public Categoria addPlantilla(Plantilla plantilla) {
        this.plantillas.add(plantilla);
        plantilla.setCategoria(this);
        return this;
    }

    public Categoria removePlantilla(Plantilla plantilla) {
        this.plantillas.remove(plantilla);
        plantilla.setCategoria(null);
        return this;
    }

    public void setPlantillas(Set<Plantilla> plantillas) {
        if (this.plantillas != null) {
            this.plantillas.forEach(i -> i.setCategoria(null));
        }
        if (plantillas != null) {
            plantillas.forEach(i -> i.setCategoria(this));
        }
        this.plantillas = plantillas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Categoria)) {
            return false;
        }
        return id != null && id.equals(((Categoria) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Categoria{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}

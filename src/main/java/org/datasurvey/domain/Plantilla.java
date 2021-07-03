package org.datasurvey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.datasurvey.domain.enumeration.EstadoPlantilla;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Plantilla.
 */
@Entity
@Table(name = "plantilla")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Plantilla implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 1, max = 50)
    @Column(name = "nombre", length = 50)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    private ZonedDateTime fechaCreacion;

    @Column(name = "fecha_publicacion_tienda")
    private ZonedDateTime fechaPublicacionTienda;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPlantilla estado;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Double precio;

    @OneToMany(mappedBy = "plantilla")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pPreguntaCerradaOpcions", "plantilla" }, allowSetters = true)
    private Set<PPreguntaCerrada> pPreguntaCerradas = new HashSet<>();

    @OneToMany(mappedBy = "plantilla")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "plantilla" }, allowSetters = true)
    private Set<PPreguntaAbierta> pPreguntaAbiertas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "encuestas", "plantillas" }, allowSetters = true)
    private Categoria categoria;

    @ManyToMany(mappedBy = "plantillas")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "encuestas", "usuarioEncuestas", "plantillas" }, allowSetters = true)
    private Set<UsuarioExtra> usuarioExtras = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plantilla id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Plantilla nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Plantilla descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ZonedDateTime getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Plantilla fechaCreacion(ZonedDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public void setFechaCreacion(ZonedDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ZonedDateTime getFechaPublicacionTienda() {
        return this.fechaPublicacionTienda;
    }

    public Plantilla fechaPublicacionTienda(ZonedDateTime fechaPublicacionTienda) {
        this.fechaPublicacionTienda = fechaPublicacionTienda;
        return this;
    }

    public void setFechaPublicacionTienda(ZonedDateTime fechaPublicacionTienda) {
        this.fechaPublicacionTienda = fechaPublicacionTienda;
    }

    public EstadoPlantilla getEstado() {
        return this.estado;
    }

    public Plantilla estado(EstadoPlantilla estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoPlantilla estado) {
        this.estado = estado;
    }

    public Double getPrecio() {
        return this.precio;
    }

    public Plantilla precio(Double precio) {
        this.precio = precio;
        return this;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Set<PPreguntaCerrada> getPPreguntaCerradas() {
        return this.pPreguntaCerradas;
    }

    public Plantilla pPreguntaCerradas(Set<PPreguntaCerrada> pPreguntaCerradas) {
        this.setPPreguntaCerradas(pPreguntaCerradas);
        return this;
    }

    public Plantilla addPPreguntaCerrada(PPreguntaCerrada pPreguntaCerrada) {
        this.pPreguntaCerradas.add(pPreguntaCerrada);
        pPreguntaCerrada.setPlantilla(this);
        return this;
    }

    public Plantilla removePPreguntaCerrada(PPreguntaCerrada pPreguntaCerrada) {
        this.pPreguntaCerradas.remove(pPreguntaCerrada);
        pPreguntaCerrada.setPlantilla(null);
        return this;
    }

    public void setPPreguntaCerradas(Set<PPreguntaCerrada> pPreguntaCerradas) {
        if (this.pPreguntaCerradas != null) {
            this.pPreguntaCerradas.forEach(i -> i.setPlantilla(null));
        }
        if (pPreguntaCerradas != null) {
            pPreguntaCerradas.forEach(i -> i.setPlantilla(this));
        }
        this.pPreguntaCerradas = pPreguntaCerradas;
    }

    public Set<PPreguntaAbierta> getPPreguntaAbiertas() {
        return this.pPreguntaAbiertas;
    }

    public Plantilla pPreguntaAbiertas(Set<PPreguntaAbierta> pPreguntaAbiertas) {
        this.setPPreguntaAbiertas(pPreguntaAbiertas);
        return this;
    }

    public Plantilla addPPreguntaAbierta(PPreguntaAbierta pPreguntaAbierta) {
        this.pPreguntaAbiertas.add(pPreguntaAbierta);
        pPreguntaAbierta.setPlantilla(this);
        return this;
    }

    public Plantilla removePPreguntaAbierta(PPreguntaAbierta pPreguntaAbierta) {
        this.pPreguntaAbiertas.remove(pPreguntaAbierta);
        pPreguntaAbierta.setPlantilla(null);
        return this;
    }

    public void setPPreguntaAbiertas(Set<PPreguntaAbierta> pPreguntaAbiertas) {
        if (this.pPreguntaAbiertas != null) {
            this.pPreguntaAbiertas.forEach(i -> i.setPlantilla(null));
        }
        if (pPreguntaAbiertas != null) {
            pPreguntaAbiertas.forEach(i -> i.setPlantilla(this));
        }
        this.pPreguntaAbiertas = pPreguntaAbiertas;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public Plantilla categoria(Categoria categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Set<UsuarioExtra> getUsuarioExtras() {
        return this.usuarioExtras;
    }

    public Plantilla usuarioExtras(Set<UsuarioExtra> usuarioExtras) {
        this.setUsuarioExtras(usuarioExtras);
        return this;
    }

    public Plantilla addUsuarioExtra(UsuarioExtra usuarioExtra) {
        this.usuarioExtras.add(usuarioExtra);
        usuarioExtra.getPlantillas().add(this);
        return this;
    }

    public Plantilla removeUsuarioExtra(UsuarioExtra usuarioExtra) {
        this.usuarioExtras.remove(usuarioExtra);
        usuarioExtra.getPlantillas().remove(this);
        return this;
    }

    public void setUsuarioExtras(Set<UsuarioExtra> usuarioExtras) {
        if (this.usuarioExtras != null) {
            this.usuarioExtras.forEach(i -> i.removePlantilla(this));
        }
        if (usuarioExtras != null) {
            usuarioExtras.forEach(i -> i.addPlantilla(this));
        }
        this.usuarioExtras = usuarioExtras;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plantilla)) {
            return false;
        }
        return id != null && id.equals(((Plantilla) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plantilla{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fechaPublicacionTienda='" + getFechaPublicacionTienda() + "'" +
            ", estado='" + getEstado() + "'" +
            ", precio=" + getPrecio() +
            "}";
    }
}

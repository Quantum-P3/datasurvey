package org.datasurvey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.datasurvey.domain.enumeration.AccesoEncuesta;
import org.datasurvey.domain.enumeration.EstadoEncuesta;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Encuesta.
 */
@Entity
@Table(name = "encuesta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Encuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "fecha_creacion", nullable = false)
    private ZonedDateTime fechaCreacion;

    @Column(name = "fecha_publicacion")
    private ZonedDateTime fechaPublicacion;

    @Column(name = "fecha_finalizar")
    private ZonedDateTime fechaFinalizar;

    @Column(name = "fecha_finalizada")
    private ZonedDateTime fechaFinalizada;

    @NotNull
    @Column(name = "calificacion", nullable = false)
    private Double calificacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "acceso", nullable = false)
    private AccesoEncuesta acceso;

    @Column(name = "contrasenna")
    private String contrasenna;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoEncuesta estado;

    @OneToMany(mappedBy = "encuesta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "usuarioExtra", "encuesta" }, allowSetters = true)
    private Set<UsuarioEncuesta> usuarioEncuestas = new HashSet<>();

    @OneToMany(mappedBy = "encuesta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ePreguntaAbiertaRespuestas", "encuesta" }, allowSetters = true)
    private Set<EPreguntaAbierta> ePreguntaAbiertas = new HashSet<>();

    @OneToMany(mappedBy = "encuesta")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ePreguntaCerradaOpcions", "encuesta" }, allowSetters = true)
    private Set<EPreguntaCerrada> ePreguntaCerradas = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "encuestas", "plantillas" }, allowSetters = true)
    private Categoria categoria;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "encuestas", "usuarioEncuestas", "plantillas" }, allowSetters = true)
    private UsuarioExtra usuarioExtra;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Encuesta id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Encuesta nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public Encuesta descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ZonedDateTime getFechaCreacion() {
        return this.fechaCreacion;
    }

    public Encuesta fechaCreacion(ZonedDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public void setFechaCreacion(ZonedDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public ZonedDateTime getFechaPublicacion() {
        return this.fechaPublicacion;
    }

    public Encuesta fechaPublicacion(ZonedDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
        return this;
    }

    public void setFechaPublicacion(ZonedDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public ZonedDateTime getFechaFinalizar() {
        return this.fechaFinalizar;
    }

    public Encuesta fechaFinalizar(ZonedDateTime fechaFinalizar) {
        this.fechaFinalizar = fechaFinalizar;
        return this;
    }

    public void setFechaFinalizar(ZonedDateTime fechaFinalizar) {
        this.fechaFinalizar = fechaFinalizar;
    }

    public ZonedDateTime getFechaFinalizada() {
        return this.fechaFinalizada;
    }

    public Encuesta fechaFinalizada(ZonedDateTime fechaFinalizada) {
        this.fechaFinalizada = fechaFinalizada;
        return this;
    }

    public void setFechaFinalizada(ZonedDateTime fechaFinalizada) {
        this.fechaFinalizada = fechaFinalizada;
    }

    public Double getCalificacion() {
        return this.calificacion;
    }

    public Encuesta calificacion(Double calificacion) {
        this.calificacion = calificacion;
        return this;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public AccesoEncuesta getAcceso() {
        return this.acceso;
    }

    public Encuesta acceso(AccesoEncuesta acceso) {
        this.acceso = acceso;
        return this;
    }

    public void setAcceso(AccesoEncuesta acceso) {
        this.acceso = acceso;
    }

    public String getContrasenna() {
        return this.contrasenna;
    }

    public Encuesta contrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
        return this;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public EstadoEncuesta getEstado() {
        return this.estado;
    }

    public Encuesta estado(EstadoEncuesta estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoEncuesta estado) {
        this.estado = estado;
    }

    public Set<UsuarioEncuesta> getUsuarioEncuestas() {
        return this.usuarioEncuestas;
    }

    public Encuesta usuarioEncuestas(Set<UsuarioEncuesta> usuarioEncuestas) {
        this.setUsuarioEncuestas(usuarioEncuestas);
        return this;
    }

    public Encuesta addUsuarioEncuesta(UsuarioEncuesta usuarioEncuesta) {
        this.usuarioEncuestas.add(usuarioEncuesta);
        usuarioEncuesta.setEncuesta(this);
        return this;
    }

    public Encuesta removeUsuarioEncuesta(UsuarioEncuesta usuarioEncuesta) {
        this.usuarioEncuestas.remove(usuarioEncuesta);
        usuarioEncuesta.setEncuesta(null);
        return this;
    }

    public void setUsuarioEncuestas(Set<UsuarioEncuesta> usuarioEncuestas) {
        if (this.usuarioEncuestas != null) {
            this.usuarioEncuestas.forEach(i -> i.setEncuesta(null));
        }
        if (usuarioEncuestas != null) {
            usuarioEncuestas.forEach(i -> i.setEncuesta(this));
        }
        this.usuarioEncuestas = usuarioEncuestas;
    }

    public Set<EPreguntaAbierta> getEPreguntaAbiertas() {
        return this.ePreguntaAbiertas;
    }

    public Encuesta ePreguntaAbiertas(Set<EPreguntaAbierta> ePreguntaAbiertas) {
        this.setEPreguntaAbiertas(ePreguntaAbiertas);
        return this;
    }

    public Encuesta addEPreguntaAbierta(EPreguntaAbierta ePreguntaAbierta) {
        this.ePreguntaAbiertas.add(ePreguntaAbierta);
        ePreguntaAbierta.setEncuesta(this);
        return this;
    }

    public Encuesta removeEPreguntaAbierta(EPreguntaAbierta ePreguntaAbierta) {
        this.ePreguntaAbiertas.remove(ePreguntaAbierta);
        ePreguntaAbierta.setEncuesta(null);
        return this;
    }

    public void setEPreguntaAbiertas(Set<EPreguntaAbierta> ePreguntaAbiertas) {
        if (this.ePreguntaAbiertas != null) {
            this.ePreguntaAbiertas.forEach(i -> i.setEncuesta(null));
        }
        if (ePreguntaAbiertas != null) {
            ePreguntaAbiertas.forEach(i -> i.setEncuesta(this));
        }
        this.ePreguntaAbiertas = ePreguntaAbiertas;
    }

    public Set<EPreguntaCerrada> getEPreguntaCerradas() {
        return this.ePreguntaCerradas;
    }

    public Encuesta ePreguntaCerradas(Set<EPreguntaCerrada> ePreguntaCerradas) {
        this.setEPreguntaCerradas(ePreguntaCerradas);
        return this;
    }

    public Encuesta addEPreguntaCerrada(EPreguntaCerrada ePreguntaCerrada) {
        this.ePreguntaCerradas.add(ePreguntaCerrada);
        ePreguntaCerrada.setEncuesta(this);
        return this;
    }

    public Encuesta removeEPreguntaCerrada(EPreguntaCerrada ePreguntaCerrada) {
        this.ePreguntaCerradas.remove(ePreguntaCerrada);
        ePreguntaCerrada.setEncuesta(null);
        return this;
    }

    public void setEPreguntaCerradas(Set<EPreguntaCerrada> ePreguntaCerradas) {
        if (this.ePreguntaCerradas != null) {
            this.ePreguntaCerradas.forEach(i -> i.setEncuesta(null));
        }
        if (ePreguntaCerradas != null) {
            ePreguntaCerradas.forEach(i -> i.setEncuesta(this));
        }
        this.ePreguntaCerradas = ePreguntaCerradas;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public Encuesta categoria(Categoria categoria) {
        this.setCategoria(categoria);
        return this;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public UsuarioExtra getUsuarioExtra() {
        return this.usuarioExtra;
    }

    public Encuesta usuarioExtra(UsuarioExtra usuarioExtra) {
        this.setUsuarioExtra(usuarioExtra);
        return this;
    }

    public void setUsuarioExtra(UsuarioExtra usuarioExtra) {
        this.usuarioExtra = usuarioExtra;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Encuesta)) {
            return false;
        }
        return id != null && id.equals(((Encuesta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Encuesta{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", fechaPublicacion='" + getFechaPublicacion() + "'" +
            ", fechaFinalizar='" + getFechaFinalizar() + "'" +
            ", fechaFinalizada='" + getFechaFinalizada() + "'" +
            ", calificacion=" + getCalificacion() +
            ", acceso='" + getAcceso() + "'" +
            ", contrasenna='" + getContrasenna() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}

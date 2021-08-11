package org.datasurvey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.datasurvey.domain.enumeration.EstadoUsuario;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UsuarioExtra.
 */
@Entity
@Table(name = "usuario_extra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UsuarioExtra implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "icono_perfil")
    private String iconoPerfil;

    @Column(name = "fecha_nacimiento")
    private ZonedDateTime fechaNacimiento;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoUsuario estado;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "usuarioExtra")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "usuarioEncuestas", "ePreguntaAbiertas", "ePreguntaCerradas", "categoria", "usuarioExtra" },
        allowSetters = true
    )
    private Set<Encuesta> encuestas = new HashSet<>();

    @OneToMany(mappedBy = "usuarioExtra")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "usuarioExtra", "encuesta" }, allowSetters = true)
    private Set<UsuarioEncuesta> usuarioEncuestas = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_usuario_extra__plantilla",
        joinColumns = @JoinColumn(name = "usuario_extra_id"),
        inverseJoinColumns = @JoinColumn(name = "plantilla_id")
    )
    @JsonIgnoreProperties(value = { "pPreguntaCerradas", "pPreguntaAbiertas", "usuarioExtras" }, allowSetters = true)
    private Set<Plantilla> plantillas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioExtra id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return this.nombre;
    }

    public UsuarioExtra nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIconoPerfil() {
        return this.iconoPerfil;
    }

    public UsuarioExtra iconoPerfil(String iconoPerfil) {
        this.iconoPerfil = iconoPerfil;
        return this;
    }

    public void setIconoPerfil(String iconoPerfil) {
        this.iconoPerfil = iconoPerfil;
    }

    public ZonedDateTime getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public UsuarioExtra fechaNacimiento(ZonedDateTime fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public void setFechaNacimiento(ZonedDateTime fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public EstadoUsuario getEstado() {
        return this.estado;
    }

    public UsuarioExtra estado(EstadoUsuario estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    public User getUser() {
        return this.user;
    }

    public UsuarioExtra user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Encuesta> getEncuestas() {
        return this.encuestas;
    }

    public UsuarioExtra encuestas(Set<Encuesta> encuestas) {
        this.setEncuestas(encuestas);
        return this;
    }

    public UsuarioExtra addEncuesta(Encuesta encuesta) {
        this.encuestas.add(encuesta);
        encuesta.setUsuarioExtra(this);
        return this;
    }

    public UsuarioExtra removeEncuesta(Encuesta encuesta) {
        this.encuestas.remove(encuesta);
        encuesta.setUsuarioExtra(null);
        return this;
    }

    public void setEncuestas(Set<Encuesta> encuestas) {
        if (this.encuestas != null) {
            this.encuestas.forEach(i -> i.setUsuarioExtra(null));
        }
        if (encuestas != null) {
            encuestas.forEach(i -> i.setUsuarioExtra(this));
        }
        this.encuestas = encuestas;
    }

    public Set<UsuarioEncuesta> getUsuarioEncuestas() {
        return this.usuarioEncuestas;
    }

    public UsuarioExtra usuarioEncuestas(Set<UsuarioEncuesta> usuarioEncuestas) {
        this.setUsuarioEncuestas(usuarioEncuestas);
        return this;
    }

    public UsuarioExtra addUsuarioEncuesta(UsuarioEncuesta usuarioEncuesta) {
        this.usuarioEncuestas.add(usuarioEncuesta);
        usuarioEncuesta.setUsuarioExtra(this);
        return this;
    }

    public UsuarioExtra removeUsuarioEncuesta(UsuarioEncuesta usuarioEncuesta) {
        this.usuarioEncuestas.remove(usuarioEncuesta);
        usuarioEncuesta.setUsuarioExtra(null);
        return this;
    }

    public void setUsuarioEncuestas(Set<UsuarioEncuesta> usuarioEncuestas) {
        if (this.usuarioEncuestas != null) {
            this.usuarioEncuestas.forEach(i -> i.setUsuarioExtra(null));
        }
        if (usuarioEncuestas != null) {
            usuarioEncuestas.forEach(i -> i.setUsuarioExtra(this));
        }
        this.usuarioEncuestas = usuarioEncuestas;
    }

    public Set<Plantilla> getPlantillas() {
        return this.plantillas;
    }

    public UsuarioExtra plantillas(Set<Plantilla> plantillas) {
        this.setPlantillas(plantillas);
        return this;
    }

    public UsuarioExtra addPlantilla(Plantilla plantilla) {
        this.plantillas.add(plantilla);
        plantilla.getUsuarioExtras().add(this);
        return this;
    }

    public UsuarioExtra removePlantilla(Plantilla plantilla) {
        this.plantillas.remove(plantilla);
        plantilla.getUsuarioExtras().remove(this);
        return this;
    }

    public void setPlantillas(Set<Plantilla> plantillas) {
        this.plantillas = plantillas;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UsuarioExtra)) {
            return false;
        }
        return id != null && id.equals(((UsuarioExtra) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UsuarioExtra{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", iconoPerfil='" + getIconoPerfil() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}

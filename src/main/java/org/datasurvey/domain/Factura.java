package org.datasurvey.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Factura.
 */
@Entity
@Table(name = "factura")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @NotNull
    @Column(name = "nombre_plantilla", nullable = false)
    private String nombrePlantilla;

    @NotNull
    @Column(name = "costo", nullable = false)
    private Double costo;

    @NotNull
    @Column(name = "fecha", nullable = false)
    private ZonedDateTime fecha;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Factura id(Long id) {
        this.id = id;
        return this;
    }

    public String getNombreUsuario() {
        return this.nombreUsuario;
    }

    public Factura nombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        return this;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombrePlantilla() {
        return this.nombrePlantilla;
    }

    public Factura nombrePlantilla(String nombrePlantilla) {
        this.nombrePlantilla = nombrePlantilla;
        return this;
    }

    public void setNombrePlantilla(String nombrePlantilla) {
        this.nombrePlantilla = nombrePlantilla;
    }

    public Double getCosto() {
        return this.costo;
    }

    public Factura costo(Double costo) {
        this.costo = costo;
        return this;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public ZonedDateTime getFecha() {
        return this.fecha;
    }

    public Factura fecha(ZonedDateTime fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(ZonedDateTime fecha) {
        this.fecha = fecha;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Factura)) {
            return false;
        }
        return id != null && id.equals(((Factura) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Factura{" +
            "id=" + getId() +
            ", nombreUsuario='" + getNombreUsuario() + "'" +
            ", nombrePlantilla='" + getNombrePlantilla() + "'" +
            ", costo=" + getCosto() +
            ", fecha='" + getFecha() + "'" +
            "}";
    }
}

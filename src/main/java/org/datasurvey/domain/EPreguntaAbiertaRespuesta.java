package org.datasurvey.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EPreguntaAbiertaRespuesta.
 */
@Entity
@Table(name = "e_pregunta_abierta_respuesta")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class EPreguntaAbiertaRespuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "respuesta", nullable = false)
    private String respuesta;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ePreguntaAbiertaRespuestas", "encuesta" }, allowSetters = true)
    private EPreguntaAbierta ePreguntaAbierta;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EPreguntaAbiertaRespuesta id(Long id) {
        this.id = id;
        return this;
    }

    public String getRespuesta() {
        return this.respuesta;
    }

    public EPreguntaAbiertaRespuesta respuesta(String respuesta) {
        this.respuesta = respuesta;
        return this;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public EPreguntaAbierta getEPreguntaAbierta() {
        return this.ePreguntaAbierta;
    }

    public EPreguntaAbiertaRespuesta ePreguntaAbierta(EPreguntaAbierta ePreguntaAbierta) {
        this.setEPreguntaAbierta(ePreguntaAbierta);
        return this;
    }

    public void setEPreguntaAbierta(EPreguntaAbierta ePreguntaAbierta) {
        this.ePreguntaAbierta = ePreguntaAbierta;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EPreguntaAbiertaRespuesta)) {
            return false;
        }
        return id != null && id.equals(((EPreguntaAbiertaRespuesta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EPreguntaAbiertaRespuesta{" +
            "id=" + getId() +
            ", respuesta='" + getRespuesta() + "'" +
            "}";
    }
}

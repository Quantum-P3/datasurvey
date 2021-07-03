package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.Encuesta;
import org.datasurvey.repository.EncuestaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Encuesta}.
 */
@Service
@Transactional
public class EncuestaService {

    private final Logger log = LoggerFactory.getLogger(EncuestaService.class);

    private final EncuestaRepository encuestaRepository;

    public EncuestaService(EncuestaRepository encuestaRepository) {
        this.encuestaRepository = encuestaRepository;
    }

    /**
     * Save a encuesta.
     *
     * @param encuesta the entity to save.
     * @return the persisted entity.
     */
    public Encuesta save(Encuesta encuesta) {
        log.debug("Request to save Encuesta : {}", encuesta);
        return encuestaRepository.save(encuesta);
    }

    /**
     * Partially update a encuesta.
     *
     * @param encuesta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Encuesta> partialUpdate(Encuesta encuesta) {
        log.debug("Request to partially update Encuesta : {}", encuesta);

        return encuestaRepository
            .findById(encuesta.getId())
            .map(
                existingEncuesta -> {
                    if (encuesta.getNombre() != null) {
                        existingEncuesta.setNombre(encuesta.getNombre());
                    }
                    if (encuesta.getDescripcion() != null) {
                        existingEncuesta.setDescripcion(encuesta.getDescripcion());
                    }
                    if (encuesta.getFechaCreacion() != null) {
                        existingEncuesta.setFechaCreacion(encuesta.getFechaCreacion());
                    }
                    if (encuesta.getFechaPublicacion() != null) {
                        existingEncuesta.setFechaPublicacion(encuesta.getFechaPublicacion());
                    }
                    if (encuesta.getFechaFinalizar() != null) {
                        existingEncuesta.setFechaFinalizar(encuesta.getFechaFinalizar());
                    }
                    if (encuesta.getFechaFinalizada() != null) {
                        existingEncuesta.setFechaFinalizada(encuesta.getFechaFinalizada());
                    }
                    if (encuesta.getCalificacion() != null) {
                        existingEncuesta.setCalificacion(encuesta.getCalificacion());
                    }
                    if (encuesta.getAcceso() != null) {
                        existingEncuesta.setAcceso(encuesta.getAcceso());
                    }
                    if (encuesta.getContrasenna() != null) {
                        existingEncuesta.setContrasenna(encuesta.getContrasenna());
                    }
                    if (encuesta.getEstado() != null) {
                        existingEncuesta.setEstado(encuesta.getEstado());
                    }

                    return existingEncuesta;
                }
            )
            .map(encuestaRepository::save);
    }

    /**
     * Get all the encuestas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Encuesta> findAll() {
        log.debug("Request to get all Encuestas");
        return encuestaRepository.findAll();
    }

    /**
     * Get one encuesta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Encuesta> findOne(Long id) {
        log.debug("Request to get Encuesta : {}", id);
        return encuestaRepository.findById(id);
    }

    /**
     * Delete the encuesta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Encuesta : {}", id);
        encuestaRepository.deleteById(id);
    }
}

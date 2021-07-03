package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.UsuarioEncuesta;
import org.datasurvey.repository.UsuarioEncuestaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UsuarioEncuesta}.
 */
@Service
@Transactional
public class UsuarioEncuestaService {

    private final Logger log = LoggerFactory.getLogger(UsuarioEncuestaService.class);

    private final UsuarioEncuestaRepository usuarioEncuestaRepository;

    public UsuarioEncuestaService(UsuarioEncuestaRepository usuarioEncuestaRepository) {
        this.usuarioEncuestaRepository = usuarioEncuestaRepository;
    }

    /**
     * Save a usuarioEncuesta.
     *
     * @param usuarioEncuesta the entity to save.
     * @return the persisted entity.
     */
    public UsuarioEncuesta save(UsuarioEncuesta usuarioEncuesta) {
        log.debug("Request to save UsuarioEncuesta : {}", usuarioEncuesta);
        return usuarioEncuestaRepository.save(usuarioEncuesta);
    }

    /**
     * Partially update a usuarioEncuesta.
     *
     * @param usuarioEncuesta the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UsuarioEncuesta> partialUpdate(UsuarioEncuesta usuarioEncuesta) {
        log.debug("Request to partially update UsuarioEncuesta : {}", usuarioEncuesta);

        return usuarioEncuestaRepository
            .findById(usuarioEncuesta.getId())
            .map(
                existingUsuarioEncuesta -> {
                    if (usuarioEncuesta.getRol() != null) {
                        existingUsuarioEncuesta.setRol(usuarioEncuesta.getRol());
                    }
                    if (usuarioEncuesta.getEstado() != null) {
                        existingUsuarioEncuesta.setEstado(usuarioEncuesta.getEstado());
                    }
                    if (usuarioEncuesta.getFechaAgregado() != null) {
                        existingUsuarioEncuesta.setFechaAgregado(usuarioEncuesta.getFechaAgregado());
                    }

                    return existingUsuarioEncuesta;
                }
            )
            .map(usuarioEncuestaRepository::save);
    }

    /**
     * Get all the usuarioEncuestas.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UsuarioEncuesta> findAll() {
        log.debug("Request to get all UsuarioEncuestas");
        return usuarioEncuestaRepository.findAll();
    }

    /**
     * Get one usuarioEncuesta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioEncuesta> findOne(Long id) {
        log.debug("Request to get UsuarioEncuesta : {}", id);
        return usuarioEncuestaRepository.findById(id);
    }

    /**
     * Delete the usuarioEncuesta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UsuarioEncuesta : {}", id);
        usuarioEncuestaRepository.deleteById(id);
    }
}

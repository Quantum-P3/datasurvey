package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.UsuarioExtra;
import org.datasurvey.repository.UsuarioExtraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UsuarioExtra}.
 */
@Service
@Transactional
public class UsuarioExtraService {

    private final Logger log = LoggerFactory.getLogger(UsuarioExtraService.class);

    private final UsuarioExtraRepository usuarioExtraRepository;

    public UsuarioExtraService(UsuarioExtraRepository usuarioExtraRepository) {
        this.usuarioExtraRepository = usuarioExtraRepository;
    }

    /**
     * Save a usuarioExtra.
     *
     * @param usuarioExtra the entity to save.
     * @return the persisted entity.
     */
    public UsuarioExtra save(UsuarioExtra usuarioExtra) {
        log.debug("Request to save UsuarioExtra : {}", usuarioExtra);
        return usuarioExtraRepository.save(usuarioExtra);
    }

    /**
     * Partially update a usuarioExtra.
     *
     * @param usuarioExtra the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UsuarioExtra> partialUpdate(UsuarioExtra usuarioExtra) {
        log.debug("Request to partially update UsuarioExtra : {}", usuarioExtra);

        return usuarioExtraRepository
            .findById(usuarioExtra.getId())
            .map(
                existingUsuarioExtra -> {
                    if (usuarioExtra.getNombre() != null) {
                        existingUsuarioExtra.setNombre(usuarioExtra.getNombre());
                    }
                    if (usuarioExtra.getIconoPerfil() != null) {
                        existingUsuarioExtra.setIconoPerfil(usuarioExtra.getIconoPerfil());
                    }
                    if (usuarioExtra.getFechaNacimiento() != null) {
                        existingUsuarioExtra.setFechaNacimiento(usuarioExtra.getFechaNacimiento());
                    }
                    if (usuarioExtra.getEstado() != null) {
                        existingUsuarioExtra.setEstado(usuarioExtra.getEstado());
                    }

                    return existingUsuarioExtra;
                }
            )
            .map(usuarioExtraRepository::save);
    }

    /**
     * Get all the usuarioExtras.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UsuarioExtra> findAll() {
        log.debug("Request to get all UsuarioExtras");
        return usuarioExtraRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the usuarioExtras with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UsuarioExtra> findAllWithEagerRelationships(Pageable pageable) {
        return usuarioExtraRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one usuarioExtra by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioExtra> findOne(Long id) {
        log.debug("Request to get UsuarioExtra : {}", id);
        return usuarioExtraRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the usuarioExtra by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UsuarioExtra : {}", id);
        usuarioExtraRepository.deleteById(id);
    }
}

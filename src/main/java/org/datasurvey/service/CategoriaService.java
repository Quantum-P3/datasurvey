package org.datasurvey.service;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.Categoria;
import org.datasurvey.repository.CategoriaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Categoria}.
 */
@Service
@Transactional
public class CategoriaService {

    private final Logger log = LoggerFactory.getLogger(CategoriaService.class);

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Save a categoria.
     *
     * @param categoria the entity to save.
     * @return the persisted entity.
     */
    public Categoria save(Categoria categoria) {
        log.debug("Request to save Categoria : {}", categoria);
        return categoriaRepository.save(categoria);
    }

    /**
     * Partially update a categoria.
     *
     * @param categoria the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Categoria> partialUpdate(Categoria categoria) {
        log.debug("Request to partially update Categoria : {}", categoria);

        return categoriaRepository
            .findById(categoria.getId())
            .map(
                existingCategoria -> {
                    if (categoria.getNombre() != null) {
                        existingCategoria.setNombre(categoria.getNombre());
                    }
                    if (categoria.getEstado() != null) {
                        existingCategoria.setEstado(categoria.getEstado());
                    }

                    return existingCategoria;
                }
            )
            .map(categoriaRepository::save);
    }

    /**
     * Get all the categorias.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Categoria> findAll() {
        log.debug("Request to get all Categorias");
        return categoriaRepository.findAll();
    }

    /**
     * Get one categoria by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Categoria> findOne(Long id) {
        log.debug("Request to get Categoria : {}", id);
        return categoriaRepository.findById(id);
    }

    /**
     * Delete the categoria by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Categoria : {}", id);
        categoriaRepository.deleteById(id);
    }
}

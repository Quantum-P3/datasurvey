package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.Plantilla;
import org.datasurvey.repository.PlantillaRepository;
import org.datasurvey.service.criteria.PlantillaCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Plantilla} entities in the database.
 * The main input is a {@link PlantillaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Plantilla} or a {@link Page} of {@link Plantilla} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlantillaQueryService extends QueryService<Plantilla> {

    private final Logger log = LoggerFactory.getLogger(PlantillaQueryService.class);

    private final PlantillaRepository plantillaRepository;

    public PlantillaQueryService(PlantillaRepository plantillaRepository) {
        this.plantillaRepository = plantillaRepository;
    }

    /**
     * Return a {@link List} of {@link Plantilla} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Plantilla> findByCriteria(PlantillaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Plantilla> specification = createSpecification(criteria);
        return plantillaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Plantilla} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Plantilla> findByCriteria(PlantillaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Plantilla> specification = createSpecification(criteria);
        return plantillaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlantillaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Plantilla> specification = createSpecification(criteria);
        return plantillaRepository.count(specification);
    }

    /**
     * Function to convert {@link PlantillaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Plantilla> createSpecification(PlantillaCriteria criteria) {
        Specification<Plantilla> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Plantilla_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Plantilla_.nombre));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Plantilla_.descripcion));
            }
            if (criteria.getFechaCreacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaCreacion(), Plantilla_.fechaCreacion));
            }
            if (criteria.getFechaPublicacionTienda() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getFechaPublicacionTienda(), Plantilla_.fechaPublicacionTienda));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Plantilla_.estado));
            }
            if (criteria.getPrecio() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrecio(), Plantilla_.precio));
            }
            if (criteria.getPPreguntaCerradaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPPreguntaCerradaId(),
                            root -> root.join(Plantilla_.pPreguntaCerradas, JoinType.LEFT).get(PPreguntaCerrada_.id)
                        )
                    );
            }
            if (criteria.getPPreguntaAbiertaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPPreguntaAbiertaId(),
                            root -> root.join(Plantilla_.pPreguntaAbiertas, JoinType.LEFT).get(PPreguntaAbierta_.id)
                        )
                    );
            }
            if (criteria.getCategoriaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaId(),
                            root -> root.join(Plantilla_.categoria, JoinType.LEFT).get(Categoria_.id)
                        )
                    );
            }
            if (criteria.getUsuarioExtraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioExtraId(),
                            root -> root.join(Plantilla_.usuarioExtras, JoinType.LEFT).get(UsuarioExtra_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

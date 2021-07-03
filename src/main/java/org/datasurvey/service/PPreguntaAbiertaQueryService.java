package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.PPreguntaAbierta;
import org.datasurvey.repository.PPreguntaAbiertaRepository;
import org.datasurvey.service.criteria.PPreguntaAbiertaCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PPreguntaAbierta} entities in the database.
 * The main input is a {@link PPreguntaAbiertaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PPreguntaAbierta} or a {@link Page} of {@link PPreguntaAbierta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PPreguntaAbiertaQueryService extends QueryService<PPreguntaAbierta> {

    private final Logger log = LoggerFactory.getLogger(PPreguntaAbiertaQueryService.class);

    private final PPreguntaAbiertaRepository pPreguntaAbiertaRepository;

    public PPreguntaAbiertaQueryService(PPreguntaAbiertaRepository pPreguntaAbiertaRepository) {
        this.pPreguntaAbiertaRepository = pPreguntaAbiertaRepository;
    }

    /**
     * Return a {@link List} of {@link PPreguntaAbierta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PPreguntaAbierta> findByCriteria(PPreguntaAbiertaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PPreguntaAbierta> specification = createSpecification(criteria);
        return pPreguntaAbiertaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PPreguntaAbierta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PPreguntaAbierta> findByCriteria(PPreguntaAbiertaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PPreguntaAbierta> specification = createSpecification(criteria);
        return pPreguntaAbiertaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PPreguntaAbiertaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PPreguntaAbierta> specification = createSpecification(criteria);
        return pPreguntaAbiertaRepository.count(specification);
    }

    /**
     * Function to convert {@link PPreguntaAbiertaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PPreguntaAbierta> createSpecification(PPreguntaAbiertaCriteria criteria) {
        Specification<PPreguntaAbierta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PPreguntaAbierta_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), PPreguntaAbierta_.nombre));
            }
            if (criteria.getOpcional() != null) {
                specification = specification.and(buildSpecification(criteria.getOpcional(), PPreguntaAbierta_.opcional));
            }
            if (criteria.getOrden() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrden(), PPreguntaAbierta_.orden));
            }
            if (criteria.getPlantillaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlantillaId(),
                            root -> root.join(PPreguntaAbierta_.plantilla, JoinType.LEFT).get(Plantilla_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.ParametroAplicacion;
import org.datasurvey.repository.ParametroAplicacionRepository;
import org.datasurvey.service.criteria.ParametroAplicacionCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ParametroAplicacion} entities in the database.
 * The main input is a {@link ParametroAplicacionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParametroAplicacion} or a {@link Page} of {@link ParametroAplicacion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParametroAplicacionQueryService extends QueryService<ParametroAplicacion> {

    private final Logger log = LoggerFactory.getLogger(ParametroAplicacionQueryService.class);

    private final ParametroAplicacionRepository parametroAplicacionRepository;

    public ParametroAplicacionQueryService(ParametroAplicacionRepository parametroAplicacionRepository) {
        this.parametroAplicacionRepository = parametroAplicacionRepository;
    }

    /**
     * Return a {@link List} of {@link ParametroAplicacion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParametroAplicacion> findByCriteria(ParametroAplicacionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ParametroAplicacion> specification = createSpecification(criteria);
        return parametroAplicacionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ParametroAplicacion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParametroAplicacion> findByCriteria(ParametroAplicacionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ParametroAplicacion> specification = createSpecification(criteria);
        return parametroAplicacionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParametroAplicacionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ParametroAplicacion> specification = createSpecification(criteria);
        return parametroAplicacionRepository.count(specification);
    }

    /**
     * Function to convert {@link ParametroAplicacionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ParametroAplicacion> createSpecification(ParametroAplicacionCriteria criteria) {
        Specification<ParametroAplicacion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ParametroAplicacion_.id));
            }
            if (criteria.getMaxDiasEncuesta() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getMaxDiasEncuesta(), ParametroAplicacion_.maxDiasEncuesta));
            }
            if (criteria.getMinDiasEncuesta() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getMinDiasEncuesta(), ParametroAplicacion_.minDiasEncuesta));
            }
            if (criteria.getMaxCantidadPreguntas() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getMaxCantidadPreguntas(), ParametroAplicacion_.maxCantidadPreguntas)
                    );
            }
            if (criteria.getMinCantidadPreguntas() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getMinCantidadPreguntas(), ParametroAplicacion_.minCantidadPreguntas)
                    );
            }
        }
        return specification;
    }
}

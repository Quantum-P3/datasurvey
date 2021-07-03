package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.PPreguntaCerradaOpcion;
import org.datasurvey.repository.PPreguntaCerradaOpcionRepository;
import org.datasurvey.service.criteria.PPreguntaCerradaOpcionCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PPreguntaCerradaOpcion} entities in the database.
 * The main input is a {@link PPreguntaCerradaOpcionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PPreguntaCerradaOpcion} or a {@link Page} of {@link PPreguntaCerradaOpcion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PPreguntaCerradaOpcionQueryService extends QueryService<PPreguntaCerradaOpcion> {

    private final Logger log = LoggerFactory.getLogger(PPreguntaCerradaOpcionQueryService.class);

    private final PPreguntaCerradaOpcionRepository pPreguntaCerradaOpcionRepository;

    public PPreguntaCerradaOpcionQueryService(PPreguntaCerradaOpcionRepository pPreguntaCerradaOpcionRepository) {
        this.pPreguntaCerradaOpcionRepository = pPreguntaCerradaOpcionRepository;
    }

    /**
     * Return a {@link List} of {@link PPreguntaCerradaOpcion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PPreguntaCerradaOpcion> findByCriteria(PPreguntaCerradaOpcionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PPreguntaCerradaOpcion> specification = createSpecification(criteria);
        return pPreguntaCerradaOpcionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PPreguntaCerradaOpcion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PPreguntaCerradaOpcion> findByCriteria(PPreguntaCerradaOpcionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PPreguntaCerradaOpcion> specification = createSpecification(criteria);
        return pPreguntaCerradaOpcionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PPreguntaCerradaOpcionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PPreguntaCerradaOpcion> specification = createSpecification(criteria);
        return pPreguntaCerradaOpcionRepository.count(specification);
    }

    /**
     * Function to convert {@link PPreguntaCerradaOpcionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PPreguntaCerradaOpcion> createSpecification(PPreguntaCerradaOpcionCriteria criteria) {
        Specification<PPreguntaCerradaOpcion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PPreguntaCerradaOpcion_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), PPreguntaCerradaOpcion_.nombre));
            }
            if (criteria.getOrden() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrden(), PPreguntaCerradaOpcion_.orden));
            }
            if (criteria.getPPreguntaCerradaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPPreguntaCerradaId(),
                            root -> root.join(PPreguntaCerradaOpcion_.pPreguntaCerrada, JoinType.LEFT).get(PPreguntaCerrada_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

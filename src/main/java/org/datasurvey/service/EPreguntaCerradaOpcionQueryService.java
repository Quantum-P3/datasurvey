package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.EPreguntaCerradaOpcion;
import org.datasurvey.repository.EPreguntaCerradaOpcionRepository;
import org.datasurvey.service.criteria.EPreguntaCerradaOpcionCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EPreguntaCerradaOpcion} entities in the database.
 * The main input is a {@link EPreguntaCerradaOpcionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EPreguntaCerradaOpcion} or a {@link Page} of {@link EPreguntaCerradaOpcion} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EPreguntaCerradaOpcionQueryService extends QueryService<EPreguntaCerradaOpcion> {

    private final Logger log = LoggerFactory.getLogger(EPreguntaCerradaOpcionQueryService.class);

    private final EPreguntaCerradaOpcionRepository ePreguntaCerradaOpcionRepository;

    public EPreguntaCerradaOpcionQueryService(EPreguntaCerradaOpcionRepository ePreguntaCerradaOpcionRepository) {
        this.ePreguntaCerradaOpcionRepository = ePreguntaCerradaOpcionRepository;
    }

    /**
     * Return a {@link List} of {@link EPreguntaCerradaOpcion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EPreguntaCerradaOpcion> findByCriteria(EPreguntaCerradaOpcionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EPreguntaCerradaOpcion> specification = createSpecification(criteria);
        return ePreguntaCerradaOpcionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EPreguntaCerradaOpcion} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EPreguntaCerradaOpcion> findByCriteria(EPreguntaCerradaOpcionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EPreguntaCerradaOpcion> specification = createSpecification(criteria);
        return ePreguntaCerradaOpcionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EPreguntaCerradaOpcionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EPreguntaCerradaOpcion> specification = createSpecification(criteria);
        return ePreguntaCerradaOpcionRepository.count(specification);
    }

    /**
     * Function to convert {@link EPreguntaCerradaOpcionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EPreguntaCerradaOpcion> createSpecification(EPreguntaCerradaOpcionCriteria criteria) {
        Specification<EPreguntaCerradaOpcion> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EPreguntaCerradaOpcion_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), EPreguntaCerradaOpcion_.nombre));
            }
            if (criteria.getOrden() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrden(), EPreguntaCerradaOpcion_.orden));
            }
            if (criteria.getCantidad() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCantidad(), EPreguntaCerradaOpcion_.cantidad));
            }
            if (criteria.getEPreguntaCerradaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEPreguntaCerradaId(),
                            root -> root.join(EPreguntaCerradaOpcion_.ePreguntaCerrada, JoinType.LEFT).get(EPreguntaCerrada_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

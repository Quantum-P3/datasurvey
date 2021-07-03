package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.EPreguntaCerrada;
import org.datasurvey.repository.EPreguntaCerradaRepository;
import org.datasurvey.service.criteria.EPreguntaCerradaCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EPreguntaCerrada} entities in the database.
 * The main input is a {@link EPreguntaCerradaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EPreguntaCerrada} or a {@link Page} of {@link EPreguntaCerrada} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EPreguntaCerradaQueryService extends QueryService<EPreguntaCerrada> {

    private final Logger log = LoggerFactory.getLogger(EPreguntaCerradaQueryService.class);

    private final EPreguntaCerradaRepository ePreguntaCerradaRepository;

    public EPreguntaCerradaQueryService(EPreguntaCerradaRepository ePreguntaCerradaRepository) {
        this.ePreguntaCerradaRepository = ePreguntaCerradaRepository;
    }

    /**
     * Return a {@link List} of {@link EPreguntaCerrada} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EPreguntaCerrada> findByCriteria(EPreguntaCerradaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EPreguntaCerrada> specification = createSpecification(criteria);
        return ePreguntaCerradaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EPreguntaCerrada} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EPreguntaCerrada> findByCriteria(EPreguntaCerradaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EPreguntaCerrada> specification = createSpecification(criteria);
        return ePreguntaCerradaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EPreguntaCerradaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EPreguntaCerrada> specification = createSpecification(criteria);
        return ePreguntaCerradaRepository.count(specification);
    }

    /**
     * Function to convert {@link EPreguntaCerradaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EPreguntaCerrada> createSpecification(EPreguntaCerradaCriteria criteria) {
        Specification<EPreguntaCerrada> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EPreguntaCerrada_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), EPreguntaCerrada_.nombre));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), EPreguntaCerrada_.tipo));
            }
            if (criteria.getOpcional() != null) {
                specification = specification.and(buildSpecification(criteria.getOpcional(), EPreguntaCerrada_.opcional));
            }
            if (criteria.getOrden() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrden(), EPreguntaCerrada_.orden));
            }
            if (criteria.getEPreguntaCerradaOpcionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEPreguntaCerradaOpcionId(),
                            root -> root.join(EPreguntaCerrada_.ePreguntaCerradaOpcions, JoinType.LEFT).get(EPreguntaCerradaOpcion_.id)
                        )
                    );
            }
            if (criteria.getEncuestaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEncuestaId(),
                            root -> root.join(EPreguntaCerrada_.encuesta, JoinType.LEFT).get(Encuesta_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

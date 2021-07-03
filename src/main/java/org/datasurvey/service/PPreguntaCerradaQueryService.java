package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.PPreguntaCerrada;
import org.datasurvey.repository.PPreguntaCerradaRepository;
import org.datasurvey.service.criteria.PPreguntaCerradaCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PPreguntaCerrada} entities in the database.
 * The main input is a {@link PPreguntaCerradaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PPreguntaCerrada} or a {@link Page} of {@link PPreguntaCerrada} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PPreguntaCerradaQueryService extends QueryService<PPreguntaCerrada> {

    private final Logger log = LoggerFactory.getLogger(PPreguntaCerradaQueryService.class);

    private final PPreguntaCerradaRepository pPreguntaCerradaRepository;

    public PPreguntaCerradaQueryService(PPreguntaCerradaRepository pPreguntaCerradaRepository) {
        this.pPreguntaCerradaRepository = pPreguntaCerradaRepository;
    }

    /**
     * Return a {@link List} of {@link PPreguntaCerrada} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PPreguntaCerrada> findByCriteria(PPreguntaCerradaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PPreguntaCerrada> specification = createSpecification(criteria);
        return pPreguntaCerradaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PPreguntaCerrada} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PPreguntaCerrada> findByCriteria(PPreguntaCerradaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PPreguntaCerrada> specification = createSpecification(criteria);
        return pPreguntaCerradaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PPreguntaCerradaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PPreguntaCerrada> specification = createSpecification(criteria);
        return pPreguntaCerradaRepository.count(specification);
    }

    /**
     * Function to convert {@link PPreguntaCerradaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PPreguntaCerrada> createSpecification(PPreguntaCerradaCriteria criteria) {
        Specification<PPreguntaCerrada> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PPreguntaCerrada_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), PPreguntaCerrada_.nombre));
            }
            if (criteria.getTipo() != null) {
                specification = specification.and(buildSpecification(criteria.getTipo(), PPreguntaCerrada_.tipo));
            }
            if (criteria.getOpcional() != null) {
                specification = specification.and(buildSpecification(criteria.getOpcional(), PPreguntaCerrada_.opcional));
            }
            if (criteria.getOrden() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrden(), PPreguntaCerrada_.orden));
            }
            if (criteria.getPPreguntaCerradaOpcionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPPreguntaCerradaOpcionId(),
                            root -> root.join(PPreguntaCerrada_.pPreguntaCerradaOpcions, JoinType.LEFT).get(PPreguntaCerradaOpcion_.id)
                        )
                    );
            }
            if (criteria.getPlantillaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlantillaId(),
                            root -> root.join(PPreguntaCerrada_.plantilla, JoinType.LEFT).get(Plantilla_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

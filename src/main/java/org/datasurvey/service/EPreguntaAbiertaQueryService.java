package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.EPreguntaAbierta;
import org.datasurvey.repository.EPreguntaAbiertaRepository;
import org.datasurvey.service.criteria.EPreguntaAbiertaCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EPreguntaAbierta} entities in the database.
 * The main input is a {@link EPreguntaAbiertaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EPreguntaAbierta} or a {@link Page} of {@link EPreguntaAbierta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EPreguntaAbiertaQueryService extends QueryService<EPreguntaAbierta> {

    private final Logger log = LoggerFactory.getLogger(EPreguntaAbiertaQueryService.class);

    private final EPreguntaAbiertaRepository ePreguntaAbiertaRepository;

    public EPreguntaAbiertaQueryService(EPreguntaAbiertaRepository ePreguntaAbiertaRepository) {
        this.ePreguntaAbiertaRepository = ePreguntaAbiertaRepository;
    }

    /**
     * Return a {@link List} of {@link EPreguntaAbierta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EPreguntaAbierta> findByCriteria(EPreguntaAbiertaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EPreguntaAbierta> specification = createSpecification(criteria);
        return ePreguntaAbiertaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EPreguntaAbierta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EPreguntaAbierta> findByCriteria(EPreguntaAbiertaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EPreguntaAbierta> specification = createSpecification(criteria);
        return ePreguntaAbiertaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EPreguntaAbiertaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EPreguntaAbierta> specification = createSpecification(criteria);
        return ePreguntaAbiertaRepository.count(specification);
    }

    /**
     * Function to convert {@link EPreguntaAbiertaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EPreguntaAbierta> createSpecification(EPreguntaAbiertaCriteria criteria) {
        Specification<EPreguntaAbierta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EPreguntaAbierta_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), EPreguntaAbierta_.nombre));
            }
            if (criteria.getOpcional() != null) {
                specification = specification.and(buildSpecification(criteria.getOpcional(), EPreguntaAbierta_.opcional));
            }
            if (criteria.getOrden() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrden(), EPreguntaAbierta_.orden));
            }
            if (criteria.getEPreguntaAbiertaRespuestaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEPreguntaAbiertaRespuestaId(),
                            root ->
                                root.join(EPreguntaAbierta_.ePreguntaAbiertaRespuestas, JoinType.LEFT).get(EPreguntaAbiertaRespuesta_.id)
                        )
                    );
            }
            if (criteria.getEncuestaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEncuestaId(),
                            root -> root.join(EPreguntaAbierta_.encuesta, JoinType.LEFT).get(Encuesta_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

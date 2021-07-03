package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.EPreguntaAbiertaRespuesta;
import org.datasurvey.repository.EPreguntaAbiertaRespuestaRepository;
import org.datasurvey.service.criteria.EPreguntaAbiertaRespuestaCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EPreguntaAbiertaRespuesta} entities in the database.
 * The main input is a {@link EPreguntaAbiertaRespuestaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EPreguntaAbiertaRespuesta} or a {@link Page} of {@link EPreguntaAbiertaRespuesta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EPreguntaAbiertaRespuestaQueryService extends QueryService<EPreguntaAbiertaRespuesta> {

    private final Logger log = LoggerFactory.getLogger(EPreguntaAbiertaRespuestaQueryService.class);

    private final EPreguntaAbiertaRespuestaRepository ePreguntaAbiertaRespuestaRepository;

    public EPreguntaAbiertaRespuestaQueryService(EPreguntaAbiertaRespuestaRepository ePreguntaAbiertaRespuestaRepository) {
        this.ePreguntaAbiertaRespuestaRepository = ePreguntaAbiertaRespuestaRepository;
    }

    /**
     * Return a {@link List} of {@link EPreguntaAbiertaRespuesta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EPreguntaAbiertaRespuesta> findByCriteria(EPreguntaAbiertaRespuestaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EPreguntaAbiertaRespuesta> specification = createSpecification(criteria);
        return ePreguntaAbiertaRespuestaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link EPreguntaAbiertaRespuesta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EPreguntaAbiertaRespuesta> findByCriteria(EPreguntaAbiertaRespuestaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EPreguntaAbiertaRespuesta> specification = createSpecification(criteria);
        return ePreguntaAbiertaRespuestaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EPreguntaAbiertaRespuestaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EPreguntaAbiertaRespuesta> specification = createSpecification(criteria);
        return ePreguntaAbiertaRespuestaRepository.count(specification);
    }

    /**
     * Function to convert {@link EPreguntaAbiertaRespuestaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EPreguntaAbiertaRespuesta> createSpecification(EPreguntaAbiertaRespuestaCriteria criteria) {
        Specification<EPreguntaAbiertaRespuesta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EPreguntaAbiertaRespuesta_.id));
            }
            if (criteria.getRespuesta() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRespuesta(), EPreguntaAbiertaRespuesta_.respuesta));
            }
            if (criteria.getEPreguntaAbiertaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEPreguntaAbiertaId(),
                            root -> root.join(EPreguntaAbiertaRespuesta_.ePreguntaAbierta, JoinType.LEFT).get(EPreguntaAbierta_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.UsuarioEncuesta;
import org.datasurvey.repository.UsuarioEncuestaRepository;
import org.datasurvey.service.criteria.UsuarioEncuestaCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UsuarioEncuesta} entities in the database.
 * The main input is a {@link UsuarioEncuestaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsuarioEncuesta} or a {@link Page} of {@link UsuarioEncuesta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioEncuestaQueryService extends QueryService<UsuarioEncuesta> {

    private final Logger log = LoggerFactory.getLogger(UsuarioEncuestaQueryService.class);

    private final UsuarioEncuestaRepository usuarioEncuestaRepository;

    public UsuarioEncuestaQueryService(UsuarioEncuestaRepository usuarioEncuestaRepository) {
        this.usuarioEncuestaRepository = usuarioEncuestaRepository;
    }

    /**
     * Return a {@link List} of {@link UsuarioEncuesta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsuarioEncuesta> findByCriteria(UsuarioEncuestaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UsuarioEncuesta> specification = createSpecification(criteria);
        return usuarioEncuestaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UsuarioEncuesta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsuarioEncuesta> findByCriteria(UsuarioEncuestaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UsuarioEncuesta> specification = createSpecification(criteria);
        return usuarioEncuestaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsuarioEncuestaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UsuarioEncuesta> specification = createSpecification(criteria);
        return usuarioEncuestaRepository.count(specification);
    }

    /**
     * Function to convert {@link UsuarioEncuestaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UsuarioEncuesta> createSpecification(UsuarioEncuestaCriteria criteria) {
        Specification<UsuarioEncuesta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UsuarioEncuesta_.id));
            }
            if (criteria.getRol() != null) {
                specification = specification.and(buildSpecification(criteria.getRol(), UsuarioEncuesta_.rol));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), UsuarioEncuesta_.estado));
            }
            if (criteria.getFechaAgregado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaAgregado(), UsuarioEncuesta_.fechaAgregado));
            }
            if (criteria.getUsuarioExtraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioExtraId(),
                            root -> root.join(UsuarioEncuesta_.usuarioExtra, JoinType.LEFT).get(UsuarioExtra_.id)
                        )
                    );
            }
            if (criteria.getEncuestaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEncuestaId(),
                            root -> root.join(UsuarioEncuesta_.encuesta, JoinType.LEFT).get(Encuesta_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

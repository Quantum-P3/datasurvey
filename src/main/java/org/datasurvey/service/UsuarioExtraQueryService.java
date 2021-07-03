package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.UsuarioExtra;
import org.datasurvey.repository.UsuarioExtraRepository;
import org.datasurvey.service.criteria.UsuarioExtraCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link UsuarioExtra} entities in the database.
 * The main input is a {@link UsuarioExtraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UsuarioExtra} or a {@link Page} of {@link UsuarioExtra} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioExtraQueryService extends QueryService<UsuarioExtra> {

    private final Logger log = LoggerFactory.getLogger(UsuarioExtraQueryService.class);

    private final UsuarioExtraRepository usuarioExtraRepository;

    public UsuarioExtraQueryService(UsuarioExtraRepository usuarioExtraRepository) {
        this.usuarioExtraRepository = usuarioExtraRepository;
    }

    /**
     * Return a {@link List} of {@link UsuarioExtra} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UsuarioExtra> findByCriteria(UsuarioExtraCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UsuarioExtra> specification = createSpecification(criteria);
        return usuarioExtraRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link UsuarioExtra} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UsuarioExtra> findByCriteria(UsuarioExtraCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UsuarioExtra> specification = createSpecification(criteria);
        return usuarioExtraRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UsuarioExtraCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UsuarioExtra> specification = createSpecification(criteria);
        return usuarioExtraRepository.count(specification);
    }

    /**
     * Function to convert {@link UsuarioExtraCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UsuarioExtra> createSpecification(UsuarioExtraCriteria criteria) {
        Specification<UsuarioExtra> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UsuarioExtra_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), UsuarioExtra_.nombre));
            }
            if (criteria.getIconoPerfil() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIconoPerfil(), UsuarioExtra_.iconoPerfil));
            }
            if (criteria.getFechaNacimiento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaNacimiento(), UsuarioExtra_.fechaNacimiento));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), UsuarioExtra_.estado));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(UsuarioExtra_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getEncuestaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEncuestaId(),
                            root -> root.join(UsuarioExtra_.encuestas, JoinType.LEFT).get(Encuesta_.id)
                        )
                    );
            }
            if (criteria.getUsuarioEncuestaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioEncuestaId(),
                            root -> root.join(UsuarioExtra_.usuarioEncuestas, JoinType.LEFT).get(UsuarioEncuesta_.id)
                        )
                    );
            }
            if (criteria.getPlantillaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlantillaId(),
                            root -> root.join(UsuarioExtra_.plantillas, JoinType.LEFT).get(Plantilla_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

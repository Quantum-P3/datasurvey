package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.Encuesta;
import org.datasurvey.repository.EncuestaRepository;
import org.datasurvey.service.criteria.EncuestaCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Encuesta} entities in the database.
 * The main input is a {@link EncuestaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Encuesta} or a {@link Page} of {@link Encuesta} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EncuestaQueryService extends QueryService<Encuesta> {

    private final Logger log = LoggerFactory.getLogger(EncuestaQueryService.class);

    private final EncuestaRepository encuestaRepository;

    public EncuestaQueryService(EncuestaRepository encuestaRepository) {
        this.encuestaRepository = encuestaRepository;
    }

    /**
     * Return a {@link List} of {@link Encuesta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Encuesta> findByCriteria(EncuestaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Encuesta> specification = createSpecification(criteria);
        return encuestaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Encuesta} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Encuesta> findByCriteria(EncuestaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Encuesta> specification = createSpecification(criteria);
        return encuestaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EncuestaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Encuesta> specification = createSpecification(criteria);
        return encuestaRepository.count(specification);
    }

    /**
     * Function to convert {@link EncuestaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Encuesta> createSpecification(EncuestaCriteria criteria) {
        Specification<Encuesta> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Encuesta_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Encuesta_.nombre));
            }
            if (criteria.getDescripcion() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescripcion(), Encuesta_.descripcion));
            }
            if (criteria.getFechaCreacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaCreacion(), Encuesta_.fechaCreacion));
            }
            if (criteria.getFechaPublicacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaPublicacion(), Encuesta_.fechaPublicacion));
            }
            if (criteria.getFechaFinalizar() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaFinalizar(), Encuesta_.fechaFinalizar));
            }
            if (criteria.getFechaFinalizada() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFechaFinalizada(), Encuesta_.fechaFinalizada));
            }
            if (criteria.getCalificacion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCalificacion(), Encuesta_.calificacion));
            }
            if (criteria.getAcceso() != null) {
                specification = specification.and(buildSpecification(criteria.getAcceso(), Encuesta_.acceso));
            }
            if (criteria.getContrasenna() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContrasenna(), Encuesta_.contrasenna));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Encuesta_.estado));
            }
            if (criteria.getUsuarioEncuestaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioEncuestaId(),
                            root -> root.join(Encuesta_.usuarioEncuestas, JoinType.LEFT).get(UsuarioEncuesta_.id)
                        )
                    );
            }
            if (criteria.getEPreguntaAbiertaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEPreguntaAbiertaId(),
                            root -> root.join(Encuesta_.ePreguntaAbiertas, JoinType.LEFT).get(EPreguntaAbierta_.id)
                        )
                    );
            }
            if (criteria.getEPreguntaCerradaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEPreguntaCerradaId(),
                            root -> root.join(Encuesta_.ePreguntaCerradas, JoinType.LEFT).get(EPreguntaCerrada_.id)
                        )
                    );
            }
            if (criteria.getCategoriaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCategoriaId(),
                            root -> root.join(Encuesta_.categoria, JoinType.LEFT).get(Categoria_.id)
                        )
                    );
            }
            if (criteria.getUsuarioExtraId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUsuarioExtraId(),
                            root -> root.join(Encuesta_.usuarioExtra, JoinType.LEFT).get(UsuarioExtra_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

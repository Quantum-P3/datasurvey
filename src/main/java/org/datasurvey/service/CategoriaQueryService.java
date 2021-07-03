package org.datasurvey.service;

import java.util.List;
import javax.persistence.criteria.JoinType;
import org.datasurvey.domain.*; // for static metamodels
import org.datasurvey.domain.Categoria;
import org.datasurvey.repository.CategoriaRepository;
import org.datasurvey.service.criteria.CategoriaCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Categoria} entities in the database.
 * The main input is a {@link CategoriaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Categoria} or a {@link Page} of {@link Categoria} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CategoriaQueryService extends QueryService<Categoria> {

    private final Logger log = LoggerFactory.getLogger(CategoriaQueryService.class);

    private final CategoriaRepository categoriaRepository;

    public CategoriaQueryService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * Return a {@link List} of {@link Categoria} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Categoria> findByCriteria(CategoriaCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Categoria> specification = createSpecification(criteria);
        return categoriaRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Categoria} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Categoria> findByCriteria(CategoriaCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Categoria> specification = createSpecification(criteria);
        return categoriaRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CategoriaCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Categoria> specification = createSpecification(criteria);
        return categoriaRepository.count(specification);
    }

    /**
     * Function to convert {@link CategoriaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Categoria> createSpecification(CategoriaCriteria criteria) {
        Specification<Categoria> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Categoria_.id));
            }
            if (criteria.getNombre() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNombre(), Categoria_.nombre));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Categoria_.estado));
            }
            if (criteria.getEncuestaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEncuestaId(),
                            root -> root.join(Categoria_.encuestas, JoinType.LEFT).get(Encuesta_.id)
                        )
                    );
            }
            if (criteria.getPlantillaId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlantillaId(),
                            root -> root.join(Categoria_.plantillas, JoinType.LEFT).get(Plantilla_.id)
                        )
                    );
            }
        }
        return specification;
    }
}

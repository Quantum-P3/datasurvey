package org.datasurvey.repository;

import java.util.List;
import java.util.Optional;
import org.datasurvey.domain.UsuarioExtra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UsuarioExtra entity.
 */
@Repository
public interface UsuarioExtraRepository extends JpaRepository<UsuarioExtra, Long>, JpaSpecificationExecutor<UsuarioExtra> {
    @Query(
        value = "select distinct usuarioExtra from UsuarioExtra usuarioExtra left join fetch usuarioExtra.plantillas",
        countQuery = "select count(distinct usuarioExtra) from UsuarioExtra usuarioExtra"
    )
    Page<UsuarioExtra> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct usuarioExtra from UsuarioExtra usuarioExtra left join fetch usuarioExtra.plantillas")
    List<UsuarioExtra> findAllWithEagerRelationships();

    @Query("select usuarioExtra from UsuarioExtra usuarioExtra left join fetch usuarioExtra.plantillas where usuarioExtra.id =:id")
    Optional<UsuarioExtra> findOneWithEagerRelationships(@Param("id") Long id);
}

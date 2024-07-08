package af.mcit.customsystem.repository;

import af.mcit.customsystem.domain.Tarif;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tarif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TarifRepository extends JpaRepository<Tarif, Long>, JpaSpecificationExecutor<Tarif> {}

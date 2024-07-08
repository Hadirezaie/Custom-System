package af.mcit.customsystem.repository;

import af.mcit.customsystem.domain.CustomCharges;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomCharges entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomChargesRepository extends JpaRepository<CustomCharges, Long>, JpaSpecificationExecutor<CustomCharges> {}

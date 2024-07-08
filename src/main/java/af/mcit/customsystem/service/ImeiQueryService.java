package af.mcit.customsystem.service;

import af.mcit.customsystem.domain.*; // for static metamodels
import af.mcit.customsystem.domain.Imei;
import af.mcit.customsystem.repository.ImeiRepository;
import af.mcit.customsystem.service.criteria.ImeiCriteria;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Imei} entities in the database.
 * The main input is a {@link ImeiCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Imei} or a {@link Page} of {@link Imei} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ImeiQueryService extends QueryService<Imei> {

    private final Logger log = LoggerFactory.getLogger(ImeiQueryService.class);

    private final ImeiRepository imeiRepository;

    public ImeiQueryService(ImeiRepository imeiRepository) {
        this.imeiRepository = imeiRepository;
    }

    /**
     * Return a {@link List} of {@link Imei} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Imei> findByCriteria(ImeiCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Imei> specification = createSpecification(criteria);
        return imeiRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Imei} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Imei> findByCriteria(ImeiCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Imei> specification = createSpecification(criteria);
        return imeiRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ImeiCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Imei> specification = createSpecification(criteria);
        return imeiRepository.count(specification);
    }

    /**
     * Function to convert {@link ImeiCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Imei> createSpecification(ImeiCriteria criteria) {
        Specification<Imei> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Imei_.id));
            }
            if (criteria.getImeiNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getImeiNumber(), Imei_.imeiNumber));
            }
            if (criteria.getDeviceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDeviceId(), root -> root.join(Imei_.device, JoinType.LEFT).get(Device_.id))
                    );
            }
        }
        return specification;
    }
}

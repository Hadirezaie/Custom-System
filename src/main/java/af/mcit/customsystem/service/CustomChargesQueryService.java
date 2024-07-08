package af.mcit.customsystem.service;

import af.mcit.customsystem.domain.*; // for static metamodels
import af.mcit.customsystem.domain.CustomCharges;
import af.mcit.customsystem.repository.CustomChargesRepository;
import af.mcit.customsystem.service.criteria.CustomChargesCriteria;
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
 * Service for executing complex queries for {@link CustomCharges} entities in the database.
 * The main input is a {@link CustomChargesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomCharges} or a {@link Page} of {@link CustomCharges} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomChargesQueryService extends QueryService<CustomCharges> {

    private final Logger log = LoggerFactory.getLogger(CustomChargesQueryService.class);

    private final CustomChargesRepository customChargesRepository;

    public CustomChargesQueryService(CustomChargesRepository customChargesRepository) {
        this.customChargesRepository = customChargesRepository;
    }

    /**
     * Return a {@link List} of {@link CustomCharges} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomCharges> findByCriteria(CustomChargesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomCharges> specification = createSpecification(criteria);
        return customChargesRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CustomCharges} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomCharges> findByCriteria(CustomChargesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomCharges> specification = createSpecification(criteria);
        return customChargesRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomChargesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomCharges> specification = createSpecification(criteria);
        return customChargesRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomChargesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomCharges> createSpecification(CustomChargesCriteria criteria) {
        Specification<CustomCharges> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomCharges_.id));
            }
            if (criteria.getDeviceModel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDeviceModel(), CustomCharges_.deviceModel));
            }
            if (criteria.getCustomFee() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCustomFee(), CustomCharges_.customFee));
            }
        }
        return specification;
    }
}

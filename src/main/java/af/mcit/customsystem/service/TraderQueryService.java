package af.mcit.customsystem.service;

import af.mcit.customsystem.domain.*; // for static metamodels
import af.mcit.customsystem.domain.Trader;
import af.mcit.customsystem.repository.TraderRepository;
import af.mcit.customsystem.service.criteria.TraderCriteria;
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
 * Service for executing complex queries for {@link Trader} entities in the database.
 * The main input is a {@link TraderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Trader} or a {@link Page} of {@link Trader} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TraderQueryService extends QueryService<Trader> {

    private final Logger log = LoggerFactory.getLogger(TraderQueryService.class);

    private final TraderRepository traderRepository;

    public TraderQueryService(TraderRepository traderRepository) {
        this.traderRepository = traderRepository;
    }

    /**
     * Return a {@link List} of {@link Trader} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Trader> findByCriteria(TraderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Trader> specification = createSpecification(criteria);
        return traderRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Trader} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Trader> findByCriteria(TraderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Trader> specification = createSpecification(criteria);
        return traderRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TraderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Trader> specification = createSpecification(criteria);
        return traderRepository.count(specification);
    }

    /**
     * Function to convert {@link TraderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Trader> createSpecification(TraderCriteria criteria) {
        Specification<Trader> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Trader_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Trader_.name));
            }
            if (criteria.getNidNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNidNumber(), Trader_.nidNumber));
            }
            if (criteria.gettIN() != null) {
                specification = specification.and(buildStringSpecification(criteria.gettIN(), Trader_.tIN));
            }
            if (criteria.getLicenseNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLicenseNumber(), Trader_.licenseNumber));
            }
        }
        return specification;
    }
}

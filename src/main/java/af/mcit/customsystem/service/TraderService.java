package af.mcit.customsystem.service;

import af.mcit.customsystem.domain.Trader;
import af.mcit.customsystem.repository.TraderRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Trader}.
 */
@Service
@Transactional
public class TraderService {

    private final Logger log = LoggerFactory.getLogger(TraderService.class);

    private final TraderRepository traderRepository;

    public TraderService(TraderRepository traderRepository) {
        this.traderRepository = traderRepository;
    }

    /**
     * Save a trader.
     *
     * @param trader the entity to save.
     * @return the persisted entity.
     */
    public Trader save(Trader trader) {
        log.debug("Request to save Trader : {}", trader);
        return traderRepository.save(trader);
    }

    /**
     * Update a trader.
     *
     * @param trader the entity to save.
     * @return the persisted entity.
     */
    public Trader update(Trader trader) {
        log.debug("Request to update Trader : {}", trader);
        return traderRepository.save(trader);
    }

    /**
     * Partially update a trader.
     *
     * @param trader the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Trader> partialUpdate(Trader trader) {
        log.debug("Request to partially update Trader : {}", trader);

        return traderRepository
            .findById(trader.getId())
            .map(existingTrader -> {
                if (trader.getName() != null) {
                    existingTrader.setName(trader.getName());
                }
                if (trader.getNidNumber() != null) {
                    existingTrader.setNidNumber(trader.getNidNumber());
                }
                if (trader.gettIN() != null) {
                    existingTrader.settIN(trader.gettIN());
                }
                if (trader.getLicenseNumber() != null) {
                    existingTrader.setLicenseNumber(trader.getLicenseNumber());
                }

                return existingTrader;
            })
            .map(traderRepository::save);
    }

    /**
     * Get all the traders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Trader> findAll(Pageable pageable) {
        log.debug("Request to get all Traders");
        return traderRepository.findAll(pageable);
    }

    /**
     * Get one trader by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Trader> findOne(Long id) {
        log.debug("Request to get Trader : {}", id);
        return traderRepository.findById(id);
    }

    /**
     * Delete the trader by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Trader : {}", id);
        traderRepository.deleteById(id);
    }
}

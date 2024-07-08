package af.mcit.customsystem.service;

import af.mcit.customsystem.domain.CustomCharges;
import af.mcit.customsystem.repository.CustomChargesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CustomCharges}.
 */
@Service
@Transactional
public class CustomChargesService {

    private final Logger log = LoggerFactory.getLogger(CustomChargesService.class);

    private final CustomChargesRepository customChargesRepository;

    public CustomChargesService(CustomChargesRepository customChargesRepository) {
        this.customChargesRepository = customChargesRepository;
    }

    /**
     * Save a customCharges.
     *
     * @param customCharges the entity to save.
     * @return the persisted entity.
     */
    public CustomCharges save(CustomCharges customCharges) {
        log.debug("Request to save CustomCharges : {}", customCharges);
        return customChargesRepository.save(customCharges);
    }

    /**
     * Update a customCharges.
     *
     * @param customCharges the entity to save.
     * @return the persisted entity.
     */
    public CustomCharges update(CustomCharges customCharges) {
        log.debug("Request to update CustomCharges : {}", customCharges);
        return customChargesRepository.save(customCharges);
    }

    /**
     * Partially update a customCharges.
     *
     * @param customCharges the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CustomCharges> partialUpdate(CustomCharges customCharges) {
        log.debug("Request to partially update CustomCharges : {}", customCharges);

        return customChargesRepository
            .findById(customCharges.getId())
            .map(existingCustomCharges -> {
                if (customCharges.getDeviceModel() != null) {
                    existingCustomCharges.setDeviceModel(customCharges.getDeviceModel());
                }
                if (customCharges.getCustomFee() != null) {
                    existingCustomCharges.setCustomFee(customCharges.getCustomFee());
                }

                return existingCustomCharges;
            })
            .map(customChargesRepository::save);
    }

    /**
     * Get all the customCharges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomCharges> findAll(Pageable pageable) {
        log.debug("Request to get all CustomCharges");
        return customChargesRepository.findAll(pageable);
    }

    /**
     * Get one customCharges by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomCharges> findOne(Long id) {
        log.debug("Request to get CustomCharges : {}", id);
        return customChargesRepository.findById(id);
    }

    /**
     * Delete the customCharges by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CustomCharges : {}", id);
        customChargesRepository.deleteById(id);
    }
}

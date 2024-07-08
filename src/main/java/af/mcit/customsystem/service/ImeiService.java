package af.mcit.customsystem.service;

import af.mcit.customsystem.domain.Imei;
import af.mcit.customsystem.repository.ImeiRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Imei}.
 */
@Service
@Transactional
public class ImeiService {

    private final Logger log = LoggerFactory.getLogger(ImeiService.class);

    private final ImeiRepository imeiRepository;

    public ImeiService(ImeiRepository imeiRepository) {
        this.imeiRepository = imeiRepository;
    }

    /**
     * Save a imei.
     *
     * @param imei the entity to save.
     * @return the persisted entity.
     */
    public Imei save(Imei imei) {
        log.debug("Request to save Imei : {}", imei);
        return imeiRepository.save(imei);
    }

    /**
     * Update a imei.
     *
     * @param imei the entity to save.
     * @return the persisted entity.
     */
    public Imei update(Imei imei) {
        log.debug("Request to update Imei : {}", imei);
        return imeiRepository.save(imei);
    }

    /**
     * Partially update a imei.
     *
     * @param imei the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Imei> partialUpdate(Imei imei) {
        log.debug("Request to partially update Imei : {}", imei);

        return imeiRepository
            .findById(imei.getId())
            .map(existingImei -> {
                if (imei.getImeiNumber() != null) {
                    existingImei.setImeiNumber(imei.getImeiNumber());
                }

                return existingImei;
            })
            .map(imeiRepository::save);
    }

    /**
     * Get all the imeis.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Imei> findAll(Pageable pageable) {
        log.debug("Request to get all Imeis");
        return imeiRepository.findAll(pageable);
    }

    /**
     * Get one imei by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Imei> findOne(Long id) {
        log.debug("Request to get Imei : {}", id);
        return imeiRepository.findById(id);
    }

    /**
     * Delete the imei by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Imei : {}", id);
        imeiRepository.deleteById(id);
    }
}

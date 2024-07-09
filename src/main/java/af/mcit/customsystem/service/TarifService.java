package af.mcit.customsystem.service;

import af.mcit.customsystem.domain.Tarif;
import af.mcit.customsystem.repository.TarifRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Tarif}.
 */
@Service
@Transactional
public class TarifService {

    private final Logger log = LoggerFactory.getLogger(TarifService.class);

    private final TarifRepository tarifRepository;

    public TarifService(TarifRepository tarifRepository) {
        this.tarifRepository = tarifRepository;
    }

    /**
     * Save a tarif.
     *
     * @param tarif the entity to save.
     * @return the persisted entity.
     */
    public Tarif save(Tarif tarif) {
        log.debug("Request to save Tarif : {}", tarif);
        return tarifRepository.save(tarif);
    }

    /**
     * Update a tarif.
     *
     * @param tarif the entity to save.
     * @return the persisted entity.
     */
    public Tarif update(Tarif tarif) {
        log.debug("Request to update Tarif : {}", tarif);
        return tarifRepository.save(tarif);
    }

    /**
     * Partially update a tarif.
     *
     * @param tarif the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Tarif> partialUpdate(Tarif tarif) {
        log.debug("Request to partially update Tarif : {}", tarif);

        return tarifRepository
            .findById(tarif.getId())
            .map(existingTarif -> {
                if (tarif.getAmount() != null) {
                    existingTarif.setAmount(tarif.getAmount());
                }
                if (tarif.getPaidDate() != null) {
                    existingTarif.setPaidDate(tarif.getPaidDate());
                }
                if (tarif.getPaid() != null) {
                    existingTarif.setPaid(tarif.getPaid());
                }
                if (tarif.getNumberOfDevice() != null) {
                    existingTarif.setNumberOfDevice(tarif.getNumberOfDevice());
                }

                return existingTarif;
            })
            .map(tarifRepository::save);
    }

    /**
     * Get all the tarifs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Tarif> findAll(Pageable pageable) {
        log.debug("Request to get all Tarifs");
        return tarifRepository.findAll(pageable);
    }

    /**
     * Get one tarif by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Tarif> findOne(Long id) {
        log.debug("Request to get Tarif : {}", id);
        return tarifRepository.findById(id);
    }

    /**
     * Delete the tarif by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tarif : {}", id);
        tarifRepository.deleteById(id);
    }
}

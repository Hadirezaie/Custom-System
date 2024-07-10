package af.mcit.customsystem.service;

import af.mcit.customsystem.domain.CustomCharges;
import af.mcit.customsystem.domain.Device;
import af.mcit.customsystem.domain.Imei;
import af.mcit.customsystem.domain.Tarif;
import af.mcit.customsystem.domain.Trader;
import af.mcit.customsystem.repository.DeviceRepository;
import af.mcit.customsystem.repository.ImeiRepository;
import af.mcit.customsystem.repository.TarifRepository;
import af.mcit.customsystem.repository.TraderRepository;
import af.mcit.customsystem.service.criteria.CustomChargesCriteria;
import af.mcit.customsystem.service.criteria.ImeiCriteria;
import af.mcit.customsystem.service.criteria.TraderCriteria;
import af.mcit.customsystem.service.dto.ImeiCheckDTO;
import af.mcit.customsystem.service.dto.ImeiCheckResultDTO;
import af.mcit.customsystem.service.dto.ImeiRegisterDTO;
import af.mcit.customsystem.service.dto.ImeiRegisterResultDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Service Implementation for managing {@link Imei}.
 */
@Service
@Transactional
public class ImeiService {

    private final Logger log = LoggerFactory.getLogger(ImeiService.class);

    private final ImeiRepository imeiRepository;

    private final ImeiQueryService imeiQueryService;
    private final TraderQueryService traderQueryService;
    private final TraderRepository traderRepository;
    private final TarifQueryService tarifQueryService;
    private final TarifRepository tarifRepository;
    private final CustomChargesQueryService customChargesQueryService;
    private final DeviceQueryService deviceQueryService;
    private final DeviceRepository deviceRepository;

    public ImeiService(
        ImeiRepository imeiRepository,
        DeviceQueryService deviceQueryService,
        ImeiQueryService imeiQueryService,
        TraderQueryService traderQueryService,
        TraderRepository traderRepository,
        TarifQueryService tarifQueryService,
        TarifRepository tarifRepository,
        CustomChargesQueryService customChargesQueryService,
        DeviceRepository deviceRepository
    ) {
        this.imeiRepository = imeiRepository;
        this.deviceQueryService = deviceQueryService;
        this.imeiQueryService = imeiQueryService;
        this.traderQueryService = traderQueryService;
        this.traderRepository = traderRepository;
        this.tarifQueryService = tarifQueryService;
        this.tarifRepository = tarifRepository;
        this.customChargesQueryService = customChargesQueryService;
        this.deviceRepository = deviceRepository;
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

    public ImeiCheckResultDTO checkImei(ImeiCheckDTO imeiCheckDTO) {
        log.debug("Request to check Imei : {}", imeiCheckDTO);
        // Initialize resultDTO
        ImeiCheckResultDTO resultDTO = new ImeiCheckResultDTO();
        resultDTO.setImei(imeiCheckDTO.getImei());

        // Query for IMEI
        ImeiCriteria imeiCriteria = new ImeiCriteria();
        imeiCriteria.setImeiNumber((LongFilter) new LongFilter().setEquals(imeiCheckDTO.getImei()));
        List<Imei> imeis = imeiQueryService.findByCriteria(imeiCriteria);

        // Check if IMEI is not found
        if (imeis.isEmpty()) {
            resultDTO.setDescription("IMEI not found!");
            return resultDTO;
        }

        // Get the first IMEI (assuming IMEI is unique or order doesn't matter)
        Imei imei = imeis.get(0);

        // Check if Device is not found
        if (imei.getDevice() == null) {
            resultDTO.setDescription("Device not found!");
            return resultDTO;
        }

        // Set Device Model
        resultDTO.setDeviceModel(imei.getDevice().getModelNumber());

        // Check if Tarif is unpaid
        if (!imei.getDevice().getTarif().getPaid()) {
            resultDTO.setDescription("Unpaid!");
        } else {
            resultDTO.setDescription("Paid!");
            resultDTO.setTraderName(imei.getDevice().getTrader().getName());
            resultDTO.setTraderNidNumber(imei.getDevice().getTrader().getNidNumber());
        }

        return resultDTO;
    }

    // register
    @Transactional
    public ImeiRegisterResultDTO registerImei(ImeiRegisterDTO imeiRegisterDTO) {
        log.debug("Request to save Imei and device and trader : {}", imeiRegisterDTO);

        ImeiRegisterResultDTO imeiRegisterResultDTO = new ImeiRegisterResultDTO();

        try {
            // Check if trader exists or save new trader
            Trader trader = findOrCreateTrader(imeiRegisterDTO);

            // Find custom charges for the device model
            CustomCharges customCharges = findCustomCharges(imeiRegisterDTO);

            // Save tariff
            Tarif savedTarif = saveTarif(customCharges);

            // Save device
            Device savedDevice = saveDevice(imeiRegisterDTO, savedTarif, trader);

            // Save IMEIs
            saveImeis(imeiRegisterDTO.getImeis(), savedDevice);

            // Populate result DTO
            imeiRegisterResultDTO.setCustomCharge(savedTarif.getAmount());
            imeiRegisterResultDTO.setDeviceModel(savedDevice.getModelNumber());
            imeiRegisterResultDTO.setMessage("Success!");
            imeiRegisterResultDTO.setPaidStatus(savedTarif.getPaid().toString());
        } catch (Exception e) {
            // Handle any unexpected exception
            log.error("Error occurred while registering IMEI: {}", e.getMessage());
            imeiRegisterResultDTO.setMessage("Failed to register IMEI: " + e.getMessage());
        }

        return imeiRegisterResultDTO;
    }

    private Trader findOrCreateTrader(ImeiRegisterDTO imeiRegisterDTO) {
        TraderCriteria traderCriteria = new TraderCriteria();
        traderCriteria.setNidNumber((StringFilter) new StringFilter().setEquals(imeiRegisterDTO.getTraderNidNumber()));
        List<Trader> traders = traderQueryService.findByCriteria(traderCriteria);

        if (!traders.isEmpty()) {
            return traders.get(0); // Trader already exists
        }

        // Create and save new trader
        Trader trader = new Trader();
        trader.setLicenseNumber(imeiRegisterDTO.getTraderLicenseNumber());
        trader.setName(imeiRegisterDTO.getTraderName());
        trader.setNidNumber(imeiRegisterDTO.getTraderNidNumber());
        trader.settIN(imeiRegisterDTO.getTraderTIN());
        return traderRepository.save(trader);
    }

    private CustomCharges findCustomCharges(ImeiRegisterDTO imeiRegisterDTO) throws Exception {
        CustomChargesCriteria ccc = new CustomChargesCriteria();
        ccc.setDeviceModel((StringFilter) new StringFilter().setEquals(imeiRegisterDTO.getDeviceModelNumber()));
        List<CustomCharges> customCharges = customChargesQueryService.findByCriteria(ccc);

        if (customCharges.isEmpty()) {
            throw new Exception("Custom charges not found for device model: " + imeiRegisterDTO.getDeviceModelNumber());
        }

        return customCharges.get(0);
    }

    private Tarif saveTarif(CustomCharges customCharges) throws Exception {
        Tarif tarif = new Tarif();
        tarif.setAmount(customCharges.getCustomFee());
        tarif.setNumberOfDevice(1L);
        tarif.setPaid(false);
        return tarifRepository.save(tarif);
    }

    private Device saveDevice(ImeiRegisterDTO imeiRegisterDTO, Tarif savedTarif, Trader trader) throws Exception {
        Device device = new Device();
        device.setModelNumber(imeiRegisterDTO.getDeviceModelNumber());
        device.setSerialNumber(imeiRegisterDTO.getDeviceSerialNumber());
        device.setTarif(savedTarif);
        device.setTrader(trader);
        return deviceRepository.save(device);
    }

    private void saveImeis(Set<String> imeis, Device savedDevice) {
        imeis.forEach(imei -> {
            Imei imeiObj = new Imei();
            try {
                Long imeiNumber = Long.valueOf(imei);
                imeiObj.setImeiNumber(imeiNumber);
                imeiObj.setDevice(savedDevice);
                imeiRepository.save(imeiObj);
            } catch (NumberFormatException e) {
                log.error("Invalid IMEI format: {}", imei);
                // Handle invalid IMEI format if needed
            }
        });
    }
}

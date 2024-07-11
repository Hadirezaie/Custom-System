package af.mcit.customsystem.web.rest;

import af.mcit.customsystem.domain.CustomCharges;
import af.mcit.customsystem.domain.Device;
import af.mcit.customsystem.domain.Imei;
import af.mcit.customsystem.domain.Tarif;
import af.mcit.customsystem.domain.Trader;
import af.mcit.customsystem.repository.DeviceRepository;
import af.mcit.customsystem.repository.ImeiRepository;
import af.mcit.customsystem.repository.TarifRepository;
import af.mcit.customsystem.repository.TraderRepository;
import af.mcit.customsystem.service.CustomChargesQueryService;
import af.mcit.customsystem.service.DeviceQueryService;
import af.mcit.customsystem.service.DeviceService;
import af.mcit.customsystem.service.TraderQueryService;
import af.mcit.customsystem.service.criteria.CustomChargesCriteria;
import af.mcit.customsystem.service.criteria.DeviceCriteria;
import af.mcit.customsystem.service.criteria.TraderCriteria;
import af.mcit.customsystem.service.dto.DeviceDTO;
import af.mcit.customsystem.service.dto.DeviceRegisterBatchDTO;
import af.mcit.customsystem.service.dto.ImeiDTO;
import af.mcit.customsystem.service.dto.ImeiRegisterResultDTO;
import af.mcit.customsystem.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link af.mcit.customsystem.domain.Device}.
 */
@RestController
@RequestMapping("/api")
public class DeviceResource {

    private final Logger log = LoggerFactory.getLogger(DeviceResource.class);

    private static final String ENTITY_NAME = "device";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceService deviceService;

    private final DeviceRepository deviceRepository;

    private final DeviceQueryService deviceQueryService;
    private final TraderQueryService traderQueryService;
    private final TraderRepository traderRepository;
    private final CustomChargesQueryService customChargesQueryService;
    private final TarifRepository tarifRepository;
    private final ImeiRepository imeiRepository;

    public DeviceResource(
        DeviceService deviceService,
        DeviceRepository deviceRepository,
        DeviceQueryService deviceQueryService,
        TraderQueryService traderQueryService,
        TraderRepository traderRepository,
        CustomChargesQueryService customChargesQueryService,
        TarifRepository tarifRepository,
        ImeiRepository imeiRepository
    ) {
        this.deviceService = deviceService;
        this.deviceRepository = deviceRepository;
        this.deviceQueryService = deviceQueryService;
        this.traderQueryService = traderQueryService;
        this.traderRepository = traderRepository;
        this.customChargesQueryService = customChargesQueryService;
        this.tarifRepository = tarifRepository;
        this.imeiRepository = imeiRepository;
    }

    /**
     * {@code POST  /devices} : Create a new device.
     *
     * @param device the device to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new device, or with status {@code 400 (Bad Request)} if the
     *         device has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/devices")
    public ResponseEntity<Device> createDevice(@Valid @RequestBody Device device) throws URISyntaxException {
        log.debug("REST request to save Device : {}", device);
        if (device.getId() != null) {
            throw new BadRequestAlertException("A new device cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Device result = deviceService.save(device);
        return ResponseEntity
            .created(new URI("/api/devices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /devices/:id} : Updates an existing device.
     *
     * @param id     the id of the device to save.
     * @param device the device to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated device,
     *         or with status {@code 400 (Bad Request)} if the device is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the device
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/devices/{id}")
    public ResponseEntity<Device> updateDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Device device
    ) throws URISyntaxException {
        log.debug("REST request to update Device : {}, {}", id, device);
        if (device.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, device.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Device result = deviceService.update(device);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, device.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /devices/:id} : Partial updates given fields of an existing
     * device, field will ignore if it is null
     *
     * @param id     the id of the device to save.
     * @param device the device to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated device,
     *         or with status {@code 400 (Bad Request)} if the device is not valid,
     *         or with status {@code 404 (Not Found)} if the device is not found,
     *         or with status {@code 500 (Internal Server Error)} if the device
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/devices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Device> partialUpdateDevice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Device device
    ) throws URISyntaxException {
        log.debug("REST request to partial update Device partially : {}, {}", id, device);
        if (device.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, device.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Device> result = deviceService.partialUpdate(device);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, device.getId().toString())
        );
    }

    /**
     * {@code GET  /devices} : get all the devices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of devices in body.
     */
    @GetMapping("/devices")
    public ResponseEntity<List<Device>> getAllDevices(
        DeviceCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Devices by criteria: {}", criteria);
        Page<Device> page = deviceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /devices/count} : count all the devices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/devices/count")
    public ResponseEntity<Long> countDevices(DeviceCriteria criteria) {
        log.debug("REST request to count Devices by criteria: {}", criteria);
        return ResponseEntity.ok().body(deviceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /devices/:id} : get the "id" device.
     *
     * @param id the id of the device to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the device, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/devices/{id}")
    public ResponseEntity<Device> getDevice(@PathVariable Long id) {
        log.debug("REST request to get Device : {}", id);
        Optional<Device> device = deviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(device);
    }

    /**
     * {@code DELETE  /devices/:id} : delete the "id" device.
     *
     * @param id the id of the device to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/devices/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        log.debug("REST request to delete Device : {}", id);
        deviceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/devices/batch")
    public ResponseEntity<ImeiRegisterResultDTO> insertBatchDevices(@RequestBody DeviceRegisterBatchDTO batchRegDto) {
        Trader trader = findOrCreateTrader(batchRegDto.getTrader());
        Long totalCharge = 0L;

        batchRegDto
            .getDeviceRegisterDTOs()
            .forEach(batchDTO -> {
                try {
                    CustomCharges customCharges = findCustomCharges(batchDTO.getDeviceDTO());
                    Tarif savedTarif = saveTarif(customCharges);
                    // totalCharge=totalCharge+savedTarif.getAmount();

                    Device savedDevice = saveDevice(batchDTO.getDeviceDTO(), savedTarif, trader);

                    saveImeis(batchDTO.getImeis(), savedDevice);
                } catch (Exception e) {}
            });

        ImeiRegisterResultDTO regDto = new ImeiRegisterResultDTO();
        regDto.setMessage("Batch Inserted Successfully");
        regDto.setPaidStatus("UnPaid");
        regDto.setCustomCharge(totalCharge);
        return ResponseEntity.ok(regDto);
    }

    private Trader findOrCreateTrader(Trader trader) {
        TraderCriteria traderCriteria = new TraderCriteria();
        traderCriteria.setNidNumber((StringFilter) new StringFilter().setEquals(trader.getNidNumber()));
        List<Trader> traders = traderQueryService.findByCriteria(traderCriteria);

        if (!traders.isEmpty()) {
            return traders.get(0);
        }

        // Create and save new trader
        Trader newTrader = new Trader();
        newTrader.setLicenseNumber(trader.getLicenseNumber());
        newTrader.setName(trader.getName());
        newTrader.setNidNumber(trader.getNidNumber());
        newTrader.settIN(trader.gettIN());
        return traderRepository.save(trader);
    }

    private CustomCharges findCustomCharges(DeviceDTO deviceDTO) throws Exception {
        CustomChargesCriteria ccc = new CustomChargesCriteria();
        ccc.setDeviceModel((StringFilter) new StringFilter().setEquals(deviceDTO.getModelNumber()));
        List<CustomCharges> customCharges = customChargesQueryService.findByCriteria(ccc);

        if (customCharges.isEmpty()) {
            throw new Exception("Custom charges not found for device model: " + deviceDTO.getModelNumber());
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

    private Device saveDevice(DeviceDTO deviceDTO, Tarif savedTarif, Trader trader) throws Exception {
        Device newDevice = new Device();
        newDevice.setModelNumber(deviceDTO.getModelNumber());
        newDevice.setSerialNumber(deviceDTO.getSerialNumber());
        newDevice.setTarif(savedTarif);
        newDevice.setTrader(trader);
        return deviceRepository.save(newDevice);
    }

    private void saveImeis(List<ImeiDTO> imeis, Device savedDevice) {
        imeis.forEach(imei -> {
            Imei newImei = new Imei();
            try {
                newImei.setImeiNumber(imei.getImeiNumber());
                newImei.setDevice(savedDevice);
                imeiRepository.save(newImei);
            } catch (NumberFormatException e) {
                log.error("Invalid IMEI format: {}", imei);
            }
        });
    }
}

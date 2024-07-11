package af.mcit.customsystem.service;

import af.mcit.customsystem.domain.Device;
import af.mcit.customsystem.domain.Imei;
import af.mcit.customsystem.repository.DeviceRepository;
import af.mcit.customsystem.service.dto.DeviceRegisterBatchDTO;
import af.mcit.customsystem.service.dto.DeviceRegisterDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Device}.
 */
@Service
@Transactional
public class DeviceService {

    private final Logger log = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * Save a device.
     *
     * @param device the entity to save.
     * @return the persisted entity.
     */
    public Device save(Device device) {
        log.debug("Request to save Device : {}", device);
        return deviceRepository.save(device);
    }

    /**
     * Update a device.
     *
     * @param device the entity to save.
     * @return the persisted entity.
     */
    public Device update(Device device) {
        log.debug("Request to update Device : {}", device);
        return deviceRepository.save(device);
    }

    /**
     * Partially update a device.
     *
     * @param device the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Device> partialUpdate(Device device) {
        log.debug("Request to partially update Device : {}", device);

        return deviceRepository
            .findById(device.getId())
            .map(existingDevice -> {
                if (device.getSerialNumber() != null) {
                    existingDevice.setSerialNumber(device.getSerialNumber());
                }
                if (device.getModelNumber() != null) {
                    existingDevice.setModelNumber(device.getModelNumber());
                }

                return existingDevice;
            })
            .map(deviceRepository::save);
    }

    /**
     * Get all the devices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Device> findAll(Pageable pageable) {
        log.debug("Request to get all Devices");
        return deviceRepository.findAll(pageable);
    }

    /**
     * Get one device by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Device> findOne(Long id) {
        log.debug("Request to get Device : {}", id);
        return deviceRepository.findById(id);
    }

    /**
     * Delete the device by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Device : {}", id);
        deviceRepository.deleteById(id);
    }
}

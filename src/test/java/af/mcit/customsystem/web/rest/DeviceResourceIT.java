package af.mcit.customsystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import af.mcit.customsystem.IntegrationTest;
import af.mcit.customsystem.domain.Device;
import af.mcit.customsystem.domain.Trader;
import af.mcit.customsystem.repository.DeviceRepository;
import af.mcit.customsystem.service.criteria.DeviceCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeviceResourceIT {

    private static final String DEFAULT_SERIAL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SERIAL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceMockMvc;

    private Device device;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity(EntityManager em) {
        Device device = new Device().serialNumber(DEFAULT_SERIAL_NUMBER).modelNumber(DEFAULT_MODEL_NUMBER);
        return device;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createUpdatedEntity(EntityManager em) {
        Device device = new Device().serialNumber(UPDATED_SERIAL_NUMBER).modelNumber(UPDATED_MODEL_NUMBER);
        return device;
    }

    @BeforeEach
    public void initTest() {
        device = createEntity(em);
    }

    @Test
    @Transactional
    void createDevice() throws Exception {
        int databaseSizeBeforeCreate = deviceRepository.findAll().size();
        // Create the Device
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isCreated());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate + 1);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testDevice.getModelNumber()).isEqualTo(DEFAULT_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void createDeviceWithExistingId() throws Exception {
        // Create the Device with an existing ID
        device.setId(1L);

        int databaseSizeBeforeCreate = deviceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSerialNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setSerialNumber(null);

        // Create the Device, which fails.

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = deviceRepository.findAll().size();
        // set the field null
        device.setModelNumber(null);

        // Create the Device, which fails.

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isBadRequest());

        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDevices() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)));
    }

    @Test
    @Transactional
    void getDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().intValue()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER))
            .andExpect(jsonPath("$.modelNumber").value(DEFAULT_MODEL_NUMBER));
    }

    @Test
    @Transactional
    void getDevicesByIdFiltering() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        Long id = device.getId();

        defaultDeviceShouldBeFound("id.equals=" + id);
        defaultDeviceShouldNotBeFound("id.notEquals=" + id);

        defaultDeviceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeviceShouldNotBeFound("id.greaterThan=" + id);

        defaultDeviceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeviceShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDevicesBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serialNumber equals to DEFAULT_SERIAL_NUMBER
        defaultDeviceShouldBeFound("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the deviceList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultDeviceShouldNotBeFound("serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDevicesBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serialNumber in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultDeviceShouldBeFound("serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the deviceList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultDeviceShouldNotBeFound("serialNumber.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDevicesBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serialNumber is not null
        defaultDeviceShouldBeFound("serialNumber.specified=true");

        // Get all the deviceList where serialNumber is null
        defaultDeviceShouldNotBeFound("serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesBySerialNumberContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serialNumber contains DEFAULT_SERIAL_NUMBER
        defaultDeviceShouldBeFound("serialNumber.contains=" + DEFAULT_SERIAL_NUMBER);

        // Get all the deviceList where serialNumber contains UPDATED_SERIAL_NUMBER
        defaultDeviceShouldNotBeFound("serialNumber.contains=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDevicesBySerialNumberNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where serialNumber does not contain DEFAULT_SERIAL_NUMBER
        defaultDeviceShouldNotBeFound("serialNumber.doesNotContain=" + DEFAULT_SERIAL_NUMBER);

        // Get all the deviceList where serialNumber does not contain UPDATED_SERIAL_NUMBER
        defaultDeviceShouldBeFound("serialNumber.doesNotContain=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDevicesByModelNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where modelNumber equals to DEFAULT_MODEL_NUMBER
        defaultDeviceShouldBeFound("modelNumber.equals=" + DEFAULT_MODEL_NUMBER);

        // Get all the deviceList where modelNumber equals to UPDATED_MODEL_NUMBER
        defaultDeviceShouldNotBeFound("modelNumber.equals=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDevicesByModelNumberIsInShouldWork() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where modelNumber in DEFAULT_MODEL_NUMBER or UPDATED_MODEL_NUMBER
        defaultDeviceShouldBeFound("modelNumber.in=" + DEFAULT_MODEL_NUMBER + "," + UPDATED_MODEL_NUMBER);

        // Get all the deviceList where modelNumber equals to UPDATED_MODEL_NUMBER
        defaultDeviceShouldNotBeFound("modelNumber.in=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDevicesByModelNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where modelNumber is not null
        defaultDeviceShouldBeFound("modelNumber.specified=true");

        // Get all the deviceList where modelNumber is null
        defaultDeviceShouldNotBeFound("modelNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDevicesByModelNumberContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where modelNumber contains DEFAULT_MODEL_NUMBER
        defaultDeviceShouldBeFound("modelNumber.contains=" + DEFAULT_MODEL_NUMBER);

        // Get all the deviceList where modelNumber contains UPDATED_MODEL_NUMBER
        defaultDeviceShouldNotBeFound("modelNumber.contains=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDevicesByModelNumberNotContainsSomething() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        // Get all the deviceList where modelNumber does not contain DEFAULT_MODEL_NUMBER
        defaultDeviceShouldNotBeFound("modelNumber.doesNotContain=" + DEFAULT_MODEL_NUMBER);

        // Get all the deviceList where modelNumber does not contain UPDATED_MODEL_NUMBER
        defaultDeviceShouldBeFound("modelNumber.doesNotContain=" + UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDevicesByTraderIsEqualToSomething() throws Exception {
        Trader trader;
        if (TestUtil.findAll(em, Trader.class).isEmpty()) {
            deviceRepository.saveAndFlush(device);
            trader = TraderResourceIT.createEntity(em);
        } else {
            trader = TestUtil.findAll(em, Trader.class).get(0);
        }
        em.persist(trader);
        em.flush();
        device.setTrader(trader);
        deviceRepository.saveAndFlush(device);
        Long traderId = trader.getId();

        // Get all the deviceList where trader equals to traderId
        defaultDeviceShouldBeFound("traderId.equals=" + traderId);

        // Get all the deviceList where trader equals to (traderId + 1)
        defaultDeviceShouldNotBeFound("traderId.equals=" + (traderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeviceShouldBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER)))
            .andExpect(jsonPath("$.[*].modelNumber").value(hasItem(DEFAULT_MODEL_NUMBER)));

        // Check, that the count call also returns 1
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeviceShouldNotBeFound(String filter) throws Exception {
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).get();
        // Disconnect from session so that the updates on updatedDevice are not directly saved in db
        em.detach(updatedDevice);
        updatedDevice.serialNumber(UPDATED_SERIAL_NUMBER).modelNumber(UPDATED_MODEL_NUMBER);

        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDevice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testDevice.getModelNumber()).isEqualTo(UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, device.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(device))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(device))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
        assertThat(testDevice.getModelNumber()).isEqualTo(DEFAULT_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice.serialNumber(UPDATED_SERIAL_NUMBER).modelNumber(UPDATED_MODEL_NUMBER);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
        Device testDevice = deviceList.get(deviceList.size() - 1);
        assertThat(testDevice.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
        assertThat(testDevice.getModelNumber()).isEqualTo(UPDATED_MODEL_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, device.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(device))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(device))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDevice() throws Exception {
        int databaseSizeBeforeUpdate = deviceRepository.findAll().size();
        device.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(device)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDevice() throws Exception {
        // Initialize the database
        deviceRepository.saveAndFlush(device);

        int databaseSizeBeforeDelete = deviceRepository.findAll().size();

        // Delete the device
        restDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, device.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Device> deviceList = deviceRepository.findAll();
        assertThat(deviceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

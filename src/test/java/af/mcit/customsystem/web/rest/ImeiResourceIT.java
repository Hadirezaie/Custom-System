package af.mcit.customsystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import af.mcit.customsystem.IntegrationTest;
import af.mcit.customsystem.domain.Device;
import af.mcit.customsystem.domain.Imei;
import af.mcit.customsystem.repository.ImeiRepository;
import af.mcit.customsystem.service.criteria.ImeiCriteria;
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
 * Integration tests for the {@link ImeiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImeiResourceIT {

    private static final Long DEFAULT_IMEI_NUMBER = 1L;
    private static final Long UPDATED_IMEI_NUMBER = 2L;
    private static final Long SMALLER_IMEI_NUMBER = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/imeis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImeiRepository imeiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImeiMockMvc;

    private Imei imei;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Imei createEntity(EntityManager em) {
        Imei imei = new Imei().imeiNumber(DEFAULT_IMEI_NUMBER);
        return imei;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Imei createUpdatedEntity(EntityManager em) {
        Imei imei = new Imei().imeiNumber(UPDATED_IMEI_NUMBER);
        return imei;
    }

    @BeforeEach
    public void initTest() {
        imei = createEntity(em);
    }

    @Test
    @Transactional
    void createImei() throws Exception {
        int databaseSizeBeforeCreate = imeiRepository.findAll().size();
        // Create the Imei
        restImeiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imei)))
            .andExpect(status().isCreated());

        // Validate the Imei in the database
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeCreate + 1);
        Imei testImei = imeiList.get(imeiList.size() - 1);
        assertThat(testImei.getImeiNumber()).isEqualTo(DEFAULT_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void createImeiWithExistingId() throws Exception {
        // Create the Imei with an existing ID
        imei.setId(1L);

        int databaseSizeBeforeCreate = imeiRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImeiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imei)))
            .andExpect(status().isBadRequest());

        // Validate the Imei in the database
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImeis() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get all the imeiList
        restImeiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imei.getId().intValue())))
            .andExpect(jsonPath("$.[*].imeiNumber").value(hasItem(DEFAULT_IMEI_NUMBER.intValue())));
    }

    @Test
    @Transactional
    void getImei() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get the imei
        restImeiMockMvc
            .perform(get(ENTITY_API_URL_ID, imei.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imei.getId().intValue()))
            .andExpect(jsonPath("$.imeiNumber").value(DEFAULT_IMEI_NUMBER.intValue()));
    }

    @Test
    @Transactional
    void getImeisByIdFiltering() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        Long id = imei.getId();

        defaultImeiShouldBeFound("id.equals=" + id);
        defaultImeiShouldNotBeFound("id.notEquals=" + id);

        defaultImeiShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultImeiShouldNotBeFound("id.greaterThan=" + id);

        defaultImeiShouldBeFound("id.lessThanOrEqual=" + id);
        defaultImeiShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImeisByImeiNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get all the imeiList where imeiNumber equals to DEFAULT_IMEI_NUMBER
        defaultImeiShouldBeFound("imeiNumber.equals=" + DEFAULT_IMEI_NUMBER);

        // Get all the imeiList where imeiNumber equals to UPDATED_IMEI_NUMBER
        defaultImeiShouldNotBeFound("imeiNumber.equals=" + UPDATED_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllImeisByImeiNumberIsInShouldWork() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get all the imeiList where imeiNumber in DEFAULT_IMEI_NUMBER or UPDATED_IMEI_NUMBER
        defaultImeiShouldBeFound("imeiNumber.in=" + DEFAULT_IMEI_NUMBER + "," + UPDATED_IMEI_NUMBER);

        // Get all the imeiList where imeiNumber equals to UPDATED_IMEI_NUMBER
        defaultImeiShouldNotBeFound("imeiNumber.in=" + UPDATED_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllImeisByImeiNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get all the imeiList where imeiNumber is not null
        defaultImeiShouldBeFound("imeiNumber.specified=true");

        // Get all the imeiList where imeiNumber is null
        defaultImeiShouldNotBeFound("imeiNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllImeisByImeiNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get all the imeiList where imeiNumber is greater than or equal to DEFAULT_IMEI_NUMBER
        defaultImeiShouldBeFound("imeiNumber.greaterThanOrEqual=" + DEFAULT_IMEI_NUMBER);

        // Get all the imeiList where imeiNumber is greater than or equal to UPDATED_IMEI_NUMBER
        defaultImeiShouldNotBeFound("imeiNumber.greaterThanOrEqual=" + UPDATED_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllImeisByImeiNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get all the imeiList where imeiNumber is less than or equal to DEFAULT_IMEI_NUMBER
        defaultImeiShouldBeFound("imeiNumber.lessThanOrEqual=" + DEFAULT_IMEI_NUMBER);

        // Get all the imeiList where imeiNumber is less than or equal to SMALLER_IMEI_NUMBER
        defaultImeiShouldNotBeFound("imeiNumber.lessThanOrEqual=" + SMALLER_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllImeisByImeiNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get all the imeiList where imeiNumber is less than DEFAULT_IMEI_NUMBER
        defaultImeiShouldNotBeFound("imeiNumber.lessThan=" + DEFAULT_IMEI_NUMBER);

        // Get all the imeiList where imeiNumber is less than UPDATED_IMEI_NUMBER
        defaultImeiShouldBeFound("imeiNumber.lessThan=" + UPDATED_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllImeisByImeiNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        // Get all the imeiList where imeiNumber is greater than DEFAULT_IMEI_NUMBER
        defaultImeiShouldNotBeFound("imeiNumber.greaterThan=" + DEFAULT_IMEI_NUMBER);

        // Get all the imeiList where imeiNumber is greater than SMALLER_IMEI_NUMBER
        defaultImeiShouldBeFound("imeiNumber.greaterThan=" + SMALLER_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void getAllImeisByDeviceIsEqualToSomething() throws Exception {
        Device device;
        if (TestUtil.findAll(em, Device.class).isEmpty()) {
            imeiRepository.saveAndFlush(imei);
            device = DeviceResourceIT.createEntity(em);
        } else {
            device = TestUtil.findAll(em, Device.class).get(0);
        }
        em.persist(device);
        em.flush();
        imei.setDevice(device);
        imeiRepository.saveAndFlush(imei);
        Long deviceId = device.getId();

        // Get all the imeiList where device equals to deviceId
        defaultImeiShouldBeFound("deviceId.equals=" + deviceId);

        // Get all the imeiList where device equals to (deviceId + 1)
        defaultImeiShouldNotBeFound("deviceId.equals=" + (deviceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImeiShouldBeFound(String filter) throws Exception {
        restImeiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imei.getId().intValue())))
            .andExpect(jsonPath("$.[*].imeiNumber").value(hasItem(DEFAULT_IMEI_NUMBER.intValue())));

        // Check, that the count call also returns 1
        restImeiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImeiShouldNotBeFound(String filter) throws Exception {
        restImeiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImeiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImei() throws Exception {
        // Get the imei
        restImeiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImei() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        int databaseSizeBeforeUpdate = imeiRepository.findAll().size();

        // Update the imei
        Imei updatedImei = imeiRepository.findById(imei.getId()).get();
        // Disconnect from session so that the updates on updatedImei are not directly saved in db
        em.detach(updatedImei);
        updatedImei.imeiNumber(UPDATED_IMEI_NUMBER);

        restImeiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImei.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedImei))
            )
            .andExpect(status().isOk());

        // Validate the Imei in the database
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeUpdate);
        Imei testImei = imeiList.get(imeiList.size() - 1);
        assertThat(testImei.getImeiNumber()).isEqualTo(UPDATED_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingImei() throws Exception {
        int databaseSizeBeforeUpdate = imeiRepository.findAll().size();
        imei.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImeiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imei.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imei))
            )
            .andExpect(status().isBadRequest());

        // Validate the Imei in the database
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImei() throws Exception {
        int databaseSizeBeforeUpdate = imeiRepository.findAll().size();
        imei.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImeiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imei))
            )
            .andExpect(status().isBadRequest());

        // Validate the Imei in the database
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImei() throws Exception {
        int databaseSizeBeforeUpdate = imeiRepository.findAll().size();
        imei.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImeiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imei)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Imei in the database
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImeiWithPatch() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        int databaseSizeBeforeUpdate = imeiRepository.findAll().size();

        // Update the imei using partial update
        Imei partialUpdatedImei = new Imei();
        partialUpdatedImei.setId(imei.getId());

        restImeiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImei.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImei))
            )
            .andExpect(status().isOk());

        // Validate the Imei in the database
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeUpdate);
        Imei testImei = imeiList.get(imeiList.size() - 1);
        assertThat(testImei.getImeiNumber()).isEqualTo(DEFAULT_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateImeiWithPatch() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        int databaseSizeBeforeUpdate = imeiRepository.findAll().size();

        // Update the imei using partial update
        Imei partialUpdatedImei = new Imei();
        partialUpdatedImei.setId(imei.getId());

        partialUpdatedImei.imeiNumber(UPDATED_IMEI_NUMBER);

        restImeiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImei.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImei))
            )
            .andExpect(status().isOk());

        // Validate the Imei in the database
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeUpdate);
        Imei testImei = imeiList.get(imeiList.size() - 1);
        assertThat(testImei.getImeiNumber()).isEqualTo(UPDATED_IMEI_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingImei() throws Exception {
        int databaseSizeBeforeUpdate = imeiRepository.findAll().size();
        imei.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImeiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imei.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imei))
            )
            .andExpect(status().isBadRequest());

        // Validate the Imei in the database
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImei() throws Exception {
        int databaseSizeBeforeUpdate = imeiRepository.findAll().size();
        imei.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImeiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imei))
            )
            .andExpect(status().isBadRequest());

        // Validate the Imei in the database
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImei() throws Exception {
        int databaseSizeBeforeUpdate = imeiRepository.findAll().size();
        imei.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImeiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imei)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Imei in the database
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImei() throws Exception {
        // Initialize the database
        imeiRepository.saveAndFlush(imei);

        int databaseSizeBeforeDelete = imeiRepository.findAll().size();

        // Delete the imei
        restImeiMockMvc
            .perform(delete(ENTITY_API_URL_ID, imei.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Imei> imeiList = imeiRepository.findAll();
        assertThat(imeiList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

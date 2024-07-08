package af.mcit.customsystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import af.mcit.customsystem.IntegrationTest;
import af.mcit.customsystem.domain.CustomCharges;
import af.mcit.customsystem.repository.CustomChargesRepository;
import af.mcit.customsystem.service.criteria.CustomChargesCriteria;
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
 * Integration tests for the {@link CustomChargesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CustomChargesResourceIT {

    private static final String DEFAULT_DEVICE_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_MODEL = "BBBBBBBBBB";

    private static final Long DEFAULT_CUSTOM_FEE = 1L;
    private static final Long UPDATED_CUSTOM_FEE = 2L;
    private static final Long SMALLER_CUSTOM_FEE = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/custom-charges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomChargesRepository customChargesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCustomChargesMockMvc;

    private CustomCharges customCharges;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomCharges createEntity(EntityManager em) {
        CustomCharges customCharges = new CustomCharges().deviceModel(DEFAULT_DEVICE_MODEL).customFee(DEFAULT_CUSTOM_FEE);
        return customCharges;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomCharges createUpdatedEntity(EntityManager em) {
        CustomCharges customCharges = new CustomCharges().deviceModel(UPDATED_DEVICE_MODEL).customFee(UPDATED_CUSTOM_FEE);
        return customCharges;
    }

    @BeforeEach
    public void initTest() {
        customCharges = createEntity(em);
    }

    @Test
    @Transactional
    void createCustomCharges() throws Exception {
        int databaseSizeBeforeCreate = customChargesRepository.findAll().size();
        // Create the CustomCharges
        restCustomChargesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customCharges)))
            .andExpect(status().isCreated());

        // Validate the CustomCharges in the database
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeCreate + 1);
        CustomCharges testCustomCharges = customChargesList.get(customChargesList.size() - 1);
        assertThat(testCustomCharges.getDeviceModel()).isEqualTo(DEFAULT_DEVICE_MODEL);
        assertThat(testCustomCharges.getCustomFee()).isEqualTo(DEFAULT_CUSTOM_FEE);
    }

    @Test
    @Transactional
    void createCustomChargesWithExistingId() throws Exception {
        // Create the CustomCharges with an existing ID
        customCharges.setId(1L);

        int databaseSizeBeforeCreate = customChargesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomChargesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customCharges)))
            .andExpect(status().isBadRequest());

        // Validate the CustomCharges in the database
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCustomCharges() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList
        restCustomChargesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customCharges.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceModel").value(hasItem(DEFAULT_DEVICE_MODEL)))
            .andExpect(jsonPath("$.[*].customFee").value(hasItem(DEFAULT_CUSTOM_FEE.intValue())));
    }

    @Test
    @Transactional
    void getCustomCharges() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get the customCharges
        restCustomChargesMockMvc
            .perform(get(ENTITY_API_URL_ID, customCharges.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(customCharges.getId().intValue()))
            .andExpect(jsonPath("$.deviceModel").value(DEFAULT_DEVICE_MODEL))
            .andExpect(jsonPath("$.customFee").value(DEFAULT_CUSTOM_FEE.intValue()));
    }

    @Test
    @Transactional
    void getCustomChargesByIdFiltering() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        Long id = customCharges.getId();

        defaultCustomChargesShouldBeFound("id.equals=" + id);
        defaultCustomChargesShouldNotBeFound("id.notEquals=" + id);

        defaultCustomChargesShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCustomChargesShouldNotBeFound("id.greaterThan=" + id);

        defaultCustomChargesShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCustomChargesShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCustomChargesByDeviceModelIsEqualToSomething() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where deviceModel equals to DEFAULT_DEVICE_MODEL
        defaultCustomChargesShouldBeFound("deviceModel.equals=" + DEFAULT_DEVICE_MODEL);

        // Get all the customChargesList where deviceModel equals to UPDATED_DEVICE_MODEL
        defaultCustomChargesShouldNotBeFound("deviceModel.equals=" + UPDATED_DEVICE_MODEL);
    }

    @Test
    @Transactional
    void getAllCustomChargesByDeviceModelIsInShouldWork() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where deviceModel in DEFAULT_DEVICE_MODEL or UPDATED_DEVICE_MODEL
        defaultCustomChargesShouldBeFound("deviceModel.in=" + DEFAULT_DEVICE_MODEL + "," + UPDATED_DEVICE_MODEL);

        // Get all the customChargesList where deviceModel equals to UPDATED_DEVICE_MODEL
        defaultCustomChargesShouldNotBeFound("deviceModel.in=" + UPDATED_DEVICE_MODEL);
    }

    @Test
    @Transactional
    void getAllCustomChargesByDeviceModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where deviceModel is not null
        defaultCustomChargesShouldBeFound("deviceModel.specified=true");

        // Get all the customChargesList where deviceModel is null
        defaultCustomChargesShouldNotBeFound("deviceModel.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomChargesByDeviceModelContainsSomething() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where deviceModel contains DEFAULT_DEVICE_MODEL
        defaultCustomChargesShouldBeFound("deviceModel.contains=" + DEFAULT_DEVICE_MODEL);

        // Get all the customChargesList where deviceModel contains UPDATED_DEVICE_MODEL
        defaultCustomChargesShouldNotBeFound("deviceModel.contains=" + UPDATED_DEVICE_MODEL);
    }

    @Test
    @Transactional
    void getAllCustomChargesByDeviceModelNotContainsSomething() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where deviceModel does not contain DEFAULT_DEVICE_MODEL
        defaultCustomChargesShouldNotBeFound("deviceModel.doesNotContain=" + DEFAULT_DEVICE_MODEL);

        // Get all the customChargesList where deviceModel does not contain UPDATED_DEVICE_MODEL
        defaultCustomChargesShouldBeFound("deviceModel.doesNotContain=" + UPDATED_DEVICE_MODEL);
    }

    @Test
    @Transactional
    void getAllCustomChargesByCustomFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where customFee equals to DEFAULT_CUSTOM_FEE
        defaultCustomChargesShouldBeFound("customFee.equals=" + DEFAULT_CUSTOM_FEE);

        // Get all the customChargesList where customFee equals to UPDATED_CUSTOM_FEE
        defaultCustomChargesShouldNotBeFound("customFee.equals=" + UPDATED_CUSTOM_FEE);
    }

    @Test
    @Transactional
    void getAllCustomChargesByCustomFeeIsInShouldWork() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where customFee in DEFAULT_CUSTOM_FEE or UPDATED_CUSTOM_FEE
        defaultCustomChargesShouldBeFound("customFee.in=" + DEFAULT_CUSTOM_FEE + "," + UPDATED_CUSTOM_FEE);

        // Get all the customChargesList where customFee equals to UPDATED_CUSTOM_FEE
        defaultCustomChargesShouldNotBeFound("customFee.in=" + UPDATED_CUSTOM_FEE);
    }

    @Test
    @Transactional
    void getAllCustomChargesByCustomFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where customFee is not null
        defaultCustomChargesShouldBeFound("customFee.specified=true");

        // Get all the customChargesList where customFee is null
        defaultCustomChargesShouldNotBeFound("customFee.specified=false");
    }

    @Test
    @Transactional
    void getAllCustomChargesByCustomFeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where customFee is greater than or equal to DEFAULT_CUSTOM_FEE
        defaultCustomChargesShouldBeFound("customFee.greaterThanOrEqual=" + DEFAULT_CUSTOM_FEE);

        // Get all the customChargesList where customFee is greater than or equal to UPDATED_CUSTOM_FEE
        defaultCustomChargesShouldNotBeFound("customFee.greaterThanOrEqual=" + UPDATED_CUSTOM_FEE);
    }

    @Test
    @Transactional
    void getAllCustomChargesByCustomFeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where customFee is less than or equal to DEFAULT_CUSTOM_FEE
        defaultCustomChargesShouldBeFound("customFee.lessThanOrEqual=" + DEFAULT_CUSTOM_FEE);

        // Get all the customChargesList where customFee is less than or equal to SMALLER_CUSTOM_FEE
        defaultCustomChargesShouldNotBeFound("customFee.lessThanOrEqual=" + SMALLER_CUSTOM_FEE);
    }

    @Test
    @Transactional
    void getAllCustomChargesByCustomFeeIsLessThanSomething() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where customFee is less than DEFAULT_CUSTOM_FEE
        defaultCustomChargesShouldNotBeFound("customFee.lessThan=" + DEFAULT_CUSTOM_FEE);

        // Get all the customChargesList where customFee is less than UPDATED_CUSTOM_FEE
        defaultCustomChargesShouldBeFound("customFee.lessThan=" + UPDATED_CUSTOM_FEE);
    }

    @Test
    @Transactional
    void getAllCustomChargesByCustomFeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        // Get all the customChargesList where customFee is greater than DEFAULT_CUSTOM_FEE
        defaultCustomChargesShouldNotBeFound("customFee.greaterThan=" + DEFAULT_CUSTOM_FEE);

        // Get all the customChargesList where customFee is greater than SMALLER_CUSTOM_FEE
        defaultCustomChargesShouldBeFound("customFee.greaterThan=" + SMALLER_CUSTOM_FEE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCustomChargesShouldBeFound(String filter) throws Exception {
        restCustomChargesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customCharges.getId().intValue())))
            .andExpect(jsonPath("$.[*].deviceModel").value(hasItem(DEFAULT_DEVICE_MODEL)))
            .andExpect(jsonPath("$.[*].customFee").value(hasItem(DEFAULT_CUSTOM_FEE.intValue())));

        // Check, that the count call also returns 1
        restCustomChargesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCustomChargesShouldNotBeFound(String filter) throws Exception {
        restCustomChargesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCustomChargesMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCustomCharges() throws Exception {
        // Get the customCharges
        restCustomChargesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCustomCharges() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        int databaseSizeBeforeUpdate = customChargesRepository.findAll().size();

        // Update the customCharges
        CustomCharges updatedCustomCharges = customChargesRepository.findById(customCharges.getId()).get();
        // Disconnect from session so that the updates on updatedCustomCharges are not directly saved in db
        em.detach(updatedCustomCharges);
        updatedCustomCharges.deviceModel(UPDATED_DEVICE_MODEL).customFee(UPDATED_CUSTOM_FEE);

        restCustomChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCustomCharges.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCustomCharges))
            )
            .andExpect(status().isOk());

        // Validate the CustomCharges in the database
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeUpdate);
        CustomCharges testCustomCharges = customChargesList.get(customChargesList.size() - 1);
        assertThat(testCustomCharges.getDeviceModel()).isEqualTo(UPDATED_DEVICE_MODEL);
        assertThat(testCustomCharges.getCustomFee()).isEqualTo(UPDATED_CUSTOM_FEE);
    }

    @Test
    @Transactional
    void putNonExistingCustomCharges() throws Exception {
        int databaseSizeBeforeUpdate = customChargesRepository.findAll().size();
        customCharges.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, customCharges.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customCharges))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomCharges in the database
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCustomCharges() throws Exception {
        int databaseSizeBeforeUpdate = customChargesRepository.findAll().size();
        customCharges.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomChargesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(customCharges))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomCharges in the database
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCustomCharges() throws Exception {
        int databaseSizeBeforeUpdate = customChargesRepository.findAll().size();
        customCharges.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomChargesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(customCharges)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomCharges in the database
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCustomChargesWithPatch() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        int databaseSizeBeforeUpdate = customChargesRepository.findAll().size();

        // Update the customCharges using partial update
        CustomCharges partialUpdatedCustomCharges = new CustomCharges();
        partialUpdatedCustomCharges.setId(customCharges.getId());

        partialUpdatedCustomCharges.customFee(UPDATED_CUSTOM_FEE);

        restCustomChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomCharges.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomCharges))
            )
            .andExpect(status().isOk());

        // Validate the CustomCharges in the database
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeUpdate);
        CustomCharges testCustomCharges = customChargesList.get(customChargesList.size() - 1);
        assertThat(testCustomCharges.getDeviceModel()).isEqualTo(DEFAULT_DEVICE_MODEL);
        assertThat(testCustomCharges.getCustomFee()).isEqualTo(UPDATED_CUSTOM_FEE);
    }

    @Test
    @Transactional
    void fullUpdateCustomChargesWithPatch() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        int databaseSizeBeforeUpdate = customChargesRepository.findAll().size();

        // Update the customCharges using partial update
        CustomCharges partialUpdatedCustomCharges = new CustomCharges();
        partialUpdatedCustomCharges.setId(customCharges.getId());

        partialUpdatedCustomCharges.deviceModel(UPDATED_DEVICE_MODEL).customFee(UPDATED_CUSTOM_FEE);

        restCustomChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCustomCharges.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomCharges))
            )
            .andExpect(status().isOk());

        // Validate the CustomCharges in the database
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeUpdate);
        CustomCharges testCustomCharges = customChargesList.get(customChargesList.size() - 1);
        assertThat(testCustomCharges.getDeviceModel()).isEqualTo(UPDATED_DEVICE_MODEL);
        assertThat(testCustomCharges.getCustomFee()).isEqualTo(UPDATED_CUSTOM_FEE);
    }

    @Test
    @Transactional
    void patchNonExistingCustomCharges() throws Exception {
        int databaseSizeBeforeUpdate = customChargesRepository.findAll().size();
        customCharges.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, customCharges.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customCharges))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomCharges in the database
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCustomCharges() throws Exception {
        int databaseSizeBeforeUpdate = customChargesRepository.findAll().size();
        customCharges.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomChargesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(customCharges))
            )
            .andExpect(status().isBadRequest());

        // Validate the CustomCharges in the database
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCustomCharges() throws Exception {
        int databaseSizeBeforeUpdate = customChargesRepository.findAll().size();
        customCharges.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCustomChargesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(customCharges))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CustomCharges in the database
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCustomCharges() throws Exception {
        // Initialize the database
        customChargesRepository.saveAndFlush(customCharges);

        int databaseSizeBeforeDelete = customChargesRepository.findAll().size();

        // Delete the customCharges
        restCustomChargesMockMvc
            .perform(delete(ENTITY_API_URL_ID, customCharges.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CustomCharges> customChargesList = customChargesRepository.findAll();
        assertThat(customChargesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

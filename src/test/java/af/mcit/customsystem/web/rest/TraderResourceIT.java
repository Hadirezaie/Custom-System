package af.mcit.customsystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import af.mcit.customsystem.IntegrationTest;
import af.mcit.customsystem.domain.Trader;
import af.mcit.customsystem.repository.TraderRepository;
import af.mcit.customsystem.service.criteria.TraderCriteria;
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
 * Integration tests for the {@link TraderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TraderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_T_IN = "AAAAAAAAAA";
    private static final String UPDATED_T_IN = "BBBBBBBBBB";

    private static final String DEFAULT_LICENSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/traders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TraderRepository traderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTraderMockMvc;

    private Trader trader;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trader createEntity(EntityManager em) {
        Trader trader = new Trader()
            .name(DEFAULT_NAME)
            .nidNumber(DEFAULT_NID_NUMBER)
            .tIN(DEFAULT_T_IN)
            .licenseNumber(DEFAULT_LICENSE_NUMBER);
        return trader;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trader createUpdatedEntity(EntityManager em) {
        Trader trader = new Trader()
            .name(UPDATED_NAME)
            .nidNumber(UPDATED_NID_NUMBER)
            .tIN(UPDATED_T_IN)
            .licenseNumber(UPDATED_LICENSE_NUMBER);
        return trader;
    }

    @BeforeEach
    public void initTest() {
        trader = createEntity(em);
    }

    @Test
    @Transactional
    void createTrader() throws Exception {
        int databaseSizeBeforeCreate = traderRepository.findAll().size();
        // Create the Trader
        restTraderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trader)))
            .andExpect(status().isCreated());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeCreate + 1);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrader.getNidNumber()).isEqualTo(DEFAULT_NID_NUMBER);
        assertThat(testTrader.gettIN()).isEqualTo(DEFAULT_T_IN);
        assertThat(testTrader.getLicenseNumber()).isEqualTo(DEFAULT_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    void createTraderWithExistingId() throws Exception {
        // Create the Trader with an existing ID
        trader.setId(1L);

        int databaseSizeBeforeCreate = traderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTraderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trader)))
            .andExpect(status().isBadRequest());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTraders() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList
        restTraderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trader.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nidNumber").value(hasItem(DEFAULT_NID_NUMBER)))
            .andExpect(jsonPath("$.[*].tIN").value(hasItem(DEFAULT_T_IN)))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER)));
    }

    @Test
    @Transactional
    void getTrader() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get the trader
        restTraderMockMvc
            .perform(get(ENTITY_API_URL_ID, trader.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trader.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.nidNumber").value(DEFAULT_NID_NUMBER))
            .andExpect(jsonPath("$.tIN").value(DEFAULT_T_IN))
            .andExpect(jsonPath("$.licenseNumber").value(DEFAULT_LICENSE_NUMBER));
    }

    @Test
    @Transactional
    void getTradersByIdFiltering() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        Long id = trader.getId();

        defaultTraderShouldBeFound("id.equals=" + id);
        defaultTraderShouldNotBeFound("id.notEquals=" + id);

        defaultTraderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTraderShouldNotBeFound("id.greaterThan=" + id);

        defaultTraderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTraderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTradersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where name equals to DEFAULT_NAME
        defaultTraderShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the traderList where name equals to UPDATED_NAME
        defaultTraderShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTradersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTraderShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the traderList where name equals to UPDATED_NAME
        defaultTraderShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTradersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where name is not null
        defaultTraderShouldBeFound("name.specified=true");

        // Get all the traderList where name is null
        defaultTraderShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTradersByNameContainsSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where name contains DEFAULT_NAME
        defaultTraderShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the traderList where name contains UPDATED_NAME
        defaultTraderShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTradersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where name does not contain DEFAULT_NAME
        defaultTraderShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the traderList where name does not contain UPDATED_NAME
        defaultTraderShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTradersByNidNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where nidNumber equals to DEFAULT_NID_NUMBER
        defaultTraderShouldBeFound("nidNumber.equals=" + DEFAULT_NID_NUMBER);

        // Get all the traderList where nidNumber equals to UPDATED_NID_NUMBER
        defaultTraderShouldNotBeFound("nidNumber.equals=" + UPDATED_NID_NUMBER);
    }

    @Test
    @Transactional
    void getAllTradersByNidNumberIsInShouldWork() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where nidNumber in DEFAULT_NID_NUMBER or UPDATED_NID_NUMBER
        defaultTraderShouldBeFound("nidNumber.in=" + DEFAULT_NID_NUMBER + "," + UPDATED_NID_NUMBER);

        // Get all the traderList where nidNumber equals to UPDATED_NID_NUMBER
        defaultTraderShouldNotBeFound("nidNumber.in=" + UPDATED_NID_NUMBER);
    }

    @Test
    @Transactional
    void getAllTradersByNidNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where nidNumber is not null
        defaultTraderShouldBeFound("nidNumber.specified=true");

        // Get all the traderList where nidNumber is null
        defaultTraderShouldNotBeFound("nidNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTradersByNidNumberContainsSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where nidNumber contains DEFAULT_NID_NUMBER
        defaultTraderShouldBeFound("nidNumber.contains=" + DEFAULT_NID_NUMBER);

        // Get all the traderList where nidNumber contains UPDATED_NID_NUMBER
        defaultTraderShouldNotBeFound("nidNumber.contains=" + UPDATED_NID_NUMBER);
    }

    @Test
    @Transactional
    void getAllTradersByNidNumberNotContainsSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where nidNumber does not contain DEFAULT_NID_NUMBER
        defaultTraderShouldNotBeFound("nidNumber.doesNotContain=" + DEFAULT_NID_NUMBER);

        // Get all the traderList where nidNumber does not contain UPDATED_NID_NUMBER
        defaultTraderShouldBeFound("nidNumber.doesNotContain=" + UPDATED_NID_NUMBER);
    }

    @Test
    @Transactional
    void getAllTradersBytINIsEqualToSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where tIN equals to DEFAULT_T_IN
        defaultTraderShouldBeFound("tIN.equals=" + DEFAULT_T_IN);

        // Get all the traderList where tIN equals to UPDATED_T_IN
        defaultTraderShouldNotBeFound("tIN.equals=" + UPDATED_T_IN);
    }

    @Test
    @Transactional
    void getAllTradersBytINIsInShouldWork() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where tIN in DEFAULT_T_IN or UPDATED_T_IN
        defaultTraderShouldBeFound("tIN.in=" + DEFAULT_T_IN + "," + UPDATED_T_IN);

        // Get all the traderList where tIN equals to UPDATED_T_IN
        defaultTraderShouldNotBeFound("tIN.in=" + UPDATED_T_IN);
    }

    @Test
    @Transactional
    void getAllTradersBytINIsNullOrNotNull() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where tIN is not null
        defaultTraderShouldBeFound("tIN.specified=true");

        // Get all the traderList where tIN is null
        defaultTraderShouldNotBeFound("tIN.specified=false");
    }

    @Test
    @Transactional
    void getAllTradersBytINContainsSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where tIN contains DEFAULT_T_IN
        defaultTraderShouldBeFound("tIN.contains=" + DEFAULT_T_IN);

        // Get all the traderList where tIN contains UPDATED_T_IN
        defaultTraderShouldNotBeFound("tIN.contains=" + UPDATED_T_IN);
    }

    @Test
    @Transactional
    void getAllTradersBytINNotContainsSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where tIN does not contain DEFAULT_T_IN
        defaultTraderShouldNotBeFound("tIN.doesNotContain=" + DEFAULT_T_IN);

        // Get all the traderList where tIN does not contain UPDATED_T_IN
        defaultTraderShouldBeFound("tIN.doesNotContain=" + UPDATED_T_IN);
    }

    @Test
    @Transactional
    void getAllTradersByLicenseNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where licenseNumber equals to DEFAULT_LICENSE_NUMBER
        defaultTraderShouldBeFound("licenseNumber.equals=" + DEFAULT_LICENSE_NUMBER);

        // Get all the traderList where licenseNumber equals to UPDATED_LICENSE_NUMBER
        defaultTraderShouldNotBeFound("licenseNumber.equals=" + UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTradersByLicenseNumberIsInShouldWork() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where licenseNumber in DEFAULT_LICENSE_NUMBER or UPDATED_LICENSE_NUMBER
        defaultTraderShouldBeFound("licenseNumber.in=" + DEFAULT_LICENSE_NUMBER + "," + UPDATED_LICENSE_NUMBER);

        // Get all the traderList where licenseNumber equals to UPDATED_LICENSE_NUMBER
        defaultTraderShouldNotBeFound("licenseNumber.in=" + UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTradersByLicenseNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where licenseNumber is not null
        defaultTraderShouldBeFound("licenseNumber.specified=true");

        // Get all the traderList where licenseNumber is null
        defaultTraderShouldNotBeFound("licenseNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllTradersByLicenseNumberContainsSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where licenseNumber contains DEFAULT_LICENSE_NUMBER
        defaultTraderShouldBeFound("licenseNumber.contains=" + DEFAULT_LICENSE_NUMBER);

        // Get all the traderList where licenseNumber contains UPDATED_LICENSE_NUMBER
        defaultTraderShouldNotBeFound("licenseNumber.contains=" + UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    void getAllTradersByLicenseNumberNotContainsSomething() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        // Get all the traderList where licenseNumber does not contain DEFAULT_LICENSE_NUMBER
        defaultTraderShouldNotBeFound("licenseNumber.doesNotContain=" + DEFAULT_LICENSE_NUMBER);

        // Get all the traderList where licenseNumber does not contain UPDATED_LICENSE_NUMBER
        defaultTraderShouldBeFound("licenseNumber.doesNotContain=" + UPDATED_LICENSE_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTraderShouldBeFound(String filter) throws Exception {
        restTraderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trader.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].nidNumber").value(hasItem(DEFAULT_NID_NUMBER)))
            .andExpect(jsonPath("$.[*].tIN").value(hasItem(DEFAULT_T_IN)))
            .andExpect(jsonPath("$.[*].licenseNumber").value(hasItem(DEFAULT_LICENSE_NUMBER)));

        // Check, that the count call also returns 1
        restTraderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTraderShouldNotBeFound(String filter) throws Exception {
        restTraderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTraderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrader() throws Exception {
        // Get the trader
        restTraderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrader() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        int databaseSizeBeforeUpdate = traderRepository.findAll().size();

        // Update the trader
        Trader updatedTrader = traderRepository.findById(trader.getId()).get();
        // Disconnect from session so that the updates on updatedTrader are not directly saved in db
        em.detach(updatedTrader);
        updatedTrader.name(UPDATED_NAME).nidNumber(UPDATED_NID_NUMBER).tIN(UPDATED_T_IN).licenseNumber(UPDATED_LICENSE_NUMBER);

        restTraderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrader))
            )
            .andExpect(status().isOk());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrader.getNidNumber()).isEqualTo(UPDATED_NID_NUMBER);
        assertThat(testTrader.gettIN()).isEqualTo(UPDATED_T_IN);
        assertThat(testTrader.getLicenseNumber()).isEqualTo(UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingTrader() throws Exception {
        int databaseSizeBeforeUpdate = traderRepository.findAll().size();
        trader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTraderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trader.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trader))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrader() throws Exception {
        int databaseSizeBeforeUpdate = traderRepository.findAll().size();
        trader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTraderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trader))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrader() throws Exception {
        int databaseSizeBeforeUpdate = traderRepository.findAll().size();
        trader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTraderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTraderWithPatch() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        int databaseSizeBeforeUpdate = traderRepository.findAll().size();

        // Update the trader using partial update
        Trader partialUpdatedTrader = new Trader();
        partialUpdatedTrader.setId(trader.getId());

        partialUpdatedTrader.nidNumber(UPDATED_NID_NUMBER).tIN(UPDATED_T_IN).licenseNumber(UPDATED_LICENSE_NUMBER);

        restTraderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrader))
            )
            .andExpect(status().isOk());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTrader.getNidNumber()).isEqualTo(UPDATED_NID_NUMBER);
        assertThat(testTrader.gettIN()).isEqualTo(UPDATED_T_IN);
        assertThat(testTrader.getLicenseNumber()).isEqualTo(UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateTraderWithPatch() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        int databaseSizeBeforeUpdate = traderRepository.findAll().size();

        // Update the trader using partial update
        Trader partialUpdatedTrader = new Trader();
        partialUpdatedTrader.setId(trader.getId());

        partialUpdatedTrader.name(UPDATED_NAME).nidNumber(UPDATED_NID_NUMBER).tIN(UPDATED_T_IN).licenseNumber(UPDATED_LICENSE_NUMBER);

        restTraderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrader))
            )
            .andExpect(status().isOk());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
        Trader testTrader = traderList.get(traderList.size() - 1);
        assertThat(testTrader.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTrader.getNidNumber()).isEqualTo(UPDATED_NID_NUMBER);
        assertThat(testTrader.gettIN()).isEqualTo(UPDATED_T_IN);
        assertThat(testTrader.getLicenseNumber()).isEqualTo(UPDATED_LICENSE_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingTrader() throws Exception {
        int databaseSizeBeforeUpdate = traderRepository.findAll().size();
        trader.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTraderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trader.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trader))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrader() throws Exception {
        int databaseSizeBeforeUpdate = traderRepository.findAll().size();
        trader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTraderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trader))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrader() throws Exception {
        int databaseSizeBeforeUpdate = traderRepository.findAll().size();
        trader.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTraderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trader)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trader in the database
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrader() throws Exception {
        // Initialize the database
        traderRepository.saveAndFlush(trader);

        int databaseSizeBeforeDelete = traderRepository.findAll().size();

        // Delete the trader
        restTraderMockMvc
            .perform(delete(ENTITY_API_URL_ID, trader.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trader> traderList = traderRepository.findAll();
        assertThat(traderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

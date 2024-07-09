package af.mcit.customsystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import af.mcit.customsystem.IntegrationTest;
import af.mcit.customsystem.domain.Device;
import af.mcit.customsystem.domain.Tarif;
import af.mcit.customsystem.repository.TarifRepository;
import af.mcit.customsystem.service.criteria.TarifCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TarifResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TarifResourceIT {

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;
    private static final Long SMALLER_AMOUNT = 1L - 1L;

    private static final LocalDate DEFAULT_PAID_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAID_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PAID_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    private static final Long DEFAULT_NUMBER_OF_DEVICE = 1L;
    private static final Long UPDATED_NUMBER_OF_DEVICE = 2L;
    private static final Long SMALLER_NUMBER_OF_DEVICE = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/tarifs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TarifRepository tarifRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTarifMockMvc;

    private Tarif tarif;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarif createEntity(EntityManager em) {
        Tarif tarif = new Tarif()
            .amount(DEFAULT_AMOUNT)
            .paidDate(DEFAULT_PAID_DATE)
            .paid(DEFAULT_PAID)
            .numberOfDevice(DEFAULT_NUMBER_OF_DEVICE);
        return tarif;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tarif createUpdatedEntity(EntityManager em) {
        Tarif tarif = new Tarif()
            .amount(UPDATED_AMOUNT)
            .paidDate(UPDATED_PAID_DATE)
            .paid(UPDATED_PAID)
            .numberOfDevice(UPDATED_NUMBER_OF_DEVICE);
        return tarif;
    }

    @BeforeEach
    public void initTest() {
        tarif = createEntity(em);
    }

    @Test
    @Transactional
    void createTarif() throws Exception {
        int databaseSizeBeforeCreate = tarifRepository.findAll().size();
        // Create the Tarif
        restTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarif)))
            .andExpect(status().isCreated());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeCreate + 1);
        Tarif testTarif = tarifList.get(tarifList.size() - 1);
        assertThat(testTarif.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testTarif.getPaidDate()).isEqualTo(DEFAULT_PAID_DATE);
        assertThat(testTarif.getPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testTarif.getNumberOfDevice()).isEqualTo(DEFAULT_NUMBER_OF_DEVICE);
    }

    @Test
    @Transactional
    void createTarifWithExistingId() throws Exception {
        // Create the Tarif with an existing ID
        tarif.setId(1L);

        int databaseSizeBeforeCreate = tarifRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTarifMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarif)))
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTarifs() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList
        restTarifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarif.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].paidDate").value(hasItem(DEFAULT_PAID_DATE.toString())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].numberOfDevice").value(hasItem(DEFAULT_NUMBER_OF_DEVICE.intValue())));
    }

    @Test
    @Transactional
    void getTarif() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get the tarif
        restTarifMockMvc
            .perform(get(ENTITY_API_URL_ID, tarif.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tarif.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.paidDate").value(DEFAULT_PAID_DATE.toString()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()))
            .andExpect(jsonPath("$.numberOfDevice").value(DEFAULT_NUMBER_OF_DEVICE.intValue()));
    }

    @Test
    @Transactional
    void getTarifsByIdFiltering() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        Long id = tarif.getId();

        defaultTarifShouldBeFound("id.equals=" + id);
        defaultTarifShouldNotBeFound("id.notEquals=" + id);

        defaultTarifShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTarifShouldNotBeFound("id.greaterThan=" + id);

        defaultTarifShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTarifShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTarifsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where amount equals to DEFAULT_AMOUNT
        defaultTarifShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the tarifList where amount equals to UPDATED_AMOUNT
        defaultTarifShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTarifsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultTarifShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the tarifList where amount equals to UPDATED_AMOUNT
        defaultTarifShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTarifsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where amount is not null
        defaultTarifShouldBeFound("amount.specified=true");

        // Get all the tarifList where amount is null
        defaultTarifShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllTarifsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultTarifShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the tarifList where amount is greater than or equal to UPDATED_AMOUNT
        defaultTarifShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTarifsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where amount is less than or equal to DEFAULT_AMOUNT
        defaultTarifShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the tarifList where amount is less than or equal to SMALLER_AMOUNT
        defaultTarifShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTarifsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where amount is less than DEFAULT_AMOUNT
        defaultTarifShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the tarifList where amount is less than UPDATED_AMOUNT
        defaultTarifShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTarifsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where amount is greater than DEFAULT_AMOUNT
        defaultTarifShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the tarifList where amount is greater than SMALLER_AMOUNT
        defaultTarifShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllTarifsByPaidDateIsEqualToSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where paidDate equals to DEFAULT_PAID_DATE
        defaultTarifShouldBeFound("paidDate.equals=" + DEFAULT_PAID_DATE);

        // Get all the tarifList where paidDate equals to UPDATED_PAID_DATE
        defaultTarifShouldNotBeFound("paidDate.equals=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllTarifsByPaidDateIsInShouldWork() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where paidDate in DEFAULT_PAID_DATE or UPDATED_PAID_DATE
        defaultTarifShouldBeFound("paidDate.in=" + DEFAULT_PAID_DATE + "," + UPDATED_PAID_DATE);

        // Get all the tarifList where paidDate equals to UPDATED_PAID_DATE
        defaultTarifShouldNotBeFound("paidDate.in=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllTarifsByPaidDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where paidDate is not null
        defaultTarifShouldBeFound("paidDate.specified=true");

        // Get all the tarifList where paidDate is null
        defaultTarifShouldNotBeFound("paidDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTarifsByPaidDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where paidDate is greater than or equal to DEFAULT_PAID_DATE
        defaultTarifShouldBeFound("paidDate.greaterThanOrEqual=" + DEFAULT_PAID_DATE);

        // Get all the tarifList where paidDate is greater than or equal to UPDATED_PAID_DATE
        defaultTarifShouldNotBeFound("paidDate.greaterThanOrEqual=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllTarifsByPaidDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where paidDate is less than or equal to DEFAULT_PAID_DATE
        defaultTarifShouldBeFound("paidDate.lessThanOrEqual=" + DEFAULT_PAID_DATE);

        // Get all the tarifList where paidDate is less than or equal to SMALLER_PAID_DATE
        defaultTarifShouldNotBeFound("paidDate.lessThanOrEqual=" + SMALLER_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllTarifsByPaidDateIsLessThanSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where paidDate is less than DEFAULT_PAID_DATE
        defaultTarifShouldNotBeFound("paidDate.lessThan=" + DEFAULT_PAID_DATE);

        // Get all the tarifList where paidDate is less than UPDATED_PAID_DATE
        defaultTarifShouldBeFound("paidDate.lessThan=" + UPDATED_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllTarifsByPaidDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where paidDate is greater than DEFAULT_PAID_DATE
        defaultTarifShouldNotBeFound("paidDate.greaterThan=" + DEFAULT_PAID_DATE);

        // Get all the tarifList where paidDate is greater than SMALLER_PAID_DATE
        defaultTarifShouldBeFound("paidDate.greaterThan=" + SMALLER_PAID_DATE);
    }

    @Test
    @Transactional
    void getAllTarifsByPaidIsEqualToSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where paid equals to DEFAULT_PAID
        defaultTarifShouldBeFound("paid.equals=" + DEFAULT_PAID);

        // Get all the tarifList where paid equals to UPDATED_PAID
        defaultTarifShouldNotBeFound("paid.equals=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    void getAllTarifsByPaidIsInShouldWork() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where paid in DEFAULT_PAID or UPDATED_PAID
        defaultTarifShouldBeFound("paid.in=" + DEFAULT_PAID + "," + UPDATED_PAID);

        // Get all the tarifList where paid equals to UPDATED_PAID
        defaultTarifShouldNotBeFound("paid.in=" + UPDATED_PAID);
    }

    @Test
    @Transactional
    void getAllTarifsByPaidIsNullOrNotNull() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where paid is not null
        defaultTarifShouldBeFound("paid.specified=true");

        // Get all the tarifList where paid is null
        defaultTarifShouldNotBeFound("paid.specified=false");
    }

    @Test
    @Transactional
    void getAllTarifsByNumberOfDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where numberOfDevice equals to DEFAULT_NUMBER_OF_DEVICE
        defaultTarifShouldBeFound("numberOfDevice.equals=" + DEFAULT_NUMBER_OF_DEVICE);

        // Get all the tarifList where numberOfDevice equals to UPDATED_NUMBER_OF_DEVICE
        defaultTarifShouldNotBeFound("numberOfDevice.equals=" + UPDATED_NUMBER_OF_DEVICE);
    }

    @Test
    @Transactional
    void getAllTarifsByNumberOfDeviceIsInShouldWork() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where numberOfDevice in DEFAULT_NUMBER_OF_DEVICE or UPDATED_NUMBER_OF_DEVICE
        defaultTarifShouldBeFound("numberOfDevice.in=" + DEFAULT_NUMBER_OF_DEVICE + "," + UPDATED_NUMBER_OF_DEVICE);

        // Get all the tarifList where numberOfDevice equals to UPDATED_NUMBER_OF_DEVICE
        defaultTarifShouldNotBeFound("numberOfDevice.in=" + UPDATED_NUMBER_OF_DEVICE);
    }

    @Test
    @Transactional
    void getAllTarifsByNumberOfDeviceIsNullOrNotNull() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where numberOfDevice is not null
        defaultTarifShouldBeFound("numberOfDevice.specified=true");

        // Get all the tarifList where numberOfDevice is null
        defaultTarifShouldNotBeFound("numberOfDevice.specified=false");
    }

    @Test
    @Transactional
    void getAllTarifsByNumberOfDeviceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where numberOfDevice is greater than or equal to DEFAULT_NUMBER_OF_DEVICE
        defaultTarifShouldBeFound("numberOfDevice.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_DEVICE);

        // Get all the tarifList where numberOfDevice is greater than or equal to UPDATED_NUMBER_OF_DEVICE
        defaultTarifShouldNotBeFound("numberOfDevice.greaterThanOrEqual=" + UPDATED_NUMBER_OF_DEVICE);
    }

    @Test
    @Transactional
    void getAllTarifsByNumberOfDeviceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where numberOfDevice is less than or equal to DEFAULT_NUMBER_OF_DEVICE
        defaultTarifShouldBeFound("numberOfDevice.lessThanOrEqual=" + DEFAULT_NUMBER_OF_DEVICE);

        // Get all the tarifList where numberOfDevice is less than or equal to SMALLER_NUMBER_OF_DEVICE
        defaultTarifShouldNotBeFound("numberOfDevice.lessThanOrEqual=" + SMALLER_NUMBER_OF_DEVICE);
    }

    @Test
    @Transactional
    void getAllTarifsByNumberOfDeviceIsLessThanSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where numberOfDevice is less than DEFAULT_NUMBER_OF_DEVICE
        defaultTarifShouldNotBeFound("numberOfDevice.lessThan=" + DEFAULT_NUMBER_OF_DEVICE);

        // Get all the tarifList where numberOfDevice is less than UPDATED_NUMBER_OF_DEVICE
        defaultTarifShouldBeFound("numberOfDevice.lessThan=" + UPDATED_NUMBER_OF_DEVICE);
    }

    @Test
    @Transactional
    void getAllTarifsByNumberOfDeviceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        // Get all the tarifList where numberOfDevice is greater than DEFAULT_NUMBER_OF_DEVICE
        defaultTarifShouldNotBeFound("numberOfDevice.greaterThan=" + DEFAULT_NUMBER_OF_DEVICE);

        // Get all the tarifList where numberOfDevice is greater than SMALLER_NUMBER_OF_DEVICE
        defaultTarifShouldBeFound("numberOfDevice.greaterThan=" + SMALLER_NUMBER_OF_DEVICE);
    }

    @Test
    @Transactional
    void getAllTarifsByDeviceIsEqualToSomething() throws Exception {
        Device device;
        if (TestUtil.findAll(em, Device.class).isEmpty()) {
            tarifRepository.saveAndFlush(tarif);
            device = DeviceResourceIT.createEntity(em);
        } else {
            device = TestUtil.findAll(em, Device.class).get(0);
        }
        em.persist(device);
        em.flush();
        tarif.addDevice(device);
        tarifRepository.saveAndFlush(tarif);
        Long deviceId = device.getId();

        // Get all the tarifList where device equals to deviceId
        defaultTarifShouldBeFound("deviceId.equals=" + deviceId);

        // Get all the tarifList where device equals to (deviceId + 1)
        defaultTarifShouldNotBeFound("deviceId.equals=" + (deviceId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTarifShouldBeFound(String filter) throws Exception {
        restTarifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tarif.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].paidDate").value(hasItem(DEFAULT_PAID_DATE.toString())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].numberOfDevice").value(hasItem(DEFAULT_NUMBER_OF_DEVICE.intValue())));

        // Check, that the count call also returns 1
        restTarifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTarifShouldNotBeFound(String filter) throws Exception {
        restTarifMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTarifMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTarif() throws Exception {
        // Get the tarif
        restTarifMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTarif() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();

        // Update the tarif
        Tarif updatedTarif = tarifRepository.findById(tarif.getId()).get();
        // Disconnect from session so that the updates on updatedTarif are not directly saved in db
        em.detach(updatedTarif);
        updatedTarif.amount(UPDATED_AMOUNT).paidDate(UPDATED_PAID_DATE).paid(UPDATED_PAID).numberOfDevice(UPDATED_NUMBER_OF_DEVICE);

        restTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTarif.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTarif))
            )
            .andExpect(status().isOk());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
        Tarif testTarif = tarifList.get(tarifList.size() - 1);
        assertThat(testTarif.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTarif.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testTarif.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testTarif.getNumberOfDevice()).isEqualTo(UPDATED_NUMBER_OF_DEVICE);
    }

    @Test
    @Transactional
    void putNonExistingTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tarif.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tarif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tarif)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTarifWithPatch() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();

        // Update the tarif using partial update
        Tarif partialUpdatedTarif = new Tarif();
        partialUpdatedTarif.setId(tarif.getId());

        partialUpdatedTarif.amount(UPDATED_AMOUNT).paidDate(UPDATED_PAID_DATE).paid(UPDATED_PAID);

        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarif))
            )
            .andExpect(status().isOk());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
        Tarif testTarif = tarifList.get(tarifList.size() - 1);
        assertThat(testTarif.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTarif.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testTarif.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testTarif.getNumberOfDevice()).isEqualTo(DEFAULT_NUMBER_OF_DEVICE);
    }

    @Test
    @Transactional
    void fullUpdateTarifWithPatch() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();

        // Update the tarif using partial update
        Tarif partialUpdatedTarif = new Tarif();
        partialUpdatedTarif.setId(tarif.getId());

        partialUpdatedTarif.amount(UPDATED_AMOUNT).paidDate(UPDATED_PAID_DATE).paid(UPDATED_PAID).numberOfDevice(UPDATED_NUMBER_OF_DEVICE);

        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarif))
            )
            .andExpect(status().isOk());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
        Tarif testTarif = tarifList.get(tarifList.size() - 1);
        assertThat(testTarif.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testTarif.getPaidDate()).isEqualTo(UPDATED_PAID_DATE);
        assertThat(testTarif.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testTarif.getNumberOfDevice()).isEqualTo(UPDATED_NUMBER_OF_DEVICE);
    }

    @Test
    @Transactional
    void patchNonExistingTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tarif.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tarif))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarif() throws Exception {
        int databaseSizeBeforeUpdate = tarifRepository.findAll().size();
        tarif.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTarifMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tarif)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tarif in the database
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarif() throws Exception {
        // Initialize the database
        tarifRepository.saveAndFlush(tarif);

        int databaseSizeBeforeDelete = tarifRepository.findAll().size();

        // Delete the tarif
        restTarifMockMvc
            .perform(delete(ENTITY_API_URL_ID, tarif.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Tarif> tarifList = tarifRepository.findAll();
        assertThat(tarifList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package af.mcit.customsystem.web.rest;

import af.mcit.customsystem.domain.CustomCharges;
import af.mcit.customsystem.repository.CustomChargesRepository;
import af.mcit.customsystem.service.CustomChargesQueryService;
import af.mcit.customsystem.service.CustomChargesService;
import af.mcit.customsystem.service.criteria.CustomChargesCriteria;
import af.mcit.customsystem.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link af.mcit.customsystem.domain.CustomCharges}.
 */
@RestController
@RequestMapping("/api")
public class CustomChargesResource {

    private final Logger log = LoggerFactory.getLogger(CustomChargesResource.class);

    private static final String ENTITY_NAME = "customCharges";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomChargesService customChargesService;

    private final CustomChargesRepository customChargesRepository;

    private final CustomChargesQueryService customChargesQueryService;

    public CustomChargesResource(
        CustomChargesService customChargesService,
        CustomChargesRepository customChargesRepository,
        CustomChargesQueryService customChargesQueryService
    ) {
        this.customChargesService = customChargesService;
        this.customChargesRepository = customChargesRepository;
        this.customChargesQueryService = customChargesQueryService;
    }

    /**
     * {@code POST  /custom-charges} : Create a new customCharges.
     *
     * @param customCharges the customCharges to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customCharges, or with status {@code 400 (Bad Request)} if the customCharges has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/custom-charges")
    public ResponseEntity<CustomCharges> createCustomCharges(@RequestBody CustomCharges customCharges) throws URISyntaxException {
        log.debug("REST request to save CustomCharges : {}", customCharges);
        if (customCharges.getId() != null) {
            throw new BadRequestAlertException("A new customCharges cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomCharges result = customChargesService.save(customCharges);
        return ResponseEntity
            .created(new URI("/api/custom-charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /custom-charges/:id} : Updates an existing customCharges.
     *
     * @param id the id of the customCharges to save.
     * @param customCharges the customCharges to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customCharges,
     * or with status {@code 400 (Bad Request)} if the customCharges is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customCharges couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/custom-charges/{id}")
    public ResponseEntity<CustomCharges> updateCustomCharges(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomCharges customCharges
    ) throws URISyntaxException {
        log.debug("REST request to update CustomCharges : {}, {}", id, customCharges);
        if (customCharges.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customCharges.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customChargesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomCharges result = customChargesService.update(customCharges);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customCharges.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /custom-charges/:id} : Partial updates given fields of an existing customCharges, field will ignore if it is null
     *
     * @param id the id of the customCharges to save.
     * @param customCharges the customCharges to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customCharges,
     * or with status {@code 400 (Bad Request)} if the customCharges is not valid,
     * or with status {@code 404 (Not Found)} if the customCharges is not found,
     * or with status {@code 500 (Internal Server Error)} if the customCharges couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/custom-charges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomCharges> partialUpdateCustomCharges(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CustomCharges customCharges
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomCharges partially : {}, {}", id, customCharges);
        if (customCharges.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customCharges.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customChargesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomCharges> result = customChargesService.partialUpdate(customCharges);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customCharges.getId().toString())
        );
    }

    /**
     * {@code GET  /custom-charges} : get all the customCharges.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customCharges in body.
     */
    @GetMapping("/custom-charges")
    public ResponseEntity<List<CustomCharges>> getAllCustomCharges(
        CustomChargesCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get CustomCharges by criteria: {}", criteria);
        Page<CustomCharges> page = customChargesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /custom-charges/count} : count all the customCharges.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/custom-charges/count")
    public ResponseEntity<Long> countCustomCharges(CustomChargesCriteria criteria) {
        log.debug("REST request to count CustomCharges by criteria: {}", criteria);
        return ResponseEntity.ok().body(customChargesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /custom-charges/:id} : get the "id" customCharges.
     *
     * @param id the id of the customCharges to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customCharges, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/custom-charges/{id}")
    public ResponseEntity<CustomCharges> getCustomCharges(@PathVariable Long id) {
        log.debug("REST request to get CustomCharges : {}", id);
        Optional<CustomCharges> customCharges = customChargesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customCharges);
    }

    /**
     * {@code DELETE  /custom-charges/:id} : delete the "id" customCharges.
     *
     * @param id the id of the customCharges to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/custom-charges/{id}")
    public ResponseEntity<Void> deleteCustomCharges(@PathVariable Long id) {
        log.debug("REST request to delete CustomCharges : {}", id);
        customChargesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

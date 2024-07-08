package af.mcit.customsystem.web.rest;

import af.mcit.customsystem.domain.Tarif;
import af.mcit.customsystem.repository.TarifRepository;
import af.mcit.customsystem.service.TarifQueryService;
import af.mcit.customsystem.service.TarifService;
import af.mcit.customsystem.service.criteria.TarifCriteria;
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
 * REST controller for managing {@link af.mcit.customsystem.domain.Tarif}.
 */
@RestController
@RequestMapping("/api")
public class TarifResource {

    private final Logger log = LoggerFactory.getLogger(TarifResource.class);

    private static final String ENTITY_NAME = "tarif";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TarifService tarifService;

    private final TarifRepository tarifRepository;

    private final TarifQueryService tarifQueryService;

    public TarifResource(TarifService tarifService, TarifRepository tarifRepository, TarifQueryService tarifQueryService) {
        this.tarifService = tarifService;
        this.tarifRepository = tarifRepository;
        this.tarifQueryService = tarifQueryService;
    }

    /**
     * {@code POST  /tarifs} : Create a new tarif.
     *
     * @param tarif the tarif to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tarif, or with status {@code 400 (Bad Request)} if the tarif has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tarifs")
    public ResponseEntity<Tarif> createTarif(@RequestBody Tarif tarif) throws URISyntaxException {
        log.debug("REST request to save Tarif : {}", tarif);
        if (tarif.getId() != null) {
            throw new BadRequestAlertException("A new tarif cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tarif result = tarifService.save(tarif);
        return ResponseEntity
            .created(new URI("/api/tarifs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tarifs/:id} : Updates an existing tarif.
     *
     * @param id the id of the tarif to save.
     * @param tarif the tarif to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarif,
     * or with status {@code 400 (Bad Request)} if the tarif is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tarif couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tarifs/{id}")
    public ResponseEntity<Tarif> updateTarif(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tarif tarif)
        throws URISyntaxException {
        log.debug("REST request to update Tarif : {}, {}", id, tarif);
        if (tarif.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarif.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Tarif result = tarifService.update(tarif);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tarif.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tarifs/:id} : Partial updates given fields of an existing tarif, field will ignore if it is null
     *
     * @param id the id of the tarif to save.
     * @param tarif the tarif to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tarif,
     * or with status {@code 400 (Bad Request)} if the tarif is not valid,
     * or with status {@code 404 (Not Found)} if the tarif is not found,
     * or with status {@code 500 (Internal Server Error)} if the tarif couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tarifs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Tarif> partialUpdateTarif(@PathVariable(value = "id", required = false) final Long id, @RequestBody Tarif tarif)
        throws URISyntaxException {
        log.debug("REST request to partial update Tarif partially : {}, {}", id, tarif);
        if (tarif.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tarif.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tarifRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Tarif> result = tarifService.partialUpdate(tarif);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tarif.getId().toString())
        );
    }

    /**
     * {@code GET  /tarifs} : get all the tarifs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tarifs in body.
     */
    @GetMapping("/tarifs")
    public ResponseEntity<List<Tarif>> getAllTarifs(
        TarifCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Tarifs by criteria: {}", criteria);
        Page<Tarif> page = tarifQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tarifs/count} : count all the tarifs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tarifs/count")
    public ResponseEntity<Long> countTarifs(TarifCriteria criteria) {
        log.debug("REST request to count Tarifs by criteria: {}", criteria);
        return ResponseEntity.ok().body(tarifQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tarifs/:id} : get the "id" tarif.
     *
     * @param id the id of the tarif to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tarif, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tarifs/{id}")
    public ResponseEntity<Tarif> getTarif(@PathVariable Long id) {
        log.debug("REST request to get Tarif : {}", id);
        Optional<Tarif> tarif = tarifService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tarif);
    }

    /**
     * {@code DELETE  /tarifs/:id} : delete the "id" tarif.
     *
     * @param id the id of the tarif to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tarifs/{id}")
    public ResponseEntity<Void> deleteTarif(@PathVariable Long id) {
        log.debug("REST request to delete Tarif : {}", id);
        tarifService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

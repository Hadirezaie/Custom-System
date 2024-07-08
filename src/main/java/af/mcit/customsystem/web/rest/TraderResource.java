package af.mcit.customsystem.web.rest;

import af.mcit.customsystem.domain.Trader;
import af.mcit.customsystem.repository.TraderRepository;
import af.mcit.customsystem.service.TraderQueryService;
import af.mcit.customsystem.service.TraderService;
import af.mcit.customsystem.service.criteria.TraderCriteria;
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
 * REST controller for managing {@link af.mcit.customsystem.domain.Trader}.
 */
@RestController
@RequestMapping("/api")
public class TraderResource {

    private final Logger log = LoggerFactory.getLogger(TraderResource.class);

    private static final String ENTITY_NAME = "trader";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TraderService traderService;

    private final TraderRepository traderRepository;

    private final TraderQueryService traderQueryService;

    public TraderResource(TraderService traderService, TraderRepository traderRepository, TraderQueryService traderQueryService) {
        this.traderService = traderService;
        this.traderRepository = traderRepository;
        this.traderQueryService = traderQueryService;
    }

    /**
     * {@code POST  /traders} : Create a new trader.
     *
     * @param trader the trader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trader, or with status {@code 400 (Bad Request)} if the trader has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/traders")
    public ResponseEntity<Trader> createTrader(@RequestBody Trader trader) throws URISyntaxException {
        log.debug("REST request to save Trader : {}", trader);
        if (trader.getId() != null) {
            throw new BadRequestAlertException("A new trader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Trader result = traderService.save(trader);
        return ResponseEntity
            .created(new URI("/api/traders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /traders/:id} : Updates an existing trader.
     *
     * @param id the id of the trader to save.
     * @param trader the trader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trader,
     * or with status {@code 400 (Bad Request)} if the trader is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/traders/{id}")
    public ResponseEntity<Trader> updateTrader(@PathVariable(value = "id", required = false) final Long id, @RequestBody Trader trader)
        throws URISyntaxException {
        log.debug("REST request to update Trader : {}, {}", id, trader);
        if (trader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!traderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Trader result = traderService.update(trader);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trader.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /traders/:id} : Partial updates given fields of an existing trader, field will ignore if it is null
     *
     * @param id the id of the trader to save.
     * @param trader the trader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trader,
     * or with status {@code 400 (Bad Request)} if the trader is not valid,
     * or with status {@code 404 (Not Found)} if the trader is not found,
     * or with status {@code 500 (Internal Server Error)} if the trader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/traders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Trader> partialUpdateTrader(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Trader trader
    ) throws URISyntaxException {
        log.debug("REST request to partial update Trader partially : {}, {}", id, trader);
        if (trader.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trader.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!traderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Trader> result = traderService.partialUpdate(trader);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trader.getId().toString())
        );
    }

    /**
     * {@code GET  /traders} : get all the traders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of traders in body.
     */
    @GetMapping("/traders")
    public ResponseEntity<List<Trader>> getAllTraders(
        TraderCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Traders by criteria: {}", criteria);
        Page<Trader> page = traderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /traders/count} : count all the traders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/traders/count")
    public ResponseEntity<Long> countTraders(TraderCriteria criteria) {
        log.debug("REST request to count Traders by criteria: {}", criteria);
        return ResponseEntity.ok().body(traderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /traders/:id} : get the "id" trader.
     *
     * @param id the id of the trader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/traders/{id}")
    public ResponseEntity<Trader> getTrader(@PathVariable Long id) {
        log.debug("REST request to get Trader : {}", id);
        Optional<Trader> trader = traderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trader);
    }

    /**
     * {@code DELETE  /traders/:id} : delete the "id" trader.
     *
     * @param id the id of the trader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/traders/{id}")
    public ResponseEntity<Void> deleteTrader(@PathVariable Long id) {
        log.debug("REST request to delete Trader : {}", id);
        traderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

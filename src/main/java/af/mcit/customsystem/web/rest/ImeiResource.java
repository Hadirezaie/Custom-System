package af.mcit.customsystem.web.rest;

import af.mcit.customsystem.domain.Imei;
import af.mcit.customsystem.repository.ImeiRepository;
import af.mcit.customsystem.service.ImeiQueryService;
import af.mcit.customsystem.service.ImeiService;
import af.mcit.customsystem.service.criteria.ImeiCriteria;
import af.mcit.customsystem.service.dto.ImeiCheckDTO;
import af.mcit.customsystem.service.dto.ImeiCheckResultDTO;
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
 * REST controller for managing {@link af.mcit.customsystem.domain.Imei}.
 */
@RestController
@RequestMapping("/api")
public class ImeiResource {

    private final Logger log = LoggerFactory.getLogger(ImeiResource.class);

    private static final String ENTITY_NAME = "imei";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImeiService imeiService;

    private final ImeiRepository imeiRepository;

    private final ImeiQueryService imeiQueryService;

    public ImeiResource(ImeiService imeiService, ImeiRepository imeiRepository, ImeiQueryService imeiQueryService) {
        this.imeiService = imeiService;
        this.imeiRepository = imeiRepository;
        this.imeiQueryService = imeiQueryService;
    }

    /**
     * {@code POST  /imeis} : Create a new imei.
     *
     * @param imei the imei to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new imei, or with status {@code 400 (Bad Request)} if the
     *         imei has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/imeis")
    public ResponseEntity<Imei> createImei(@RequestBody Imei imei) throws URISyntaxException {
        log.debug("REST request to save Imei : {}", imei);
        if (imei.getId() != null) {
            throw new BadRequestAlertException("A new imei cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Imei result = imeiService.save(imei);
        return ResponseEntity
            .created(new URI("/api/imeis/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /imeis/:id} : Updates an existing imei.
     *
     * @param id   the id of the imei to save.
     * @param imei the imei to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated imei,
     *         or with status {@code 400 (Bad Request)} if the imei is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the imei
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/imeis/{id}")
    public ResponseEntity<Imei> updateImei(@PathVariable(value = "id", required = false) final Long id, @RequestBody Imei imei)
        throws URISyntaxException {
        log.debug("REST request to update Imei : {}, {}", id, imei);
        if (imei.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imei.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imeiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Imei result = imeiService.update(imei);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, imei.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /imeis/:id} : Partial updates given fields of an existing imei,
     * field will ignore if it is null
     *
     * @param id   the id of the imei to save.
     * @param imei the imei to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated imei,
     *         or with status {@code 400 (Bad Request)} if the imei is not valid,
     *         or with status {@code 404 (Not Found)} if the imei is not found,
     *         or with status {@code 500 (Internal Server Error)} if the imei
     *         couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/imeis/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Imei> partialUpdateImei(@PathVariable(value = "id", required = false) final Long id, @RequestBody Imei imei)
        throws URISyntaxException {
        log.debug("REST request to partial update Imei partially : {}, {}", id, imei);
        if (imei.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imei.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imeiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Imei> result = imeiService.partialUpdate(imei);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, imei.getId().toString())
        );
    }

    /**
     * {@code GET  /imeis} : get all the imeis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of imeis in body.
     */
    @GetMapping("/imeis")
    public ResponseEntity<List<Imei>> getAllImeis(ImeiCriteria criteria, @org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get Imeis by criteria: {}", criteria);
        Page<Imei> page = imeiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /imeis/count} : count all the imeis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count
     *         in body.
     */
    @GetMapping("/imeis/count")
    public ResponseEntity<Long> countImeis(ImeiCriteria criteria) {
        log.debug("REST request to count Imeis by criteria: {}", criteria);
        return ResponseEntity.ok().body(imeiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /imeis/:id} : get the "id" imei.
     *
     * @param id the id of the imei to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the imei, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/imeis/{id}")
    public ResponseEntity<Imei> getImei(@PathVariable Long id) {
        log.debug("REST request to get Imei : {}", id);
        Optional<Imei> imei = imeiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imei);
    }

    /**
     * {@code DELETE  /imeis/:id} : delete the "id" imei.
     *
     * @param id the id of the imei to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/imeis/{id}")
    public ResponseEntity<Void> deleteImei(@PathVariable Long id) {
        log.debug("REST request to delete Imei : {}", id);
        imeiService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("imeis/imei-check")
    public ImeiCheckResultDTO checkImei(@RequestBody ImeiCheckDTO imeiCheckDTO) {
        log.debug("Chceck Imei if paid the customs fee: {}", imeiCheckDTO);
        return imeiService.checkImei(imeiCheckDTO);
    }
}

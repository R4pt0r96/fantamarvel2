package com.deluca.fantamarvel.web.rest;

import com.deluca.fantamarvel.domain.BonusMalus;
import com.deluca.fantamarvel.repository.BonusMalusRepository;
import com.deluca.fantamarvel.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.deluca.fantamarvel.domain.BonusMalus}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BonusMalusResource {

    private final Logger log = LoggerFactory.getLogger(BonusMalusResource.class);

    private static final String ENTITY_NAME = "bonusMalus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BonusMalusRepository bonusMalusRepository;

    public BonusMalusResource(BonusMalusRepository bonusMalusRepository) {
        this.bonusMalusRepository = bonusMalusRepository;
    }

    /**
     * {@code POST  /bonus-maluses} : Create a new bonusMalus.
     *
     * @param bonusMalus the bonusMalus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bonusMalus, or with status {@code 400 (Bad Request)} if the bonusMalus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bonus-maluses")
    public ResponseEntity<BonusMalus> createBonusMalus(@Valid @RequestBody BonusMalus bonusMalus) throws URISyntaxException {
        log.debug("REST request to save BonusMalus : {}", bonusMalus);
        if (bonusMalus.getId() != null) {
            throw new BadRequestAlertException("A new bonusMalus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BonusMalus result = bonusMalusRepository.save(bonusMalus);
        return ResponseEntity
            .created(new URI("/api/bonus-maluses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bonus-maluses/:id} : Updates an existing bonusMalus.
     *
     * @param id the id of the bonusMalus to save.
     * @param bonusMalus the bonusMalus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bonusMalus,
     * or with status {@code 400 (Bad Request)} if the bonusMalus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bonusMalus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bonus-maluses/{id}")
    public ResponseEntity<BonusMalus> updateBonusMalus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BonusMalus bonusMalus
    ) throws URISyntaxException {
        log.debug("REST request to update BonusMalus : {}, {}", id, bonusMalus);
        if (bonusMalus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bonusMalus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bonusMalusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BonusMalus result = bonusMalusRepository.save(bonusMalus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bonusMalus.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bonus-maluses/:id} : Partial updates given fields of an existing bonusMalus, field will ignore if it is null
     *
     * @param id the id of the bonusMalus to save.
     * @param bonusMalus the bonusMalus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bonusMalus,
     * or with status {@code 400 (Bad Request)} if the bonusMalus is not valid,
     * or with status {@code 404 (Not Found)} if the bonusMalus is not found,
     * or with status {@code 500 (Internal Server Error)} if the bonusMalus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bonus-maluses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BonusMalus> partialUpdateBonusMalus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BonusMalus bonusMalus
    ) throws URISyntaxException {
        log.debug("REST request to partial update BonusMalus partially : {}, {}", id, bonusMalus);
        if (bonusMalus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bonusMalus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bonusMalusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BonusMalus> result = bonusMalusRepository
            .findById(bonusMalus.getId())
            .map(existingBonusMalus -> {
                if (bonusMalus.getDescrizione() != null) {
                    existingBonusMalus.setDescrizione(bonusMalus.getDescrizione());
                }
                if (bonusMalus.getPunti() != null) {
                    existingBonusMalus.setPunti(bonusMalus.getPunti());
                }

                return existingBonusMalus;
            })
            .map(bonusMalusRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, bonusMalus.getId().toString())
        );
    }

    /**
     * {@code GET  /bonus-maluses} : get all the bonusMaluses.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bonusMaluses in body.
     */
    @GetMapping("/bonus-maluses")
    public ResponseEntity<List<BonusMalus>> getAllBonusMaluses(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of BonusMaluses");
        Page<BonusMalus> page;
        if (eagerload) {
            page = bonusMalusRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = bonusMalusRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bonus-maluses/:id} : get the "id" bonusMalus.
     *
     * @param id the id of the bonusMalus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bonusMalus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bonus-maluses/{id}")
    public ResponseEntity<BonusMalus> getBonusMalus(@PathVariable Long id) {
        log.debug("REST request to get BonusMalus : {}", id);
        Optional<BonusMalus> bonusMalus = bonusMalusRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(bonusMalus);
    }

    /**
     * {@code DELETE  /bonus-maluses/:id} : delete the "id" bonusMalus.
     *
     * @param id the id of the bonusMalus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bonus-maluses/{id}")
    public ResponseEntity<Void> deleteBonusMalus(@PathVariable Long id) {
        log.debug("REST request to delete BonusMalus : {}", id);
        bonusMalusRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

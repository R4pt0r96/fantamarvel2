package com.deluca.fantamarvel.web.rest;

import com.deluca.fantamarvel.domain.Lega;
import com.deluca.fantamarvel.repository.LegaRepository;
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
 * REST controller for managing {@link com.deluca.fantamarvel.domain.Lega}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LegaResource {

    private final Logger log = LoggerFactory.getLogger(LegaResource.class);

    private static final String ENTITY_NAME = "lega";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LegaRepository legaRepository;

    public LegaResource(LegaRepository legaRepository) {
        this.legaRepository = legaRepository;
    }

    /**
     * {@code POST  /legas} : Create a new lega.
     *
     * @param lega the lega to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lega, or with status {@code 400 (Bad Request)} if the lega has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/legas")
    public ResponseEntity<Lega> createLega(@Valid @RequestBody Lega lega) throws URISyntaxException {
        log.debug("REST request to save Lega : {}", lega);
        if (lega.getId() != null) {
            throw new BadRequestAlertException("A new lega cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Lega result = legaRepository.save(lega);
        return ResponseEntity
            .created(new URI("/api/legas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /legas/:id} : Updates an existing lega.
     *
     * @param id the id of the lega to save.
     * @param lega the lega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lega,
     * or with status {@code 400 (Bad Request)} if the lega is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/legas/{id}")
    public ResponseEntity<Lega> updateLega(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Lega lega)
        throws URISyntaxException {
        log.debug("REST request to update Lega : {}, {}", id, lega);
        if (lega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lega.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!legaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Lega result = legaRepository.save(lega);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lega.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /legas/:id} : Partial updates given fields of an existing lega, field will ignore if it is null
     *
     * @param id the id of the lega to save.
     * @param lega the lega to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lega,
     * or with status {@code 400 (Bad Request)} if the lega is not valid,
     * or with status {@code 404 (Not Found)} if the lega is not found,
     * or with status {@code 500 (Internal Server Error)} if the lega couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/legas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Lega> partialUpdateLega(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Lega lega
    ) throws URISyntaxException {
        log.debug("REST request to partial update Lega partially : {}, {}", id, lega);
        if (lega.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lega.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!legaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Lega> result = legaRepository
            .findById(lega.getId())
            .map(existingLega -> {
                if (lega.getCodice() != null) {
                    existingLega.setCodice(lega.getCodice());
                }
                if (lega.getDescrizione() != null) {
                    existingLega.setDescrizione(lega.getDescrizione());
                }
                if (lega.getIsPrivate() != null) {
                    existingLega.setIsPrivate(lega.getIsPrivate());
                }
                if (lega.getNome() != null) {
                    existingLega.setNome(lega.getNome());
                }

                return existingLega;
            })
            .map(legaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lega.getId().toString())
        );
    }

    /**
     * {@code GET  /legas} : get all the legas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of legas in body.
     */
    @GetMapping("/legas")
    public ResponseEntity<List<Lega>> getAllLegas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Legas");
        Page<Lega> page = legaRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /legas/:id} : get the "id" lega.
     *
     * @param id the id of the lega to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lega, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/legas/{id}")
    public ResponseEntity<Lega> getLega(@PathVariable Long id) {
        log.debug("REST request to get Lega : {}", id);
        Optional<Lega> lega = legaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lega);
    }

    /**
     * {@code DELETE  /legas/:id} : delete the "id" lega.
     *
     * @param id the id of the lega to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/legas/{id}")
    public ResponseEntity<Void> deleteLega(@PathVariable Long id) {
        log.debug("REST request to delete Lega : {}", id);
        legaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

package com.deluca.fantamarvel.web.rest;

import com.deluca.fantamarvel.domain.Squadra;
import com.deluca.fantamarvel.repository.SquadraRepository;
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
 * REST controller for managing {@link com.deluca.fantamarvel.domain.Squadra}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SquadraResource {

    private final Logger log = LoggerFactory.getLogger(SquadraResource.class);

    private static final String ENTITY_NAME = "squadra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SquadraRepository squadraRepository;

    public SquadraResource(SquadraRepository squadraRepository) {
        this.squadraRepository = squadraRepository;
    }

    /**
     * {@code POST  /squadras} : Create a new squadra.
     *
     * @param squadra the squadra to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new squadra, or with status {@code 400 (Bad Request)} if the squadra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/squadras")
    public ResponseEntity<Squadra> createSquadra(@Valid @RequestBody Squadra squadra) throws URISyntaxException {
        log.debug("REST request to save Squadra : {}", squadra);
        if (squadra.getId() != null) {
            throw new BadRequestAlertException("A new squadra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Squadra result = squadraRepository.save(squadra);
        return ResponseEntity
            .created(new URI("/api/squadras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /squadras/:id} : Updates an existing squadra.
     *
     * @param id the id of the squadra to save.
     * @param squadra the squadra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated squadra,
     * or with status {@code 400 (Bad Request)} if the squadra is not valid,
     * or with status {@code 500 (Internal Server Error)} if the squadra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/squadras/{id}")
    public ResponseEntity<Squadra> updateSquadra(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Squadra squadra
    ) throws URISyntaxException {
        log.debug("REST request to update Squadra : {}, {}", id, squadra);
        if (squadra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, squadra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!squadraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Squadra result = squadraRepository.save(squadra);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, squadra.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /squadras/:id} : Partial updates given fields of an existing squadra, field will ignore if it is null
     *
     * @param id the id of the squadra to save.
     * @param squadra the squadra to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated squadra,
     * or with status {@code 400 (Bad Request)} if the squadra is not valid,
     * or with status {@code 404 (Not Found)} if the squadra is not found,
     * or with status {@code 500 (Internal Server Error)} if the squadra couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/squadras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Squadra> partialUpdateSquadra(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Squadra squadra
    ) throws URISyntaxException {
        log.debug("REST request to partial update Squadra partially : {}, {}", id, squadra);
        if (squadra.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, squadra.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!squadraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Squadra> result = squadraRepository
            .findById(squadra.getId())
            .map(existingSquadra -> {
                if (squadra.getGettoni() != null) {
                    existingSquadra.setGettoni(squadra.getGettoni());
                }
                if (squadra.getIsInLega() != null) {
                    existingSquadra.setIsInLega(squadra.getIsInLega());
                }
                if (squadra.getIsSalvata() != null) {
                    existingSquadra.setIsSalvata(squadra.getIsSalvata());
                }
                if (squadra.getNome() != null) {
                    existingSquadra.setNome(squadra.getNome());
                }
                if (squadra.getPunteggio() != null) {
                    existingSquadra.setPunteggio(squadra.getPunteggio());
                }

                return existingSquadra;
            })
            .map(squadraRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, squadra.getId().toString())
        );
    }

    /**
     * {@code GET  /squadras} : get all the squadras.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of squadras in body.
     */
    @GetMapping("/squadras")
    public ResponseEntity<List<Squadra>> getAllSquadras(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Squadras");
        Page<Squadra> page;
        if (eagerload) {
            page = squadraRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = squadraRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /squadras/:id} : get the "id" squadra.
     *
     * @param id the id of the squadra to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the squadra, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/squadras/{id}")
    public ResponseEntity<Squadra> getSquadra(@PathVariable Long id) {
        log.debug("REST request to get Squadra : {}", id);
        Optional<Squadra> squadra = squadraRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(squadra);
    }

    /**
     * {@code DELETE  /squadras/:id} : delete the "id" squadra.
     *
     * @param id the id of the squadra to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/squadras/{id}")
    public ResponseEntity<Void> deleteSquadra(@PathVariable Long id) {
        log.debug("REST request to delete Squadra : {}", id);
        squadraRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

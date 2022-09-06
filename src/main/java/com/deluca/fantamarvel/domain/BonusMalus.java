package com.deluca.fantamarvel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BonusMalus.
 */
@Entity
@Table(name = "bonus_malus")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BonusMalus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 1024)
    @Column(name = "descrizione", length = 1024, nullable = false)
    private String descrizione;

    @NotNull
    @Column(name = "punti", nullable = false)
    private Integer punti;

    @NotNull
    @ManyToOne
    @JsonIgnoreProperties(value = { "userExtendeds" }, allowSetters = true)
    private Film film;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "bonusmaluses")
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bonusmaluses", "teams" }, allowSetters = true)
    private Set<Personaggio> personaggios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BonusMalus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public BonusMalus descrizione(String descrizione) {
        this.setDescrizione(descrizione);
        return this;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getPunti() {
        return this.punti;
    }

    public BonusMalus punti(Integer punti) {
        this.setPunti(punti);
        return this;
    }

    public void setPunti(Integer punti) {
        this.punti = punti;
    }

    public Film getFilm() {
        return this.film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public BonusMalus film(Film film) {
        this.setFilm(film);
        return this;
    }

    public Set<Personaggio> getPersonaggios() {
        return this.personaggios;
    }

    public void setPersonaggios(Set<Personaggio> personaggios) {
        if (this.personaggios != null) {
            this.personaggios.forEach(i -> i.removeBonusmalus(this));
        }
        if (personaggios != null) {
            personaggios.forEach(i -> i.addBonusmalus(this));
        }
        this.personaggios = personaggios;
    }

    public BonusMalus personaggios(Set<Personaggio> personaggios) {
        this.setPersonaggios(personaggios);
        return this;
    }

    public BonusMalus addPersonaggio(Personaggio personaggio) {
        this.personaggios.add(personaggio);
        personaggio.getBonusmaluses().add(this);
        return this;
    }

    public BonusMalus removePersonaggio(Personaggio personaggio) {
        this.personaggios.remove(personaggio);
        personaggio.getBonusmaluses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BonusMalus)) {
            return false;
        }
        return id != null && id.equals(((BonusMalus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BonusMalus{" +
            "id=" + getId() +
            ", descrizione='" + getDescrizione() + "'" +
            ", punti=" + getPunti() +
            "}";
    }
}

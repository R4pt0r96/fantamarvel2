package com.deluca.fantamarvel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FilmPersonaggio.
 */
@Entity
@Table(name = "film_personaggio")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FilmPersonaggio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "costo", nullable = false)
    private Integer costo;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties(value = { "userExtendeds" }, allowSetters = true)
    private Film film;

    @ManyToOne
    @JsonIgnoreProperties(value = { "bonusmaluses", "teams" }, allowSetters = true)
    private Personaggio personaggio;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FilmPersonaggio id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCosto() {
        return this.costo;
    }

    public FilmPersonaggio costo(Integer costo) {
        this.setCosto(costo);
        return this;
    }

    public void setCosto(Integer costo) {
        this.costo = costo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public FilmPersonaggio isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Film getFilm() {
        return this.film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public FilmPersonaggio film(Film film) {
        this.setFilm(film);
        return this;
    }

    public Personaggio getPersonaggio() {
        return this.personaggio;
    }

    public void setPersonaggio(Personaggio personaggio) {
        this.personaggio = personaggio;
    }

    public FilmPersonaggio personaggio(Personaggio personaggio) {
        this.setPersonaggio(personaggio);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilmPersonaggio)) {
            return false;
        }
        return id != null && id.equals(((FilmPersonaggio) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilmPersonaggio{" +
            "id=" + getId() +
            ", costo=" + getCosto() +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}

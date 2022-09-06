package com.deluca.fantamarvel.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Film.
 */
@Entity
@Table(name = "film")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Film implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "titolo", nullable = false)
    private String titolo;

    @Column(name = "data_uscita")
    private ZonedDateTime dataUscita;

    @Column(name = "data_fine_iscrizione")
    private ZonedDateTime dataFineIscrizione;

    @Column(name = "is_active")
    private Boolean isActive;

    @Size(max = 1024)
    @Column(name = "url_img", length = 1024)
    private String urlImg;

    @Lob
    @Column(name = "descrizione")
    private String descrizione;

    @ManyToMany
    @JoinTable(
        name = "rel_film__user_extended",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "user_extended_id")
    )
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "films" }, allowSetters = true)
    private Set<UserExtended> userExtendeds = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Film id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return this.titolo;
    }

    public Film titolo(String titolo) {
        this.setTitolo(titolo);
        return this;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public ZonedDateTime getDataUscita() {
        return this.dataUscita;
    }

    public Film dataUscita(ZonedDateTime dataUscita) {
        this.setDataUscita(dataUscita);
        return this;
    }

    public void setDataUscita(ZonedDateTime dataUscita) {
        this.dataUscita = dataUscita;
    }

    public ZonedDateTime getDataFineIscrizione() {
        return this.dataFineIscrizione;
    }

    public Film dataFineIscrizione(ZonedDateTime dataFineIscrizione) {
        this.setDataFineIscrizione(dataFineIscrizione);
        return this;
    }

    public void setDataFineIscrizione(ZonedDateTime dataFineIscrizione) {
        this.dataFineIscrizione = dataFineIscrizione;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Film isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getUrlImg() {
        return this.urlImg;
    }

    public Film urlImg(String urlImg) {
        this.setUrlImg(urlImg);
        return this;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public Film descrizione(String descrizione) {
        this.setDescrizione(descrizione);
        return this;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Set<UserExtended> getUserExtendeds() {
        return this.userExtendeds;
    }

    public void setUserExtendeds(Set<UserExtended> userExtendeds) {
        this.userExtendeds = userExtendeds;
    }

    public Film userExtendeds(Set<UserExtended> userExtendeds) {
        this.setUserExtendeds(userExtendeds);
        return this;
    }

    public Film addUserExtended(UserExtended userExtended) {
        this.userExtendeds.add(userExtended);
        userExtended.getFilms().add(this);
        return this;
    }

    public Film removeUserExtended(UserExtended userExtended) {
        this.userExtendeds.remove(userExtended);
        userExtended.getFilms().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Film)) {
            return false;
        }
        return id != null && id.equals(((Film) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Film{" +
            "id=" + getId() +
            ", titolo='" + getTitolo() + "'" +
            ", dataUscita='" + getDataUscita() + "'" +
            ", dataFineIscrizione='" + getDataFineIscrizione() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", urlImg='" + getUrlImg() + "'" +
            ", descrizione='" + getDescrizione() + "'" +
            "}";
    }
}

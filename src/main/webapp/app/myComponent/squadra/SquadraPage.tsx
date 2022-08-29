import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getEntitiesFilmPersonaggio } from 'app/entities/film-personaggio/film-personaggio.reducer';
import { getEntities as getEntitiesUserExt } from 'app/entities/user-extended/user-extended.reducer';
import { isEmpty } from 'lodash';
import React, { useEffect, useState } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Button, Card, CardBody, CardGroup, CardImg, CardSubtitle, CardTitle, Col, ListGroup, ListGroupItem, Row } from 'reactstrap';
import { getEntities as getEntitiesSquadra } from '../../entities/squadra/squadra.reducer';
import CardPersonaggio from './CardPersonaggio';

import './SquadraPage.scss';

// Trovare i Personaggi e filtrarli per film e sono attivi.
// film personaggi contengono il costo ma anche il personaggio
// Salvare la squadra con i personaggi selezionati

const SquadraPage = (props: RouteComponentProps<{ idFilm: any; idUserExt: any }>) => {
  const dispatch = useAppDispatch();
  const listaFilmPersonaggi = useAppSelector(state => state.filmPersonaggio.entities);
  const listaFilmPersonaggiIsLoading = useAppSelector(state => state.filmPersonaggio.loading);
  const listaSquadreIsLoading = useAppSelector(state => state.squadra.loading);
  const listaSquadre = useAppSelector(state => state.squadra.entities);

  //filtra i personaggi per il film
  let listaFilmPersonaggiFiltratiPerFilm;

  if (!listaFilmPersonaggiIsLoading) {
    listaFilmPersonaggiFiltratiPerFilm = listaFilmPersonaggi.filter(fp => fp?.film?.id == props.match.params.idFilm && fp.isActive);
  }

  let squadraFiltrata;
  if (!listaSquadreIsLoading) {
    for (let element of listaSquadre) {
      if (element.userExtended.id == props.match.params.idUserExt) {
        squadraFiltrata = { ...element };
      }
    }
  }

  useEffect(() => {
    dispatch(getEntitiesFilmPersonaggio({}));
    dispatch(getEntitiesSquadra({}));
  }, []);

  const findCostoFilmPersonaggio = idPersonaggio => {
    for (let pers of listaFilmPersonaggiFiltratiPerFilm) {
      if (pers.personaggio.id == idPersonaggio) {
        return pers.costo;
      }
    }
  };

  const sortArrayByCosto = (a, b) => {
    if (a.costo > b.costo) {
      return -1;
    } else if (a.costo < b.costo) {
      return 1;
    }
    return 0;
  };

  const ordinaFilmPersonaggioInBaseCosto = () => {
    let filmPersonaggioOrdinati = [];
    if (!listaFilmPersonaggiIsLoading && !listaSquadreIsLoading) {
      for (const pers of listaFilmPersonaggiFiltratiPerFilm) {
        let isPresente = false;

        if (squadraFiltrata?.personaggios[2]) {
          if (
            pers.personaggio.id != squadraFiltrata?.personaggios[2].id &&
            pers.personaggio.id != squadraFiltrata?.personaggios[1].id &&
            pers.personaggio.id != squadraFiltrata?.personaggios[0].id
          ) {
            isPresente = true;
          }
        } else if (squadraFiltrata?.personaggios[1]) {
          if (pers.personaggio.id != squadraFiltrata?.personaggios[1].id && pers.personaggio.id != squadraFiltrata?.personaggios[0].id) {
            isPresente = true;
          }
        } else if (squadraFiltrata?.personaggios[0]) {
          if (pers.personaggio.id != squadraFiltrata?.personaggios[0].id) {
            isPresente = true;
          }
        }
        if (isPresente) {
          filmPersonaggioOrdinati.push(pers);
        }
      }
      if (isEmpty(filmPersonaggioOrdinati)) {
        filmPersonaggioOrdinati = [...listaFilmPersonaggiFiltratiPerFilm];
      }
    }

    return filmPersonaggioOrdinati.sort(sortArrayByCosto);
  };

  return (
    <div>
      <Row>
        <Col md="8">
          <CardGroup>
            {squadraFiltrata?.personaggios[0] ? (
              <Card key={squadraFiltrata?.personaggios[0].id} className="card_personaggio_squadra">
                <CardImg
                  className="immagine_personaggio_squadra"
                  alt="Card image cap"
                  src={squadraFiltrata?.personaggios[0].urlImg}
                  top
                  width="100%"
                />
                <CardBody>
                  <CardTitle tag="h5">{squadraFiltrata?.personaggios[0].nome}</CardTitle>
                  <CardSubtitle className="mb-2 text-muted" tag="h6">
                    Coins: {findCostoFilmPersonaggio(squadraFiltrata?.personaggios[0].id)}
                  </CardSubtitle>
                  <Button color="danger" outline>
                    Rimuovi
                  </Button>
                </CardBody>
              </Card>
            ) : (
              <Card className="card_personaggio_squadra">
                <CardImg
                  alt="Card image cap"
                  src="https://img.icons8.com/ios-glyphs/480/question-mark.png"
                  top
                  width="100%"
                  className="immagine_personaggio_squadra"
                />
                <CardBody>
                  <CardTitle tag="h5">Vuoto</CardTitle>
                  <CardSubtitle className="mb-2 text-muted" tag="h6"></CardSubtitle>
                </CardBody>
              </Card>
            )}
            {squadraFiltrata?.personaggios[1] ? (
              <Card key={squadraFiltrata?.personaggios[1].id} className="card_personaggio_squadra">
                <CardImg
                  className="immagine_personaggio_squadra"
                  alt="Card image cap"
                  src={squadraFiltrata?.personaggios[1].urlImg}
                  top
                  width="100%"
                />
                <CardBody>
                  <CardTitle tag="h5">{squadraFiltrata?.personaggios[1].nome}</CardTitle>
                  <CardSubtitle className="mb-2 text-muted" tag="h6">
                    Coins: {findCostoFilmPersonaggio(squadraFiltrata?.personaggios[1].id)}
                  </CardSubtitle>
                  <Button color="danger" outline>
                    Rimuovi
                  </Button>
                </CardBody>
              </Card>
            ) : (
              <Card className="card_personaggio_squadra">
                <CardImg
                  alt="Card image cap"
                  src="https://img.icons8.com/ios-glyphs/480/question-mark.png"
                  top
                  width="100%"
                  className="immagine_personaggio_squadra"
                />
                <CardBody>
                  <CardTitle tag="h5">Vuoto</CardTitle>
                  <CardSubtitle className="mb-2 text-muted" tag="h6"></CardSubtitle>
                </CardBody>
              </Card>
            )}
            {squadraFiltrata?.personaggios[2] ? (
              <Card key={squadraFiltrata?.personaggios[2].id} className="card_personaggio_squadra">
                <CardImg
                  className="immagine_personaggio_squadra"
                  alt="Card image cap"
                  src={squadraFiltrata?.personaggios[2].urlImg}
                  top
                  width="100%"
                />
                <CardBody>
                  <CardTitle tag="h5">{squadraFiltrata?.personaggios[2].nome}</CardTitle>
                  <CardSubtitle className="mb-2 text-muted" tag="h6">
                    Coins: {findCostoFilmPersonaggio(squadraFiltrata?.personaggios[2].id)}
                  </CardSubtitle>
                  <Button color="danger" outline>
                    Rimuovi
                  </Button>
                </CardBody>
              </Card>
            ) : (
              <Card className="card_personaggio_squadra">
                <CardImg
                  alt="Card image cap"
                  src="https://img.icons8.com/ios-glyphs/480/question-mark.png"
                  top
                  width="100%"
                  className="immagine_personaggio_squadra"
                />
                <CardBody>
                  <CardTitle tag="h5">Vuoto</CardTitle>
                  <CardSubtitle className="mb-2 text-muted" tag="h6"></CardSubtitle>
                </CardBody>
              </Card>
            )}
          </CardGroup>
        </Col>
        <Col md="4">info</Col>
      </Row>
      <div className="lista_personaggi">
        {ordinaFilmPersonaggioInBaseCosto().map(pers => {
          return <CardPersonaggio personaggio={pers} gettoniSquadra={squadraFiltrata.gettoni} key={pers.id} />;
        })}
      </div>
    </div>
  );
};

export default SquadraPage;

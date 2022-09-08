import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getEntitiesFilmPersonaggio } from 'app/entities/film-personaggio/film-personaggio.reducer';
import React, { useEffect } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Alert, Button, Card, CardBody, CardGroup, CardImg, CardSubtitle, CardTitle, Col, Row } from 'reactstrap';
import { getEntities as getEntitiesSquadra, updateEntity as updateSquadra } from '../../entities/squadra/squadra.reducer';
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
  const account = useAppSelector(state => state.authentication.account);
  //const userExtended = useAppSelector(state => state.userExtended.entity);

  useEffect(() => {
    dispatch(getEntitiesFilmPersonaggio({}));
    dispatch(getEntitiesSquadra({}));
    // dispatch(getEntityUsrExt(props.match.params.idUserExt));
  }, []);

  if (account.id != props.match.params.idUserExt) {
    return <Alert color="danger">PAGINA NON DISPONIBILE</Alert>;
  }

  //filtra i personaggi per il film
  let listaFilmPersonaggiFiltratiPerFilm;

  if (!listaFilmPersonaggiIsLoading) {
    listaFilmPersonaggiFiltratiPerFilm = listaFilmPersonaggi.filter(fp => fp?.film?.id == props.match.params.idFilm && fp.isActive);
  }

  let squadraFiltrata;
  if (!listaSquadreIsLoading) {
    for (let element of listaSquadre) {
      if (element.userExtended.id == props.match.params.idUserExt && element.film.id == props.match.params.idFilm) {
        squadraFiltrata = { ...element };
      }
    }
  }

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

        for (let index = 0; index < squadraFiltrata?.personaggios.length; index++) {
          const element = squadraFiltrata.personaggios[index];

          if (pers.personaggio.id == element.id) {
            isPresente = true;
          }
        }

        if (!isPresente) {
          filmPersonaggioOrdinati.push(pers);
        }
      }
    }

    return filmPersonaggioOrdinati.sort(sortArrayByCosto);
  };

  const aggiornaSquadraInserendoPersonaggio = filmPersonaggio => {
    squadraFiltrata.personaggios = Object.assign([], squadraFiltrata.personaggios);
    if (squadraFiltrata.personaggios.length < 3) {
      squadraFiltrata.gettoni -= filmPersonaggio.costo;
      squadraFiltrata.personaggios.push(filmPersonaggio.personaggio);
      dispatch(updateSquadra(squadraFiltrata));
    }
  };

  const removePersonaggioDallaSquadra = personaggio => {
    squadraFiltrata.personaggios = squadraFiltrata.personaggios.filter(pers => pers.id != personaggio.id);
    squadraFiltrata.gettoni += findCostoFilmPersonaggio(personaggio.id);
    dispatch(updateSquadra(squadraFiltrata));
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
                    Coins: {listaFilmPersonaggiIsLoading ? 0 : findCostoFilmPersonaggio(squadraFiltrata?.personaggios[0].id)}
                  </CardSubtitle>
                  {!squadraFiltrata?.isSalvata ? (
                    <Button color="danger" outline onClick={() => removePersonaggioDallaSquadra(squadraFiltrata.personaggios[0])}>
                      Rimuovi
                    </Button>
                  ) : null}
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
                    Coins: {listaFilmPersonaggiIsLoading ? 0 : findCostoFilmPersonaggio(squadraFiltrata?.personaggios[1].id)}
                  </CardSubtitle>
                  {!squadraFiltrata?.isSalvata ? (
                    <Button color="danger" outline onClick={() => removePersonaggioDallaSquadra(squadraFiltrata?.personaggios[1])}>
                      Rimuovi
                    </Button>
                  ) : null}
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
                    Coins: {listaFilmPersonaggiIsLoading ? 0 : findCostoFilmPersonaggio(squadraFiltrata?.personaggios[2].id)}
                  </CardSubtitle>
                  {!squadraFiltrata?.isSalvata ? (
                    <Button color="danger" outline onClick={() => removePersonaggioDallaSquadra(squadraFiltrata?.personaggios[2])}>
                      Rimuovi
                    </Button>
                  ) : null}
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
        <Col md="4">
          <Alert className="coins_text">
            <img src="content/images/shield_logo.png" className="logo_coins" />
            &nbsp; {'COINS: ' + squadraFiltrata?.gettoni}
          </Alert>
          <Alert color="info" className="coins_text">
            <img src="content/images/target.png" className="logo_coins" />
            &nbsp; {'PUNTEGGIO: ' + squadraFiltrata?.punteggio}
          </Alert>
        </Col>
      </Row>
      {!squadraFiltrata?.isSalvata ? (
        <div className="lista_personaggi">
          {ordinaFilmPersonaggioInBaseCosto().map(pers => {
            return (
              <CardPersonaggio
                personaggio={pers}
                gettoniSquadra={squadraFiltrata?.gettoni}
                key={pers.id}
                addPersonaggio={aggiornaSquadraInserendoPersonaggio}
              />
            );
          })}
        </div>
      ) : (
        <Alert color="info">Tempo scaduto: squadra non modificabile</Alert>
      )}
    </div>
  );
};

export default SquadraPage;

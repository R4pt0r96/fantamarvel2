import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/film/film.reducer';
import React, { useEffect } from 'react';
import { Alert, Card } from 'reactstrap';
import LocandinaFilm from './locandinaFilm/locandinaFilm';

import './filmCard.css';
import DescrizioneFilm from './locandinaFilm/descrizioneFilm';
import { isUndefined } from 'lodash';

const FilmCard = props => {
  const isFilmTrovato = isUndefined(props.film?.dataUscita);

  if (!isFilmTrovato) {
    return (
      <Card style={{ marginBottom: '1rem' }} className="filmCard">
        <LocandinaFilm immagine={props.film.urlImg} />

        <DescrizioneFilm
          film={props.film}
          // titolo={filmActive[0]?.titolo}
          // data={filmActive[0]?.dataUscita}
          // descrizione={filmActive[0]?.descrizione}
          // dataFine={filmActive[0]?.dataFineIscrizione}
          bonusMalusClick={props.onBonusMalusClick}
        />
      </Card>
    );
  } else {
    return <Alert color="warning">Nessun film disponibile al momento</Alert>;
  }
};

export default FilmCard;

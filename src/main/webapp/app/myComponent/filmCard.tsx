import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/film/film.reducer';
import React, { useEffect } from 'react';
import { Alert, Card } from 'reactstrap';
import LocandinaFilm from './locandinaFilm/locandinaFilm';

import './filmCard.css';
import DescrizioneFilm from './locandinaFilm/descrizioneFilm';
import { isNull, isUndefined } from 'lodash';

const FilmCard = () => {
  const dispatch = useAppDispatch();

  const filmList = useAppSelector(state => state.film.entities);
  const loadingFilmList = useAppSelector(state => state.film.loading);
  const filmActive = filmList.filter(film => {
    return film.isActive ? film : null;
  });

  const isFilmTrovato = isUndefined(filmActive[0]?.dataUscita);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  if (!isFilmTrovato) {
    return (
      <Card style={{ marginBottom: '1rem' }} className="filmCard">
        <LocandinaFilm immagine={filmActive[0]?.urlImg} />
        {loadingFilmList ? (
          <p>Loading...</p>
        ) : (
          <DescrizioneFilm
            titolo={filmActive[0]?.titolo}
            data={filmActive[0]?.dataUscita}
            descrizione={filmActive[0]?.descrizione}
            dataFine={filmActive[0]?.dataFineIscrizione}
          />
        )}
      </Card>
    );
  } else {
    return <Alert color="warning">Nessun film disponibile al momento</Alert>;
  }
};

export default FilmCard;

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/film/film.reducer';
import React, { useEffect, useState } from 'react';
import { Card } from 'reactstrap';
import LocandinaFilm from './locandinaFilm/locandinaFilm';

import './filmCard.css';
import DescrizioneFilm from './locandinaFilm/descrizioneFilm';

const FilmCard = () => {
  const dispatch = useAppDispatch();
  const [firstRender, setFirstRender] = useState(true);

  const filmList = useAppSelector(state => state.film.entities);
  const filmActive = filmList.filter(film => {
    return film.isActive ? film : null;
  });

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  return (
    <Card style={{ marginBottom: '1rem' }} className="filmCard">
      <LocandinaFilm immagine={filmActive[0]?.urlImg} />
      <DescrizioneFilm titolo={filmActive[0]?.titolo} data={filmActive[0]?.dataUscita} descrizione={filmActive[0]?.descrizione} />
    </Card>
  );
};

export default FilmCard;

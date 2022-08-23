import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
import CountdownTimer from '../countdown/countdownTimer';
import ConvertitoreDate from '../utility/ConvertitoreDate';

import './descrizioneFilm.scss';

const DescrizioneFilm = props => {
  const dataUscita = ConvertitoreDate(props.film.dataUscita);

  return (
    <div className="descrizioneFilmContainer">
      <h1 className="titoloFilm" data-heading="thor">
        {props.film.titolo}
      </h1>
      <p id="dataUscita">Al cinema dal: {dataUscita}</p>
      <p id="descrizioneFilm">{props.film.descrizione}</p>
      <Button outline color="primary" onClick={props.bonusMalusClick}>
        <b>Bonus Malus</b>
      </Button>
      <CountdownTimer targetDate={props.film.dataFineIscrizione} film={props.film} />
    </div>
  );
};

export default DescrizioneFilm;

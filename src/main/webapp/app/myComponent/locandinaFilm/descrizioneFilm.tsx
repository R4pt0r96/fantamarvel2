import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
import CountdownTimer from '../countdown/countdownTimer';
import ConvertitoreDate from '../utility/ConvertitoreDate';

import './descrizioneFilm.scss';

const DescrizioneFilm = props => {
  const dataUscita = ConvertitoreDate(props.data);

  return (
    <div className="descrizioneFilmContainer">
      <h1 className="titoloFilm" data-heading="thor">
        {props.titolo}
      </h1>
      <p id="dataUscita">Al cinema dal: {dataUscita}</p>
      <p id="descrizioneFilm">{props.descrizione}</p>
      <Link to="/bonusmaluspage">
        <Button outline color="primary">
          <b>Bonus Malus</b>
        </Button>
      </Link>
      <CountdownTimer targetDate={props.data} />
    </div>
  );
};

export default DescrizioneFilm;

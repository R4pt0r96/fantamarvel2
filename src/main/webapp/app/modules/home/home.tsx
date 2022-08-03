import './home.scss';

import React, { useEffect, useState } from 'react';

import { Row, Col, Alert, Card } from 'reactstrap';
import { useAppSelector, useAppDispatch } from 'app/config/store';
import Regolamento from 'app/myComponent/regolamento';
import FilmCard from 'app/myComponent/filmCard';
import BonusMalusComponent from 'app/myComponent/bonusMalus/BonusMalus';
import { getEntities as getEntitiesFilm } from 'app/entities/film/film.reducer';
import { getEntities as getEntitiesBonusMalus } from 'app/entities/bonus-malus/bonus-malus.reducer';

export const Home = props => {
  // const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();

  const filmList = useAppSelector(state => state.film.entities);
  const loadingFilmList = useAppSelector(state => state.film.loading);
  const bonusMalusList = useAppSelector(state => state.bonusMalus.entities);

  const filmActive = filmList.filter(film => {
    return film.isActive ? film : null;
  });

  const [showBonusMalus, setShowBonusMalus] = useState(false);

  const changeSetBonusMalusHandler = () => {
    setShowBonusMalus(!showBonusMalus);
  };

  useEffect(() => {
    dispatch(getEntitiesFilm({}));
    dispatch(getEntitiesBonusMalus({}));
  }, []);

  return (
    <>
      <Row className="welcome_center">
        {/* <Col md="3" className="pad"> */}
        {/* <span className="hipster rounded " /> */}
        {/* </Col> */}
        <Col md="12">
          <h2>Benvenuto nel Fanta-Marvel</h2>
          {/* {account?.login ? (
            <div>
              <Alert color="success">Hai effettuato il login {account.login}.</Alert>
            </div>
          ) : (
            <div>
              <Alert color="warning">
                Non hai ancora un account?&nbsp;
                <Link to="/account/register" className="alert-link">
                  Registrati qui
                </Link>
              </Alert>
            </div>
          )} */}
        </Col>
      </Row>
      <Row className="verticalShift">
        <Col>{loadingFilmList ? <p>Loading...</p> : <FilmCard onBonusMalusClick={changeSetBonusMalusHandler} film={filmActive[0]} />}</Col>
      </Row>
      {showBonusMalus ? <BonusMalusComponent idFilm={filmActive[0].id} bonusMalusList={bonusMalusList} /> : null}
      <Regolamento />
    </>
  );
};

export default Home;

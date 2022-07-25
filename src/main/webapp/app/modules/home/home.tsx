import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert, Card } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import Regolamento from 'app/myComponent/regolamento';
import FilmCard from 'app/myComponent/filmCard';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <>
      <Row className="welcome_center">
        {/* <Col md="3" className="pad"> */}
        {/* <span className="hipster rounded " /> */}
        {/* </Col> */}
        <Col md="12">
          <h2>Benvenuto nel Fanta-Marvel</h2>
          {account?.login ? (
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
          )}
        </Col>
      </Row>
      <Row className="verticalShift">
        <Col>
          <FilmCard />
        </Col>
      </Row>
      <Regolamento />
    </>
  );
};

export default Home;

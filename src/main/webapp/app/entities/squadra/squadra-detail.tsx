import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './squadra.reducer';

export const SquadraDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const squadraEntity = useAppSelector(state => state.squadra.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="squadraDetailsHeading">Squadra</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{squadraEntity.id}</dd>
          <dt>
            <span id="gettoni">Gettoni</span>
          </dt>
          <dd>{squadraEntity.gettoni}</dd>
          <dt>
            <span id="isInLega">Is In Lega</span>
          </dt>
          <dd>{squadraEntity.isInLega ? 'true' : 'false'}</dd>
          <dt>
            <span id="isSalvata">Is Salvata</span>
          </dt>
          <dd>{squadraEntity.isSalvata ? 'true' : 'false'}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{squadraEntity.nome}</dd>
          <dt>
            <span id="punteggio">Punteggio</span>
          </dt>
          <dd>{squadraEntity.punteggio}</dd>
          <dt>Film</dt>
          <dd>{squadraEntity.film ? squadraEntity.film.titolo : ''}</dd>
          <dt>Lega</dt>
          <dd>{squadraEntity.lega ? squadraEntity.lega.nome : ''}</dd>
          <dt>User Extended</dt>
          <dd>{squadraEntity.userExtended ? squadraEntity.userExtended.username : ''}</dd>
          <dt>Personaggio</dt>
          <dd>
            {squadraEntity.personaggios
              ? squadraEntity.personaggios.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.nome}</a>
                    {squadraEntity.personaggios && i === squadraEntity.personaggios.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/squadra" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/squadra/${squadraEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default SquadraDetail;

import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './personaggio.reducer';

export const PersonaggioDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const personaggioEntity = useAppSelector(state => state.personaggio.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="personaggioDetailsHeading">Personaggio</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{personaggioEntity.id}</dd>
          <dt>
            <span id="nome">Nome</span>
          </dt>
          <dd>{personaggioEntity.nome}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{personaggioEntity.description}</dd>
          <dt>
            <span id="note">Note</span>
          </dt>
          <dd>{personaggioEntity.note}</dd>
          <dt>
            <span id="isActive">Is Active</span>
          </dt>
          <dd>{personaggioEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="urlImg">Url Img</span>
          </dt>
          <dd>{personaggioEntity.urlImg}</dd>
          <dt>Bonusmalus</dt>
          <dd>
            {personaggioEntity.bonusmaluses
              ? personaggioEntity.bonusmaluses.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.descrizione}</a>
                    {personaggioEntity.bonusmaluses && i === personaggioEntity.bonusmaluses.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/personaggio" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/personaggio/${personaggioEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PersonaggioDetail;

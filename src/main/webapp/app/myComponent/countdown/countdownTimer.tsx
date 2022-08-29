import React, { useEffect } from 'react';
import DateTimeDisplay from './DateTimeDisplay';
import { useCountdown } from '../utility/useCountdown';
import './countdownTimer.css';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Alert, Button } from 'reactstrap';
import { Link, useHistory } from 'react-router-dom';
import { getSession } from 'app/shared/reducers/authentication';

import { getEntities as getEntitiesUserExt } from 'app/entities/user-extended/user-extended.reducer';
import {
  createEntity as createEntitySquadra,
  getEntity as getEntitySquadra,
  getEntities as getEntitiesSquadra,
  reset,
} from '../../entities/squadra/squadra.reducer';

const ExpiredNotice = () => {
  return (
    <div className="expired-notice">
      <span>Tempo Scaduto</span>
      <p>Dovrai attendere il prossimo film per giocare</p>
    </div>
  );
};

const ShowCounter = ({ days, hours, minutes, seconds, film }) => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();
  const squadraEntity = useAppSelector(state => state.squadra.entity);
  const userExt = useAppSelector(state => state.userExtended.entities).filter(user => user.user.id == account.id);
  const squadraList = useAppSelector(state => state.squadra.entities);

  const history = useHistory();

  useEffect(() => {
    if (account.login) {
      dispatch(getEntitiesSquadra({}));
      dispatch(getEntitiesUserExt({}));
    }
  }, [account]);

  const isNew = () => {
    for (let i = 0; i < squadraList.length; i++) {
      const element = squadraList[i];
      if (element?.userExtended?.id == userExt[0]?.id && element?.film?.id == film.id) {
        return false;
      }
    }
    return true;
  };

  const createSquadra = () => {
    if (isNew()) {
      const entity = {
        ...squadraEntity,
        film: film,
        userExtended: userExt[0],
      };

      dispatch(createEntitySquadra(entity));
    }

    history.push('/createSquadra/' + film.id + '/' + userExt[0].id);
  };

  return (
    <>
      <div className="show-counter">
        <DateTimeDisplay value={days} type={'Days'} isDanger={days <= 3} />
        <DateTimeDisplay value={hours} type={'Hours'} isDanger={false} />
        <DateTimeDisplay value={minutes} type={'Mins'} isDanger={false} />
        <DateTimeDisplay value={seconds} type={'Seconds'} isDanger={false} />
      </div>
      {account?.login ? (
        <Button onClick={createSquadra} color="success">
          CREA LA TUA SQUADRA
        </Button>
      ) : (
        <Alert color="warning">
          Non hai ancora un account?&nbsp;
          <Link to="/account/register" className="alert-link">
            Registrati qui
          </Link>
        </Alert>
      )}
    </>
  );
};

const CountdownTimer = ({ targetDate, film }) => {
  const [days, hours, minutes, seconds] = useCountdown(targetDate);

  if (days + hours + minutes + seconds <= 0) {
    return <ExpiredNotice />;
  } else {
    return <ShowCounter days={String(days)} hours={String(hours)} minutes={String(minutes)} seconds={String(seconds)} film={film} />;
  }
};

export default CountdownTimer;

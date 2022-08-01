import React from 'react';
import DateTimeDisplay from './DateTimeDisplay';
import { useCountdown } from '../utility/useCountdown';
import './countdownTimer.css';
import { useAppSelector } from 'app/config/store';
import { Alert, Button } from 'reactstrap';
import { Link } from 'react-router-dom';

const ExpiredNotice = () => {
  return (
    <div className="expired-notice">
      <span>Tempo Scaduto</span>
      <p>Dovrai attendere il prossimo film per giocare</p>
    </div>
  );
};

const ShowCounter = ({ days, hours, minutes, seconds }) => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <>
      <div className="show-counter">
        <DateTimeDisplay value={days} type={'Days'} isDanger={days <= 3} />
        <DateTimeDisplay value={hours} type={'Hours'} isDanger={false} />
        <DateTimeDisplay value={minutes} type={'Mins'} isDanger={false} />
        <DateTimeDisplay value={seconds} type={'Seconds'} isDanger={false} />
      </div>
      {account?.login ? (
        <Link to="/createSquadra">
          <Button color="success">CREA LA TUA SQUADRA</Button>
        </Link>
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

const CountdownTimer = ({ targetDate }) => {
  const [days, hours, minutes, seconds] = useCountdown(targetDate);

  if (days + hours + minutes + seconds <= 0) {
    return <ExpiredNotice />;
  } else {
    return <ShowCounter days={String(days)} hours={String(hours)} minutes={String(minutes)} seconds={String(seconds)} />;
  }
};

export default CountdownTimer;

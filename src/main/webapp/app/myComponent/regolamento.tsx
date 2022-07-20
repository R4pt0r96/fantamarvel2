import React from 'react';

import './regolamento.css';

const Regolamento = () => {
  return (
    <div className="regolamento_container">
      <p>REGOLAMENTO </p>
      <p>Registrati e crea la tua squadra. </p>
      <p>
        Avrai a disposizione <b>100 gettoni</b> e il tuo team dovrà avere <b>3 personaggi </b>marvel tra quelli a disposizione.
      </p>
      <p>
        Ogni personaggio avrà un costo diverso in base al film. Ovviamente i personaggi principali avranno un costo elevato e quindi
        inserendo il protagonista nella tua squadra, ti rimarranno personaggi non troppo rilevanti nel film o addirittura assenti.{' '}
      </p>
      <p>
        Ma ehi! Abbiamo visto come la marvel ultimamente ci sta facendo divertire con queste comparse o introducendo nuovi personaggi nei
        film, perciò quel personaggio potrebbe essere anche importante per la tua squadra se ci sarà un bonus comparsa!.
      </p>
      <p>
        Ed è qui che entrano in gioco i <b>Bonus-Malus</b>, una lista di azioni che faranno guadagnare punteggio alla tua squadra se i
        personaggi che hai scelto eseguiranno tali azioni.
      </p>
      <p>
        Potrai partecipare fino alla<b> scadenza del timer</b> che vedrai nella pagina di creazione squadra.
        <br />
        (Ricordati di salvare la tua squadra entro tale scadenza!)
      </p>

      <h6>Risultati</h6>
      <p>I risultati potranno essere pubblicati dal giorno successivo all'uscita del film nelle sale italiane, fino a 3-4 giorni dopo.</p>
      <p>
        Perciò rimani in attesa ed entra a vedere i risultati quando vuoi in quanto vedrai solo il punteggio finale senza spoiler su chi ha
        fatto cosa!
      </p>

      <h5>Partecipa anche tu!</h5>
      <p>
        Puoi inviare una email a <i>info@fantamarvel.com</i> e dare un consiglio su Bonus-Malus o personaggio da aggiungere.
      </p>
      <p>In questo modo possiamo divertirci partecipando tutti!!</p>
    </div>
  );
};

export default Regolamento;
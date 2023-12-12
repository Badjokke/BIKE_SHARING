import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faFlagUsa, faFlag } from '@fortawesome/free-solid-svg-icons';
import i18n from '../../../i18_lan/in18';
const LanguageSwitcher: React.FC = () => {
  const changeLanguage = (language: string) => {
    i18n.changeLanguage(language);
  };

  return (
    <div>
      <button onClick={() => changeLanguage('en')}>
        <FontAwesomeIcon icon={faFlagUsa} />
      </button>
      <button onClick={() => changeLanguage('es')}>
        <FontAwesomeIcon icon={faFlag} />
      </button>
    </div>
  );
};

export default LanguageSwitcher;
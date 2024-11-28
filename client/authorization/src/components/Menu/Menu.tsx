import { FunctionComponent, useContext } from 'react';
import { Link } from 'react-router-dom';
import { IconNames, pathnames } from '../../constants';
import { Icon } from '../Icon/Icon';
import { AuthContext } from '../../context/AuthContext';
import { LogoutForm } from '../LogoutForm/LogoutForm';
import { checkAdmin } from '../../utils/checkAdmin';

import './Menu.css';

export const Menu: FunctionComponent = () => {
  const { account } = useContext(AuthContext);
  const admin = checkAdmin(account);
  const disabled = !account;

  if (disabled) {
    return <></>;
  }

  const items = [
    <Link
      to={pathnames.account}
    >
      <Icon name={IconNames.Person} />
    </Link>,
    <Link
      to={pathnames.session}
    >
      <Icon name={IconNames.List} />
    </Link>,
    admin && (
      <Link
        to={pathnames.clients}
      >
        <Icon name={IconNames.Apps} />
      </Link>
    ),
    <LogoutForm
      submitCaption={''}
    >
      <button type='submit'>
        <Icon name={IconNames.Exit} />
      </button>
    </LogoutForm>
  ];

  return (
    <div className="menu">
      {items.map((item, index) => (
        <div key={index} className="menu-item">{item}</div>
      ))}
    </div>
  );
};

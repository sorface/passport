import { FunctionComponent } from 'react';
import { IconNames } from '../../../../constants';
import { Icon } from '../../../../components/Icon/Icon';

export interface ActivateStatusProps {
  icon: IconNames;
  title: string;
  message: string;
}

export const ActivateStatus: FunctionComponent<ActivateStatusProps> = ({
  icon,
  title,
  message,
}) => {
  return (
    <div className='activate-status'>
      <Icon name={icon} />
      <h2>{title}</h2>
      <div>{message}</div>
    </div>
  );
};

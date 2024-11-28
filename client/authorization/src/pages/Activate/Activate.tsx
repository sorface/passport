import { FunctionComponent, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useApiMethodCsrf } from '../../hooks/useApiMethodCsrf';
import { ConfirmBody, accountsApiDeclaration } from '../../apiDeclarations';
import { ActivateStatus } from './components/ActivateStatus/ActivateStatus';
import { Captions, IconNames, pathnames } from '../../constants';

import './Activate.css';

const tokenParamName = 'token';
const redirectTimeoutSec = 3;

export const Activate: FunctionComponent = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const tokenParam = searchParams.get(tokenParamName);
  const { apiMethodState, fetchData } = useApiMethodCsrf<unknown, ConfirmBody>(accountsApiDeclaration.confirm);
  const { process: { error }, data } = apiMethodState;

  useEffect(() => {
    if (!tokenParam) {
      return;
    }
    fetchData({ token: tokenParam });
  }, [tokenParam, fetchData]);

  useEffect(() => {
    if (!data) {
      return;
    }
    const timeoutId = setTimeout(() => {
      navigate(pathnames.account);
    }, redirectTimeoutSec * 1000);

    return () => {
      clearTimeout(timeoutId);
    }
  }, [data, navigate]);

  if (!tokenParam) {
    return (
      <ActivateStatus
        icon={IconNames.None}
        title={Captions.ActivateFail}
        message={Captions.TokenNotFound}
      />
    );
  }

  if (error) {
    return (
      <ActivateStatus
        icon={IconNames.None}
        title={Captions.ActivateFail}
        message={Captions.InvalidToken}
      />
    );
  }

  if (data) {
    return (
      <ActivateStatus
        icon={IconNames.Checkmark}
        title={Captions.ActivateSuccess}
        message={Captions.YouWillBeRedirected.replace(':redirectTimeoutSec', `${redirectTimeoutSec}`)}
      />
    );
  }

  return (
    <ActivateStatus
      icon={IconNames.Time}
      title={Captions.Activating}
      message={Captions.PleaseWait}
    />
  );
};

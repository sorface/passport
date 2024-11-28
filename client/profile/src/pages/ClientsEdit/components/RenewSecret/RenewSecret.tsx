import {FunctionComponent, MouseEvent} from 'react';
import {Captions} from '../../../../constants';
import {appsApiDeclaration, RefreshAppParams, RefreshSecretResponse} from '../../../../apiDeclarations';
import {Loader} from '../../../../components/Loader/Loader';

import './RenewSecret.css';
import {useApiMethodCsrf} from "../../../../hooks/useApiMethodCsrf";

export const RenewSecret: FunctionComponent<RefreshAppParams> = ({
                                                                     clientId,
                                                                 }) => {
    const {
        apiMethodState,
        fetchData,
    } = useApiMethodCsrf<RefreshSecretResponse, RefreshAppParams>(appsApiDeclaration.refresh);

    const {
        data,
        process: {loading, error},
    } = apiMethodState;

    const handleRenew = (event: MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        fetchData({
            clientId,
        });
    };

    return (
        <div className='renew-secret'>
            {loading && <Loader/>}
            {!!error && <div>{Captions.Error}: {error}</div>}
            {!!data && (<div>{Captions.NewSecret}: {data.clientSecret}</div>)}
            {!!data && (<div>{Captions.NewExpiredSecret}: {data.expiresAt?.toLocaleString()}</div>)}
            <button onClick={handleRenew}>{Captions.RenewSecret}</button>
        </div>
    );
};

import {FunctionComponent} from 'react';
import {useSearchParams} from 'react-router-dom';
import {Captions, IconNames} from '../../constants';

import './Failure.css';
import {ActivateStatus} from "../Activate/components/ActivateStatus/ActivateStatus";

const errorParamName = 'error';

export const Failure: FunctionComponent = () => {
    const [searchParams] = useSearchParams();
    const errorParam = searchParams.get(errorParamName);

    const urlDecode = (str: string): string => decodeURIComponent((str + '').replace(/\+/g, '%20'));

    if (!errorParam) {
        return (
            <ActivateStatus
                icon={IconNames.Alert}
                title={Captions.ErrorTitle}
                message={Captions.UnknownError}
            />
        );
    }

    return (
        <ActivateStatus
            icon={IconNames.Alert}
            title={Captions.ErrorTitle}
            message={urlDecode(errorParam)}
        />
    );
};

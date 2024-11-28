import {FunctionComponent} from 'react';
import {Captions, IconNames} from '../../constants';
import { Icon } from '../../components/Icon/Icon';

import './NotFound.css';

export const NotFound: FunctionComponent = () => {
    return (
        <div className='not-found'>
            <Icon name={IconNames.None} />
            <h2>{Captions.NotFound}</h2>
        </div>
    );
};

import {FunctionComponent} from 'react';

import logo from './logo-mini.jpg';
import './PageLogo.css';

export const PageLogo: FunctionComponent = () => {
    return (
        <img src={logo} className='page-logo' alt='sorface-logo'/>
    );
};

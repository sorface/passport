import {FunctionComponent} from 'react';
import {IconNames, IconThemePostfix} from '../../constants';

interface IconProps {
    name: IconNames;
}

export const Icon: FunctionComponent<IconProps> = ({
                                                       name,
                                                   }) => {
    const iconPostfix = IconThemePostfix.Light;
    return (
        <ion-icon name={`${name}${iconPostfix}`}></ion-icon>
    );
};

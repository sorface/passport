import React, {FunctionComponent} from 'react';

export interface Props {
    version: string;
}

export const Footer: FunctionComponent<Props> = ({version}) => {
    return (
        <footer style={{textAlign: 'center', padding: '20px', color: 'rgb(8 8 8)'}}>
            <p>Sorface Team 2024 - {version}</p>
        </footer>
    );
};

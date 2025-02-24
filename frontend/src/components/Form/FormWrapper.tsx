import {FunctionComponent, ReactNode} from 'react';

import './Form.css';

export interface FormWrapperProps {
    children?: ReactNode;
}

export const FormWrapper: FunctionComponent<FormWrapperProps> = ({children}) => {
    return (
        <div className='form'>
            <div className='form-content'>
                {children}
            </div>
        </div>
    );
};

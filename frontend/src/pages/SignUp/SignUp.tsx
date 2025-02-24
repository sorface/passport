import {FunctionComponent} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {Captions, otpExpiredTimeLocalStorageKey, pathnames} from '../../constants';
import {Field, Form} from '../../components/Form/Form';
import {FormWrapper} from '../../components/Form/FormWrapper';
import {ApiEndpoint} from '../../types/apiContracts';
import {useApiMethodCsrf} from '../../hooks/useApiMethodCsrf';
import {accountsApiDeclaration, SignUpBody, SignUpResponse} from '../../apiDeclarations';
import {convertFormDataToObject} from '../../utils/convertFormDataToObject';

import './SignUp.css';

const fields: Field[] = [
    {
        name: 'username',
        placeholder: Captions.Username,
        autoComplete: 'username',
        required: true,
    },
    {
        name: 'password',
        placeholder: Captions.Password,
        type: 'password',
        autoComplete: 'current-password',
        required: true,
    },
    {
        name: 'email',
        placeholder: Captions.Email,
        required: true,
    },
    {
        name: 'firstname',
        placeholder: Captions.FirstName,
        required: true,
    },
    {
        name: 'lastname',
        placeholder: Captions.LastName,
        required: true,
    },
];

export const SignUp: FunctionComponent = () => {
    const navigate = useNavigate();
    const {apiMethodState, fetchData} = useApiMethodCsrf<SignUpResponse, SignUpBody>(accountsApiDeclaration.signup);
    const {process: {error}, data} = apiMethodState;

    if (data) {
        localStorage.setItem(otpExpiredTimeLocalStorageKey, data.otpExpiredTime);
        navigate(pathnames.signUpConfirm);
    }

    const handleSignUpSubmit = (formData: FormData) => {
        const requestBody = convertFormDataToObject(formData);
        if (
            typeof requestBody.email !== 'string' ||
            typeof requestBody.password !== 'string' ||
            typeof requestBody.username !== 'string' ||
            typeof requestBody.firstname !== 'string' ||
            typeof requestBody.lastname !== 'string'
        ) {
            console.warn('Invalid sign up body data');
            return;
        }
        fetchData({
            email: requestBody.email,
            password: requestBody.password,
            username: requestBody.username,
            firstname: requestBody.firstname,
            lastname: requestBody.lastname,
        });
    };

    return (
        <div className='signup'>
            <FormWrapper>
                <h2>{Captions.WelcomeToSSO}</h2>
                <Link
                    to={pathnames.signIn}
                    className='signup-back'
                >
                    {Captions.Back}
                </Link>
                <Form
                    htmlMethod='POST'
                    htmlAction={ApiEndpoint.AccountsSignup}
                    fields={fields}
                    fieldErrors={{}}
                    submitCaption={Captions.SignUp}
                    onSubmit={handleSignUpSubmit}
                />
            </FormWrapper>
            {error && <div>Error: {error}</div>}
        </div>
    );
};

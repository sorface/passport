import React, {Fragment, FunctionComponent, MouseEvent, useState} from 'react';
import {Captions, pathnames} from '../../constants';
import {Field, Form} from '../../components/Form/Form';
import githubLogo from './img/github.svg';
import yandexLogo from './img/yandex.svg';
import twitchLogo from './img/twitch.svg';
import emailLogo from './img/email-svgrepo-com.svg';
import {FormWrapper} from '../../components/Form/FormWrapper';
import {useQueryFromErrors} from '../../hooks/useQueryFromErrors';
import {ApiEndpoint} from '../../types/apiContracts';
import {REACT_APP_BACKEND_URL} from "../../config";
import {PageLogo} from '../../components/PageLogo/PageLogo';
import {convertFormDataToObject} from '../../utils/convertFormDataToObject';
import {useApiMethodCsrf} from '../../hooks/useApiMethodCsrf';
import {accountsApiDeclaration, SignInBody} from '../../apiDeclarations';
import {Link} from 'react-router-dom';

import './SignIn.css';

const emailFields: Field[] = [
    {
        name: 'username',
        placeholder: Captions.EmailOrUsername,
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
];

export const SignIn: FunctionComponent = () => {
    const [withEmail, setWithEmail] = useState(false);
    const {queryFromErrors} = useQueryFromErrors();
    const {fetchData} = useApiMethodCsrf<unknown, SignInBody>(accountsApiDeclaration.signin);

    const signinItems = [
        {
            name: 'Yandex',
            href: '/oauth2/authorization/yandex',
            logo: yandexLogo,
        },
        {
            name: 'Github',
            href: '/oauth2/authorization/github',
            logo: githubLogo,
        },
        {
            name: 'Twitch',
            href: '/oauth2/authorization/twitch',
            logo: twitchLogo,
        },
        {
            name: 'Email',
            disabled: false,
            href: '#',
            logo: emailLogo,
            fields: withEmail ? emailFields : null,
            onClick: (event: MouseEvent) => {
                event.preventDefault();
                setWithEmail(!withEmail);
            }
        },
    ];

    const handleSignIn = (formData: FormData) => {
        const requestBody = convertFormDataToObject(formData);
        if (
            typeof requestBody.username !== 'string' ||
            typeof requestBody.password !== 'string'
        ) {
            console.warn('Invalid sign body data');
            return;
        }
        fetchData({
            username: requestBody.username,
            password: requestBody.password,
        });
    };

    return (
        <div className='page-sign'>
            <div className='page-sign-header'>
                <PageLogo/>
                <h1>SORFACE</h1>
            </div>
            <FormWrapper>
                <div className="signin-container">
                    <h2>{Captions.WelcomeToSSO}</h2>
                    {signinItems.map(signinItem => (
                        <Fragment key={signinItem.name}>
                            {signinItem.name === 'Email' && (
                                <div className='signin-emailSplit-container'>
                                    <div className='signin-emailSplit'></div>
                                    <div className='signin-emailSplitCaption'>Or</div>
                                    <div className='signin-emailSplit'></div>
                                </div>
                            )}
                            {signinItem.fields ? (
                                <>
                                    <Form
                                        htmlMethod='POST'
                                        htmlAction={ApiEndpoint.AccountsSignin}
                                        skipCsrf={true}
                                        fields={signinItem.fields}
                                        fieldErrors={queryFromErrors}
                                        submitCaption={Captions.SignIn}
                                        onSubmit={handleSignIn}
                                    />
                                    <Link className="signUp-link" to={pathnames.signUp}>{Captions.SignUpLink}</Link>
                                </>
                            ) : (
                                <a
                                    className="signin-link"
                                    href={REACT_APP_BACKEND_URL + signinItem.href}
                                    onClick={signinItem.onClick}
                                >
                                    <button className='signin-button' disabled={signinItem.disabled}>
                                        <img
                                            className="signin-logo"
                                            src={signinItem.logo}
                                            alt={`${signinItem.name} ${Captions.LogoAlt}`}
                                        />
                                        <span className='signin-caption'>
                                            {Captions.ContinueWith} {signinItem.name}
                                        </span>
                                    </button>
                                </a>
                            )}
                        </Fragment>
                    ))}
                </div>
            </FormWrapper>
        </div>
    );
};

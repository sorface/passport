import { FunctionComponent } from 'react';
import { Link } from 'react-router-dom';
import { Captions, pathnames } from '../../constants';
import { Field, Form } from '../../components/Form/Form';
import { FormWrapper } from '../../components/Form/FormWrapper';
import { ApiEndpoint } from '../../types/apiContracts';

const fields: Field[] = [
    {
        name: 'username',
        placeholder: Captions.Signin,
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
        placeholder: Captions.EmailOrUsername,
        required: true,
    },
    {
        name: 'firstName',
        placeholder: Captions.FirstName,
        required: true,
    },
    {
        name: 'lastName',
        placeholder: Captions.LastName,
        required: true,
    },
];

export const SignUp: FunctionComponent = () => {
    return (
        <div>
            <FormWrapper>
                <Form
                    htmlMethod='POST'
                    htmlAction={ApiEndpoint.AccountsSignup}
                    fields={fields}
                    fieldErrors={{}}
                    submitCaption={Captions.SignUp}
                >
                    <Link to={pathnames.signIn}>{Captions.SignInLink}</Link>
                </Form>
            </FormWrapper>
        </div>
    );
};

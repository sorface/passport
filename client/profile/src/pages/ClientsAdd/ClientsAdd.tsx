import {FunctionComponent} from 'react';
import {Field, Form} from "../../components/Form/Form";
import {useApiMethodCsrf} from '../../hooks/useApiMethodCsrf';
import {appsApiDeclaration, CreateAppBody} from '../../apiDeclarations';
import {Captions} from '../../constants';
import {Loader} from "../../components/Loader/Loader";
import {Client} from "../../types/client";

const nameField = 'name';
const redirectionUrlsField = 'redirectionUrls';

const fields: Field[] = [
    {
        name: nameField,
        placeholder: Captions.ClientName,
    },
    {
        name: redirectionUrlsField,
        placeholder: Captions.RedirectionUrls,
    },
];

export const ClientsAdd: FunctionComponent = () => {
    const {fetchData, apiMethodState} = useApiMethodCsrf<Client, CreateAppBody>(appsApiDeclaration.create);

    const {
        data
    } = apiMethodState;

    const handleSubmit = (formData: FormData) => {
        const data = {
            [nameField]: String(formData.get(nameField)),
            [redirectionUrlsField]: [String(formData.get(redirectionUrlsField))],
        };
        fetchData(data);
    };

    const resultFields: Field[] = [
        {
            name: 'clientSecret',
            placeholder: Captions.ClientSecretLabel,
            readOnly: true,
            defaultValue: data?.clientSecret,
        },
        {
            name: 'id',
            placeholder: Captions.ClientTechId,
            readOnly: true,
            defaultValue: data?.id,
        },
        {
            name: 'clientId',
            placeholder: Captions.Id,
            readOnly: true,
            defaultValue: data?.clientId,
        },
        {
            name: 'nameField',
            placeholder: Captions.ClientName,
            defaultValue: data?.clientName,
        },
        {
            name: 'redirectUrls',
            placeholder: Captions.RedirectionUrls,
            defaultValue: data?.redirectUrls,
        },
        {
            name: 'issueTime',
            placeholder: Captions.IssueTime,
            readOnly: true,
            defaultValue: data?.issueTime && new Date(data.issueTime).toLocaleString(),
        },
        {
            name: 'expiresAt',
            placeholder: Captions.ExpiresAt,
            readOnly: true,
            defaultValue: data?.expiresAt && new Date(data.expiresAt).toLocaleString(),
        },
    ];

    return (
        <div>
            <h3>{Captions.AddClient}</h3>
            <Form
                fields={fields}
                fieldErrors={{}}
                submitCaption={Captions.AddClient}
                onSubmit={handleSubmit}
            />
            {!!data && (
                <Form
                    styled
                    fields={resultFields}
                    fieldErrors={{}}
                />)
            }
            {
                apiMethodState.process.loading ? (<Loader/>) : (<></>)
            }
        </div>
    );
};

import {FunctionComponent, MouseEvent, useEffect} from 'react';
import {useParams} from 'react-router-dom';
import {useApiMethod} from "../../hooks/useApiMethod";
import {appsApiDeclaration, EditAppBody} from "../../apiDeclarations";
import {ClientApp} from "../Clients/Clients";
import {Captions} from '../../constants';
import {Field, Form} from '../../components/Form/Form';
import {Loader} from '../../components/Loader/Loader';
import {RenewSecret} from './components/RenewSecret/RenewSecret';

import './ClientsEdit.css';
import {useApiMethodCsrf} from "../../hooks/useApiMethodCsrf";

const nameField = 'name';
const redirectionUrlsField = 'redirectUrls';

export const ClientsEdit: FunctionComponent = () => {
    const {
        apiMethodState,
        fetchData,
    } = useApiMethod<ClientApp, string>(appsApiDeclaration.getById);

    const {
        data,
        process: { loading, error },
    } = apiMethodState;

    const {
        apiMethodState: editApiMethodState,
        fetchData: editFetch,
    } = useApiMethodCsrf<ClientApp, EditAppBody>(appsApiDeclaration.edit);

    const {
        data: editedApp,
        process: { loading: editLoading, error: editError },
    } = editApiMethodState;

    const {
        apiMethodState: deleteApiMethodState,
        fetchData: deleteFetch,
    } = useApiMethod<unknown, string>(appsApiDeclaration.deleteById);

    const {
        data: deletedApp,
        process: { loading: deleteLoading, error: deleteError },
    } = deleteApiMethodState;

    const loadintTotal = loading || editLoading || deleteLoading;
    const errorTotal = error || editError || deleteError;

    const { id } = useParams();

    const fields: Field[] = [
        {
            name: 'clientTechId',
            placeholder: Captions.ClientTechId,
            readOnly: true,
            defaultValue: data?.id,
        },
        {
            name: 'id',
            placeholder: Captions.Id,
            readOnly: true,
            defaultValue: data?.clientId,
        },
        {
            name: nameField,
            placeholder: Captions.ClientName,
            defaultValue: data?.clientName,
        },
        {
            name: redirectionUrlsField,
            placeholder: Captions.RedirectionUrls,
            defaultValue: data?.redirectUrls.join(","),
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

    useEffect(() => {
        if (!id) {
            return;
        }

        fetchData(id);
    }, [id, editedApp, deletedApp, fetchData]);

    const handleSubmit = (data: FormData) => {
        if (!id) {
            throw new Error('App id not found');
        }
        editFetch({
            id,
            name: String(data.get(nameField)),
            redirectionUrls: [String(data.get(redirectionUrlsField))],
        });
    };

    const handleDelete = (event: MouseEvent) => {
        event.preventDefault();
        if (!id) {
            throw new Error('App id not found');
        }
        deleteFetch(id);
    };

    return (
        <div className='client-edit'>
            <h3>{Captions.EditClient}</h3>
            <div className='client-edit-body'>
                {loadintTotal && <Loader />}
                {!!errorTotal && <div>{Captions.Error}: {errorTotal}</div>}
                {!!data && (
                    <Form
                        styled
                        fields={fields}
                        fieldErrors={{}}
                        submitCaption={Captions.Save}
                        onSubmit={handleSubmit}
                    >
                        <>
                            <button className='danger' onClick={handleDelete}>{Captions.Delete}</button>
                            <RenewSecret clientId={data?.id}/>
                        </>
                    </Form>
                )}
            </div>
        </div>
    );
};

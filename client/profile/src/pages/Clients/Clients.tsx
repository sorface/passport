import {FunctionComponent, useEffect} from 'react';
import {Link} from 'react-router-dom';
import {Icon} from '../../components/Icon/Icon';
import {IconNames, pathnames} from '../../constants';

import './Clients.css';
import {ApiMethodState, useApiMethod} from "../../hooks/useApiMethod";
import {appsApiDeclaration} from "../../apiDeclarations";
import {useApiMethodCsrf} from "../../hooks/useApiMethodCsrf";

export interface ClientApp {
    id: string;
    clientId: string;
    clientName: string;
    issueTime: Date;
    expiresAt: Date;
    redirectUrls: string[];
}

export const Clients: FunctionComponent = () => {
    const {
        apiMethodState,
        fetchData
    } = useApiMethod<ClientApp[], undefined>(appsApiDeclaration.getMyApps);

    const {
        apiMethodState: deleteApiMethodState,
        fetchData: deleteFetch,
    } = useApiMethodCsrf<ApiMethodState, string>(appsApiDeclaration.deleteById);

    const {
        data
    } = apiMethodState;

    useEffect(() => {
        fetchData(undefined);
    }, [fetchData]);

    useEffect(() => {
        if (!data) {
            return;
        }
    }, [data]);

    useEffect(() => {
        if (deleteApiMethodState.process.code !== 200) {
            return;
        }

        fetchData(undefined);
    }, [deleteApiMethodState.process.code, fetchData]);

    const handleDelete = (id: string) => () => {
        deleteFetch(id);
    }

    const createAppItem = (app: ClientApp) => (
        <tr key={app.id}>
            <td>{app.id}</td>
            <td>{app.clientName}</td>
            <td>
                <Link to={pathnames.clientsEdit.replace(':id', app.id)}>
                    <button><Icon name={IconNames.Create}/></button>
                </Link>
            </td>
            <td>
                <button className='remove' onClick={handleDelete(app.id)}>
                    <Icon name={IconNames.Remove}/>
                </button>
            </td>
        </tr>
    );

    return (
        <div className='clients'>
            <h3>Clients</h3>
            <table>
                <thead>
                <tr>
                    <th>Id</th>
                    <th>App Name</th>
                    <th className='tight'></th>
                    <th className='tight'>
                        <Link to={pathnames.clientsAdd}>
                            <button className='add'><Icon name={IconNames.Add}/></button>
                        </Link>
                    </th>
                </tr>
                </thead>
                <tbody>
                {data ? Array.from(data).map(createAppItem) : []}
                </tbody>
            </table>
        </div>
    );
};

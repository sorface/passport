import {ApiContractDelete, ApiContractGet, ApiContractPatch, ApiContractPost, ApiEndpoint} from './types/apiContracts';

export type SignUpBody = Record<string, FormDataEntryValue>;

export interface ConfirmBody {
    token: string;
}

export const csrfApiDeclaration = {
    csrf: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: ApiEndpoint.Csrf,
    })
};

export interface CreateAppBody {
    name: string;
    redirectionUrls: string[];
}

export interface EditAppBody extends CreateAppBody {
    id: string;
}

export interface DeleteAppBody {
    id: string;
}

export interface RefreshAppParams {
    clientId: string;
}

export interface RefreshSecretResponse {
    clientSecret: string;
    expiresAt: Date;
}

export const appsApiDeclaration = {
    getMyApps: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: ApiEndpoint.Apps,
    }),
    getById: (id: string): ApiContractGet => ({
        method: 'GET',
        baseUrl: ApiEndpoint.GetAppById.replace(':id', id) as ApiEndpoint,
    }),
    deleteById: (id: string): ApiContractDelete => ({
        method: 'DELETE',
        baseUrl: ApiEndpoint.DeleteAppById.replace(':id', id) as ApiEndpoint,
    }),
    create: (body: CreateAppBody): ApiContractPost => ({
        method: 'POST',
        baseUrl: ApiEndpoint.Apps,
        body,
    }),
    edit: (body: EditAppBody): ApiContractPatch => ({
        method: 'PATCH',
        baseUrl: ApiEndpoint.GetAppById.replace(':id', body.id) as ApiEndpoint,
        body: { ...body, id: undefined },
    }),
    refresh: (params: RefreshAppParams): ApiContractPatch => ({
        method: 'PATCH',
        baseUrl: ApiEndpoint.RefreshApp.replace(':clientId', params.clientId) as ApiEndpoint,
        body: undefined,
    }),
};

export interface EditAccountBody {
    id: string;
    firstName?: string;
    lastName?: string;
}

export const accountsApiDeclaration = {
    signup: (body: SignUpBody): ApiContractPost => ({
        method: 'POST',
        baseUrl: ApiEndpoint.AccountsSignup,
        body,
    }),
    current: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: ApiEndpoint.AccountsCurrent,
    }),
    confirm: (body: ConfirmBody): ApiContractPost => ({
        method: 'POST',
        baseUrl: ApiEndpoint.AccountsConfirm,
        body,
    }),
    getCurrentSession: (): ApiContractGet => ({
        method: 'GET',
        baseUrl: ApiEndpoint.CurrentSession,
    }),
    edit: (body: EditAccountBody): ApiContractPatch => ({
        method: 'PATCH',
        baseUrl: ApiEndpoint.AccountsEdit.replace(':id', body.id) as ApiEndpoint,
        body: {
            ...body,
            id: undefined,
        },
    }),
    logout: (): ApiContractPost => ({
        method: 'POST',
        baseUrl: ApiEndpoint.AccountsLogout,
        body: ''
    })
};

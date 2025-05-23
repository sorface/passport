export enum ApiEndpoint {
    Csrf = '/csrf',
    Apps = '/api/applications',
    GetAppById = '/api/applications/:id',
    DeleteAppById = '/api/applications/:id',
    RefreshApp = '/api/applications/:clientId/refresh',
    AccountsSignup = '/api/registrations',
    RegistrationGet = '/api/registrations',
    AccountsSignin = '/api/login',
    AccountsCurrent = '/api/accounts/current',
    AccountsConfirm = '/api/registrations/confirm',
    AccountsEdit = '/api/accounts/:id',
    AccountsLogout = '/api/accounts/logout',
    CurrentSession = '/api/sessions',
    OtpResend = '/api/registrations/otp',
}

export interface ApiContractGet {
    method: 'GET';
    baseUrl: ApiEndpoint;
    urlParams?: object;
}

export interface ApiContractPost {
    method: 'POST';
    baseUrl: ApiEndpoint;
    urlParams?: object;
    body: any;
}

export interface ApiContractPut {
    method: 'PUT';
    baseUrl: ApiEndpoint;
    urlParams?: object;
    body?: any;
}

export interface ApiContractPatch {
    method: 'PATCH';
    baseUrl: ApiEndpoint;
    urlParams?: object;
    body: any;
}

export interface ApiContractDelete {
    method: 'DELETE';
    baseUrl: ApiEndpoint;
    urlParams?: object;
    body?: object;
}

export type ApiContract = ApiContractGet | ApiContractPost | ApiContractPut | ApiContractPatch | ApiContractDelete;

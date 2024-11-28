import { useCallback, useReducer } from 'react';
import { REACT_APP_BACKEND_URL } from '../config';
import { ApiContract } from '../types/apiContracts';

export interface ApiMethodState<ResponseData = any> {
    process: {
        loading: boolean;
        code: number | null;
        error: string | null;
    };
    data: ResponseData | null;
}

const initialState: ApiMethodState = {
    process: {
        loading: false,
        code: null,
        error: null,
    },
    data: null,
};

type ApiMethodAction = {
    name: 'startLoad';
} | {
    name: 'setData';
    payload: any;
} | {
    name: 'setError';
    payload: string;
} | {
    name: 'setCode';
    payload: number;
};

const apiMethodReducer = (state: ApiMethodState, action: ApiMethodAction): ApiMethodState => {
    switch (action.name) {
        case 'startLoad':
            return {
                process: {
                    loading: true,
                    error: null,
                    code: null,
                },
                data: null,
            };
        case 'setError':
            return {
                ...state,
                process: {
                    ...state.process,
                    loading: false,
                    error: action.payload
                }
            };
        case 'setData':
            return {
                process: {
                    ...state.process,
                    loading: false,
                    error: null,
                },
                data: action.payload
            };
        case 'setCode':
            return {
                ...state,
                process: {
                    ...state.process,
                    code: action.payload,
                },
            };
        default:
            return state;
    }
};
const createUrlParam = (name: string, value: string) =>
    `${encodeURIComponent(name)}=${encodeURIComponent(value)}`;

const createFetchUrl = (apiContract: ApiContract, additionalUrlParams?: object) => {
    const urlParams: object = {
        ...apiContract.urlParams,
        ...additionalUrlParams,
    };
    if (Object.keys(urlParams).length) {
        const params =
            Object.entries(urlParams)
                .map(([paramName, paramValue]) => {
                    if (Array.isArray(paramValue)) {
                        return paramValue.map(val => createUrlParam(paramName, val)).join('&');
                    }
                    return createUrlParam(paramName, paramValue);
                })
                .join('&');
        return `${REACT_APP_BACKEND_URL}${apiContract.baseUrl}?${params}`;
    }
    return `${REACT_APP_BACKEND_URL}${apiContract.baseUrl}`;
};

const createFetchRequestInit = (apiContract: ApiContract): RequestInit => {
    const defaultRequestInit: RequestInit = {
        credentials: 'include',
        method: apiContract.method,
    };

    if (apiContract.method === 'GET') {
        return defaultRequestInit;
    }

    const { body } = apiContract;

    const headers = new Headers();
    headers.append("Content-Type", 'application/json;charset=UTF-8')

    return {
        ...defaultRequestInit,
        body: body instanceof FormData ? body : JSON.stringify(body),
        headers
    } as RequestInit;
};

export type AnyObject = Record<string, any>;

const getResponseContent = async (response: Response): Promise<AnyObject | string> => {
    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/json')) {
        return await response.text();
    }
    return await response.json();
};

const getResponseError = (
    response: Response,
    responseContent: AnyObject | string,
    apiContract: ApiContract
) => {
    if (
        typeof responseContent === 'string' ||
        !responseContent.message
    ) {
        return `${apiContract.method} ${apiContract.baseUrl} ${response.status}`;
    }
    return responseContent.message;
}

export const useApiMethod = <ResponseData, RequestData = AnyObject>(apiContractCall: (data: RequestData) => ApiContract) => {
    const [apiMethodState, dispatch] = useReducer(apiMethodReducer, initialState);

    const fetchData = useCallback(async (requestData: RequestData, additionalUrlParams?: object) => {
        dispatch({ name: 'startLoad' });
        const apiContract = apiContractCall(requestData);
        try {
            const response = await fetch(
                createFetchUrl(apiContract, additionalUrlParams),
                createFetchRequestInit(apiContract),
            );
            dispatch({
                name: 'setCode',
                payload: response.status,
            });

            const responseData = await getResponseContent(response);
            if (!response.ok) {
                const errorMessage = getResponseError(response, responseData, apiContract);
                throw new Error(errorMessage);
            }
            dispatch({ name: 'setData', payload: responseData });
        } catch (err: any) {
            dispatch({
                name: 'setError',
                payload: err.message || `Failed to fetch ${apiContract.method} ${apiContract.baseUrl}`,
            });
        }
    }, [apiContractCall]);

    return {
        apiMethodState: apiMethodState as ApiMethodState<ResponseData>,
        fetchData,
    };
};

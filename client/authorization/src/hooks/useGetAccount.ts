import {useCallback, useReducer} from 'react';
import {REACT_APP_BACKEND_URL} from '../config';
import {Account} from '../types/account';

interface GetMeState {
    process: {
        loading: boolean;
        error: string | null;
    };
    account: Account | null;
}

const initialState: GetMeState = {
    process: {
        loading: false,
        error: null,
    },
    account: null,
};

type GetMeAction = {
    name: 'startLoad';
} | {
    name: 'setAccount';
    payload: Account;
} | {
    name: 'setError';
    payload: string;
};

const getMeReducer = (state: GetMeState, action: GetMeAction): GetMeState => {
    switch (action.name) {
        case 'startLoad':
            return {
                process: {
                    loading: true,
                    error: null,
                },
                account: null,
            };
        case 'setError':
            return {
                ...state,
                process: {
                    loading: false,
                    error: action.payload
                }
            };
        case 'setAccount':
            return {
                process: {
                    loading: false,
                    error: null,
                },
                account: action.payload
            };
        default:
            return state;
    }
};

export const useGetAccountApi = () => {
    const [accountState, dispatch] = useReducer(getMeReducer, initialState);

    const loadAccount = useCallback(async () => {
        dispatch({name: 'startLoad'});

        const headers = new Headers();

        try {
            const response = await fetch(`${REACT_APP_BACKEND_URL}/api/accounts/current`, {
                headers,
                credentials: 'include'
            });
            if (!response.ok) {
                throw new Error('UserApi error');
            }
            const responseJson = await response.json();
            dispatch({name: 'setAccount', payload: responseJson});
        } catch (err: any) {
            dispatch({
                name: 'setError',
                payload: err.message || 'Failed to get me',
            });
        }
    }, []);

    return {
        accountState,
        loadAccount,
    };
};

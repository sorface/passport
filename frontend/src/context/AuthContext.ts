import React from 'react';
import {Account} from '../types/account';

type AuthContextValue = { account: Account | null, loadAccount: () => Promise<void> };

export const AuthContext = React.createContext<AuthContextValue>({
    account: null,
    loadAccount: async () => {
    },
});

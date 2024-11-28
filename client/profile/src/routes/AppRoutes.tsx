import React, {FunctionComponent, useContext} from 'react';
import {Navigate, Route, Routes} from 'react-router-dom';
import {AuthContext} from '../context/AuthContext';
import {pathnames} from '../constants';
import {AccountPage} from '../pages/Account/AccountPage';
import {NotFound} from '../pages/NotFound/NotFound';
import {Session} from "../pages/Session/Session";
import {ProtectedRoute} from './ProtectedRoute';
import {Clients} from '../pages/Clients/Clients';
import {ClientsEdit} from '../pages/ClientsEdit/ClientsEdit';
import {ClientsAdd} from '../pages/ClientsAdd/ClientsAdd';

export const AppRoutes: FunctionComponent = () => {
    const { account } = useContext(AuthContext);
    const authenticated = !!account;
    const authenticatedRouteProps = {
        allowed: authenticated,
        redirect: pathnames.signIn,
    };

    return (
        <Routes>
            <Route path={pathnames.account}
                   element={
                       <ProtectedRoute {...authenticatedRouteProps}>
                           <AccountPage/>
                       </ProtectedRoute>
                   }
            />
            <Route path={pathnames.session}
                   element={
                       <ProtectedRoute {...authenticatedRouteProps}>
                           <Session/>
                       </ProtectedRoute>
                   }
            />
            <Route path={pathnames.clients}
                   element={
                       <ProtectedRoute {...authenticatedRouteProps}>
                           <Clients/>
                       </ProtectedRoute>
                   }
            />
            <Route path={pathnames.clientsAdd}
                   element={
                       <ProtectedRoute {...authenticatedRouteProps}>
                           <ClientsAdd/>
                       </ProtectedRoute>
                   }
            />
            <Route path={pathnames.clientsEdit}
                   element={
                       <ProtectedRoute {...authenticatedRouteProps}>
                           <ClientsEdit/>
                       </ProtectedRoute>
                   }
            />
            <Route path={'/'} element={
                <Navigate to={pathnames.account} replace/>
            }/>
            <Route path="*" element={<NotFound/>}/>
        </Routes>
    );
};

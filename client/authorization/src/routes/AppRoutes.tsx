import React, {FunctionComponent, useContext} from 'react';
import {Navigate, Route, Routes} from 'react-router-dom';
import {AuthContext} from '../context/AuthContext';
import {pathnames} from '../constants';
import {NotFound} from '../pages/NotFound/NotFound';
import {SignIn} from '../pages/SignIn/SignIn';
import {Activate} from '../pages/Activate/Activate';
import {ProtectedRoute} from './ProtectedRoute';
import {Failure} from "../pages/Failure/Failure";

export const AppRoutes: FunctionComponent = () => {
    const { account } = useContext(AuthContext);
    const authenticated = !!account;
    const authenticatedRouteProps = {
        allowed: authenticated,
        redirect: pathnames.signIn,
    };
    const notAuthenticatedRouteProps = {
        allowed: !authenticated,
        redirect: pathnames.account,
    };

    return (
        <Routes>
            {/*<Route path={pathnames.signUp} element={<SignUp/>}/>*/}
            <Route path={pathnames.activate}
                   element={
                       <ProtectedRoute {...authenticatedRouteProps}>
                           <Activate/>
                       </ProtectedRoute>
                   }
            />
            <Route path={pathnames.signIn}
                   element={
                       <ProtectedRoute {...notAuthenticatedRouteProps}>
                           <SignIn/>
                       </ProtectedRoute>
                   }
            />
            <Route path={pathnames.failure} element={<Failure/>}/>
            <Route path={'/'} element={
                <Navigate to={pathnames.account} replace/>
            }/>
            <Route path="*" element={<NotFound/>}/>
        </Routes>
    );
};

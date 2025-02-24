import {FunctionComponent, ReactElement} from 'react';
import {RouteProps} from 'react-router-dom';

type PrivateRouteProps = RouteProps & {
    allowed: boolean;
    redirect: string;
    children: ReactElement<any, any> | null;
};

export const ProtectedRoute: FunctionComponent<PrivateRouteProps> = ({
                                                                         allowed,
                                                                         redirect,
                                                                         children,
                                                                     }) => {
    if (!allowed) {
        window.location.href = redirect

        return null;
    }

    return children;
};

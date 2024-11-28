import React, { FunctionComponent, ReactElement } from 'react';
import { RouteProps, Navigate } from 'react-router-dom';

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
    return <Navigate to={redirect} replace />;
  }

  return children;
};

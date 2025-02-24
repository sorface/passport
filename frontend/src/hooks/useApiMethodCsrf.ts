import {useCallback, useEffect, useState} from 'react';
import {ApiContract} from '../types/apiContracts'
import {AnyObject, ApiMethodState, useApiMethod} from './useApiMethod'
import {useCsrfApi} from './useGetCsrf';

export const useApiMethodCsrf = <ResponseData, RequestData = AnyObject>(apiContractCall: (data: RequestData) => ApiContract) => {
    const {
        apiMethodState: apiMethodStateInternal,
        fetchData: fetchDataInternal
    } = useApiMethod<ResponseData, RequestData>(apiContractCall);
    const {csrfConfigState, loadCsrfConfig} = useCsrfApi();
    const {process: {csrfConfigLoading, csrfConfigError}, csrfConfig} = csrfConfigState;
    const [lastRequestData, setLastRequestData] = useState<RequestData | null>(null);

    const apiMethodState: ApiMethodState<ResponseData> = {
        data: apiMethodStateInternal.data,
        process: {
            code: apiMethodStateInternal.process.code,
            error: apiMethodStateInternal.process.error || csrfConfigError,
            loading: apiMethodStateInternal.process.loading || csrfConfigLoading,
        },
    };

    const fetchData: typeof fetchDataInternal = useCallback(async (requestData) => {
        setLastRequestData(requestData);
        loadCsrfConfig();
    }, [loadCsrfConfig]);

    useEffect(() => {
        if (!csrfConfig || lastRequestData === null) {
            return;
        }
        const csrfParams = {
            [csrfConfig.parameterName]: csrfConfig.token,
        };
        fetchDataInternal(lastRequestData, csrfParams);
    }, [csrfConfig, lastRequestData, fetchDataInternal]);

    return {
        fetchData,
        apiMethodState,
    }
};

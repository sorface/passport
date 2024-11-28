import { DetailedHTMLProps, FormEvent, FunctionComponent, InputHTMLAttributes, ReactNode, useEffect, useRef, useState } from 'react';
import { Captions } from '../../constants';
import { FormFields } from './FormFields';
import { useCsrfApi } from '../../hooks/useGetCsrf';
import { ApiEndpoint } from '../../types/apiContracts';
import { REACT_APP_BACKEND_URL } from '../../config';

export type FieldErrors = Record<string, string>;

export interface Field extends DetailedHTMLProps<InputHTMLAttributes<HTMLInputElement>, HTMLInputElement> {
    name: string;
}

export interface FormProps {
    fields: Field[];
    fieldErrors: FieldErrors;
    htmlAction?: ApiEndpoint;
    htmlMethod?: string;
    skipCsrf?: boolean;
    className?: string;
    styled?: boolean;
    loading?: boolean;
    error?: string | null;
    submitCaption?: string;
    children?: ReactNode;
    onSubmit?: (formData: FormData) => void;
}

export const Form: FunctionComponent<FormProps> = ({
    fields,
    fieldErrors,
    htmlAction,
    htmlMethod,
    skipCsrf,
    className,
    styled,
    loading,
    error: propsError,
    submitCaption,
    children,
    onSubmit,
}) => {
    const { loadCsrfConfig, csrfConfigState } = useCsrfApi();
    const { csrfConfig, process: { csrfConfigError } } = csrfConfigState;
    const [needRenderCsrf, setNeedRenderCsrf] = useState(false);
    const formRef = useRef<HTMLFormElement | null>(null);
    const error = propsError || csrfConfigError;

    if (needRenderCsrf && csrfConfig && formRef) {
        formRef.current?.submit();
    }

    useEffect(() => {
        if (!csrfConfig) {
            return;
        }
        setNeedRenderCsrf(true);
    }, [csrfConfig]);

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        if (onSubmit) {
            event.preventDefault();
            const formData = new FormData(event.target as HTMLFormElement);
            onSubmit(formData);
            return;
        }
        if (skipCsrf) {
            return true;
        }
        event.preventDefault();
        loadCsrfConfig();
    };

    return (
        <form
            ref={formRef}
            action={REACT_APP_BACKEND_URL + htmlAction}
            method={htmlMethod}
            className={`${styled ? 'form' : ''} ${className}`}
            onSubmit={csrfConfig ? undefined : handleSubmit}
        >
            <FormFields fields={fields} fieldErrors={fieldErrors} />
            {!skipCsrf && (
                <input type='hidden' name={csrfConfig?.parameterName} value={csrfConfig?.token} />
            )}
            {submitCaption && <input type="submit" value={submitCaption} />}
            <div className='form-status'>
                {loading && <span>{Captions.Loading}...</span>}
                {error && <span className='form-field-error'>{Captions.Error}: {error}</span>}
            </div>
            {children && <div className='form-additional-content'>{children}</div>}
        </form>
    );
};

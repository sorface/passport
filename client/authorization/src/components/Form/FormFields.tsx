import {FunctionComponent} from 'react';
import {FormProps} from './Form';
import {Captions} from '../../constants';

const errorLocalization: Record<string, string> = {
    invalidEmail: Captions.InvalidEmailOrUsername,
    invalidPassword: Captions.InvalidPassword,
    unknownError: Captions.UnknownError,
};

type FormFieldsProps = Pick<FormProps, 'fields' | 'fieldErrors'>;

export const FormFields: FunctionComponent<FormFieldsProps> = ({
    fields,
    fieldErrors,
}) => {
    return (
        <>
            {fields.map(field => (
                <div key={field.name} className='form-field-wrapper'>
                    <label htmlFor={field.name}>{field.placeholder}:</label>
                    <input id={field.name} {...field} />
                    {!!fieldErrors[field.name] && (
                        <div className='form-field-error'>
                            {errorLocalization[fieldErrors[field.name]] || errorLocalization.unknownError}
                        </div>
                    )}
                </div>
            ))}
        </>
    );
};

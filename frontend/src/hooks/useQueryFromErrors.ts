import {useEffect, useState} from 'react';
import {useSearchParams} from 'react-router-dom';
import {FieldErrors} from '../components/Form/Form';

const formErrorParamPostfix = '_error';
const formErrorParamRegexp = new RegExp(`.+${formErrorParamPostfix}$`);

export const useQueryFromErrors = () => {
    const [searchParams] = useSearchParams();
    const [queryFromErrors, setQueryFromErrors] = useState<FieldErrors>({});

    useEffect(() => {
        const currentQueryFromErrors: FieldErrors = {};
        for (let [name, value] of searchParams.entries()) {
            const formErrorParam = formErrorParamRegexp.test(name);
            if (!formErrorParam) {
                continue;
            }
            currentQueryFromErrors[name.replace(formErrorParamPostfix, '')] = value;
        }
        setQueryFromErrors(currentQueryFromErrors);
    }, [searchParams]);

    return {
        queryFromErrors,
    };
};

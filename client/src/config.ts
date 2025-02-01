const getFromEnv = (varName: string) => {
    const value = import.meta.env && import.meta.env[varName];
    if (!value) {
        throw new Error(`import.meta.env.${varName} are not defined`);
    }
    return value;
};

export const VITE_BACKEND_URL = getFromEnv('VITE_BACKEND_URL');

export const VITE_BUILD_HASH = getFromEnv('VITE_BUILD_HASH');

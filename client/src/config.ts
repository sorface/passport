const getFromEnv = (varName: string) => {
    const value = process.env && process.env[varName];
    if (!value) {
        throw new Error(`process.env.${varName} are not defined`);
    }
    return value;
};

export const VITE_BACKEND_URL = getFromEnv('VITE_BACKEND_URL');

export const VITE_BUILD_HASH = getFromEnv('VITE_BUILD_HASH');

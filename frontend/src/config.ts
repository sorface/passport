const getFromEnv = (varName: string) => {
    const value = process.env && process.env[varName];
    if (!value) {
        throw new Error(`process.env.${varName} are not defined`);
    }
    return value;
};

export const REACT_APP_BACKEND_URL = getFromEnv('REACT_APP_BACKEND_URL');

export const REACT_APP_BUILD_HASH = getFromEnv('REACT_APP_BUILD_HASH');

export const REDIRECT_PROFILE_PAGE = getFromEnv('REACT_APP_PROFILE_PAGE');
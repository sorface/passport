export const convertFormDataToObject = (formData: FormData) => {
    const object: Record<string, FormDataEntryValue> = {};
    formData.forEach((value, key) => object[key] = value);
    return object;
};

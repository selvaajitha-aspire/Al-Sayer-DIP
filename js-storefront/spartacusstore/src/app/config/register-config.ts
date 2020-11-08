export const registerOccConfig = {
    backend: {
        occ: {
            endpoints: {
                getEccCustomer: 'register/getCustomerDetails/{id}?fields=FULL',
                registerCustomer: 'register/createUser',
                sendOTP: 'register/sendOTP/{id}',
            },
        },
    },
};

//export const siteKey="6LcOuyYTAAAAAHTjFuqhA52fmfJ_j5iFk5PsfXaU";
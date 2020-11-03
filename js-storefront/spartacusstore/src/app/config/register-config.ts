export const registerOccConfig = {
    backend: {
        occ: {
            endpoints: {
                getEccCustomer: 'register/getCustomerDetails/{id}/?fields=FULL',
                registerCustomer: 'register/createUser',
            },
        },
    },
};
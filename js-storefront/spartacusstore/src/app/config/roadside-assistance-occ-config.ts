export const rsaOccConfig = {
    backend: {
        occ: {
            endpoints: {
                saveItems: 'rsa/save-details',
                getItems: 'my-vehicles/get-vehicles/?fields=FULL',
                getDriverDetails: 'rsa/get-driver'
            },
        },
    },
};

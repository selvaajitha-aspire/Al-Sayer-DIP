export const rsaOccConfig = {
    backend: {
        occ: {
            endpoints: {
                saveItems: 'rsa/saveRSADetails',
                getItems: 'myVehicles/getVehicles/?fields=FULL',
                getDriverDetails: 'rsa/getDriver',
            },
        },
    },
};

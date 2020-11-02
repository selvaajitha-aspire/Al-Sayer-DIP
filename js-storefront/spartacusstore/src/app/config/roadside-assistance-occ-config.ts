export const rsaOccConfig = {
    backend: {
        occ: {
            endpoints: {
                saveItems: 'rsa/saveDetails',
                getItems: 'myVehicles/getVehicles/?fields=FULL',
                getDriverDetails: 'rsa/getDriver',
            },
        },
    },
};

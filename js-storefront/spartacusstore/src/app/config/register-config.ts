export const registerOccConfig = {
    backend: {
        occ: {
            endpoints: {
                getEccCustomer: 'register/getCustomerDetails/',
                registerCustomer: 'register/createUser',
                sendOTP: 'register/sendOTP',
                validateOTP: 'register/validateOTP',
                getComponents: 'cms/components',
                getServiceHistory: 'service-history/getMyServiceHistoryByChassisNo/',
                getInsurances: 'insurance/getInsurances',
                updateProfilePhoto: 'register/updateProfilePhoto'
            },
            
        },
    },
};


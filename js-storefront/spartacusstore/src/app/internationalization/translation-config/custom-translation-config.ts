// It is used to map group in a page

interface TranslationChunksConfig {
    [chunk: string]: string[];
  }
  
  export const customTranslationConfig: TranslationChunksConfig = {
    common: [
      'common',
      'spinner',
      'searchBox',
      'navigation',
      'sorting',
      'httpHandlers',
      'pageMetaResolver',
      'miniCart',
      'miniLogin',
      'skipLink',
      'formErrors',
    ],
    cart: [
      'cartDetails',
      'cartItems',
      'orderCost',
      'voucher',
      'wishList',
      'saveForLaterItems',
    ],
    address: ['addressForm', 'addressBook', 'addressCard'],
    payment: ['paymentForm', 'paymentMethods', 'paymentCard'],
    myAccount: [
      'orderDetails',
      'orderHistory',
      'closeAccount',
      'updateEmailForm',
      'updatePasswordForm',
      'updateProfileForm',
      'consentManagementForm',
      'myCoupons',
      'wishlist',
      'notificationPreference',
      'myInterests',
      'AccountOrderHistoryTabContainer',
      'returnRequestList',
      'returnRequest',
    ],
    storeFinder: ['storeFinder'],
    pwa: ['pwa'],
    checkout: [
      'checkout',
      'checkoutAddress',
      'checkoutOrderConfirmation',
      'checkoutReview',
      'checkoutShipping',
      'checkoutProgress',
    ],
    product: [
      'productDetails',
      'productList',
      'productFacetNavigation',
      'productSummary',
      'productReview',
      'addToCart',
      'addToWishList',
      'CMSTabParagraphContainer',
      'variant',
      'stockNotification',
      'TabPanelContainer',
    ],
    user: [
      'anonymousConsents',
      'forgottenPassword',
      'loginForm',
      'register',
      'checkoutLogin',
    ]

  }
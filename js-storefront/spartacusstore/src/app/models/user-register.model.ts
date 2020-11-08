export interface UserRegister {
    eccCustId?:string;
    civilId?:number;
    name?: string;
    arabicName?: string;
    password?: string;
    mobile?:number;
    titleCode?: string;
    uid?: string;
    oneTimePassword?:number;
}
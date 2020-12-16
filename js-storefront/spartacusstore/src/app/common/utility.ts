export function isEmpty(val){
    return (val === undefined || val == null || val==='' || val.length <= 0) ;
}

export function setFormField(formObject,formField,value){
    if(!isEmpty(formObject) && !isEmpty(formField) && !isEmpty(value)){
        formObject.get(formField).patchValue(value);
    }
}
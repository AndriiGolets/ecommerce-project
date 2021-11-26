import {FormControl, ValidationErrors} from "@angular/forms";

export class Luv2ShopValidators {

  static notOnlyWhitespace(control: FormControl) : ValidationErrors {

    if((control.value != null) && (control.value.trim().length === 0) ) {

      // invalid, return error object
      return  {'notOnlyWhiteSpace' : true};
    } else {

      // valid
      return null;
    }
  }

}

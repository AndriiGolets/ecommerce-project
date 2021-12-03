import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import {Luv2ShopFormService} from "../../services/luv2-shop-form.service";
import {Country} from "../../common/country";
import {State} from "../../common/state";
import {Luv2ShopValidators} from "../../validators/luv2-shop-validators";
import {CartService} from "../../services/cart.service";
import {CheckoutService} from "../../services/checkout.service";
import {Router} from "@angular/router";
import {Order} from "../../common/order";
import {OrderItem} from "../../common/order-item";
import {Purchase} from "../../common/purchase";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {

  checkoutFormGroup: FormGroup;

  totalPrice: number = 0;
  totalQuantity: number = 0;

  creditCardYears: number[] = [];
  creditCardMonths: number[] = [];

  countries: Country[] = [];

  shippingAddressStates: State[] = [];
  billingAddressStates: State[] = [];

  storage: Storage = sessionStorage;

  constructor(private formBuilder: FormBuilder,
              private luv2ShopFormService: Luv2ShopFormService,
              private cartService: CartService,
              private checkoutService: CheckoutService,
              private router: Router) {
  }

  ngOnInit(): void {

    this.reviewCartDetails();

    const theEmail = JSON.parse(this.storage.getItem('userEmail'));

    this.checkoutFormGroup = this.formBuilder.group({
      customer: this.formBuilder.group({
        firstName: new FormControl('', [
          Validators.required,
          Validators.minLength(2),
          Luv2ShopValidators.notOnlyWhitespace]),
        lastName: new FormControl('', [
          Validators.required,
          Validators.minLength(2),
          Luv2ShopValidators.notOnlyWhitespace]),
        email: new FormControl(theEmail, [
          Validators.required,
          Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$'),
          Luv2ShopValidators.notOnlyWhitespace])
      }),
      shippingAddress: this.formBuilder.group({
        street: [],
        city: [],
        state: [],
        country: [],
        zipCode: []
      }),
      billingAddress: this.formBuilder.group({
        street: [],
        city: [],
        state: [],
        country: [],
        zipCode: []
      }),
      creditCard: this.formBuilder.group({
        cardType: [],
        nameOnCard: [],
        cardNumber: [],
        securityCode: [],
        expirationMonth: [],
        expirationYear: []
      }),
    })

    // populate credit card months

    const startMonth: number = new Date().getMonth() + 1;
    console.log("startMonth: " + startMonth);

    this.luv2ShopFormService.getCreditCardMonths(startMonth).subscribe(
      data => {
        console.log("Retrieved credit card months: " + JSON.stringify(data));
        this.creditCardMonths = data;
      });

    // populate credit card years

    this.luv2ShopFormService.getCreditCardYears().subscribe(
      data => {
        console.log("Retrieved credit card years: " + JSON.stringify(data));
        this.creditCardYears = data;
      });

    // populate countries

    this.luv2ShopFormService.getCountries().subscribe(
      data => {
        console.log("Retrieved countries: " + JSON.stringify(data))
        this.countries = data;
      }
    )

  }

  onSubmit() {
    console.log("Handling the submit button");
    console.log(this.checkoutFormGroup.get('customer').value);

    if (this.checkoutFormGroup.invalid) {
      this.checkoutFormGroup.markAllAsTouched();
      return;
    }

    // set up order
    let order = new Order();
    order.totalPrice = this.totalPrice;
    order.totalQuantity = this.totalQuantity;

    // get cart items
    const cartItems = this.cartService.cartItems;

    // create order items from cart items
    let orderItems: OrderItem[] = cartItems.map(item => new OrderItem(item));

    // set up purchase
    let purchase: Purchase = new Purchase();

    // populate purchase - customer
    purchase.customer = this.checkoutFormGroup.controls['customer'].value;

    // populate purchase - shipping address
    purchase.shippingAddress = this.checkoutFormGroup.controls['shippingAddress'].value;
    const shippingState: State = JSON.parse(JSON.stringify(purchase.shippingAddress.state));
    const shippingCountry: Country = JSON.parse(JSON.stringify(purchase.shippingAddress.country));
    purchase.shippingAddress.state = shippingState.name;
    purchase.shippingAddress.country = shippingCountry.name;

    // populate purchase - billing address
    purchase.billingAddress = this.checkoutFormGroup.controls['billingAddress'].value;
    const billingState: State = JSON.parse(JSON.stringify(purchase.billingAddress.state));
    const billingCountry: Country = JSON.parse(JSON.stringify(purchase.billingAddress.country));
    purchase.billingAddress.state = billingState.name;
    purchase.billingAddress.country = billingCountry.name;

    // populate purchase - order and orderItems

    purchase.order = order;
    purchase.orderItems = orderItems;

    // post REST API via checkout service

    this.checkoutService.placeOrder(purchase).subscribe({
        next: response => {
          alert(`Your order has been received. \nOrder tracking number: ${response.orderTrackingNumber}`)

          // reset card
          this.resetCart()

        },
        error: err => {
          alert(`There was an error: ${err.message}`)
        }
      }
    );

  }

  copyShippingAddressToBillingAddress(event) {
    if (event.target.checked) {
      this.checkoutFormGroup.controls['billingAddress']
        .setValue(this.checkoutFormGroup.controls['shippingAddress'].value);

      // bugfix for states
      this.billingAddressStates = this.shippingAddressStates;

    } else {
      this.checkoutFormGroup.controls['billingAddress'].reset();

      // bugfix for states
      this.billingAddressStates = [];
    }

  }

  handleMonthAndYears() {

    const creditCardFormGroup = this.checkoutFormGroup.get('creditCard');

    const currentYear: number = new Date().getFullYear();
    const selectedYear: number = Number(creditCardFormGroup.value.expirationYear);

    // if the current year equals the selected year, then start with the current month

    let startMonth: number;

    if (currentYear === selectedYear) {
      startMonth = new Date().getMonth() + 1;
    } else {
      startMonth = 1;
    }

    this.luv2ShopFormService.getCreditCardMonths(startMonth).subscribe(
      data => {
        console.log("Retrieved credit card months: " + JSON.stringify(data));
        this.creditCardMonths = data;
      }
    )

  }

  getStates(formGroupName: string) {
    const formGroup = this.checkoutFormGroup.get(formGroupName);

    const countryCode = formGroup.value.country.code;
    const countryName = formGroup.value.country.name;

    console.log(`${formGroupName} country code: ${countryCode}`);
    console.log(`${formGroupName} country name: ${countryName}`);

    this.luv2ShopFormService.getStates(countryCode).subscribe(
      data => {
        if (formGroupName === 'shippingAddress') {
          this.shippingAddressStates = data;
        } else {
          this.billingAddressStates = data;
        }

        // select first item by default
        formGroup.get('state').setValue(data[0]);
      });
  }

  get firstName() {
    return this.checkoutFormGroup.get('customer.firstName');
  }

  get lastName() {
    return this.checkoutFormGroup.get('customer.lastName');
  }

  get email() {
    return this.checkoutFormGroup.get('customer.email');
  }

  private reviewCartDetails() {
    // subscribe to CartService.totalQuantity
    this.cartService.totalQuantity.subscribe(
      totalQuantity => this.totalQuantity = totalQuantity
    );

    // subscribe to CartService.totalPrice
    this.cartService.totalPrice.subscribe(
      totalPrice => this.totalPrice = totalPrice
    )
  }

  private resetCart() {
    // reset cart data
    this.cartService.cartItems = [];
    this.cartService.totalPrice.next(0);
    //reset the form
    this.checkoutFormGroup.reset();

    // navigate to main page
    this.router.navigateByUrl("/products");

  }
}

entity Employees {
    key ID       : Integer;
        name     : String;
        jobTitle : String;
}

define type User     : String(111);

define type Amount {
    value    : Decimal(10, 3);
    currency : Currency;
}

define type Currency : Association to Currencies;

define entity Currencies {
    key code : String;
        name : String;
}

entity Books {
    price : Amount;
}

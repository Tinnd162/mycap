namespace com.bookshop;

entity Books {
    key ID     : Integer;
        title  : String;
        author : String;
        price  : Decimal;
        stock  : Integer;
        status : String enum {
            AVAILABLE;
            OUT_OF_STOCK;
            DISCONTINUED;
        }
}

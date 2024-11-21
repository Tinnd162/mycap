using com.bookshop as my from '../db/schema';

@path: '/catalog'
service CatalogService {
    @insertable: true
    entity Books as projection on my.Books;
}

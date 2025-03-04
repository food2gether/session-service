INSERT INTO profiles (displayname, name, primaryemail)
VALUES ('DisplayName', 'name', 'test@example.com');

INSERT INTO restaurants (address_city, address_county, address_postal_code, address_street, displayname)
VALUES ('city', 'country', '12345', 'Street 1', 'Restaurant');

INSERT INTO menu_items (price, restaurant_id, description, name)
VALUES (100, 1, 'Description', 'Name');

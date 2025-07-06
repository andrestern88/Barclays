INSERT INTO users (id, firstnames, lastnames, address) VALUES ('usr-1', 'Andre', 'Stern', '1 Street 1');
INSERT INTO users (id, firstnames, lastnames, address) VALUES ('usr-2', 'Mike', 'Four', '2 Street 2');

insert into accounts (account_number,user_id,account_type,balance,created_timestamp,currency,name,sort_code,updated_timestamp,id) values
    ('01837628', 'usr-1', 'PERSONAL', 0, CURRENT_TIMESTAMP, 'GBP','My current account', 'CENTRAL_LONDON', CURRENT_TIMESTAMP, nextval('account_id_seq'));
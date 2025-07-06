INSERT INTO users (id, firstnames, lastnames, address, created_timestamp, email, phone_number, updated_timestamp, username, password)
    VALUES ('usr-1', 'Andre', 'Stern', '1 Street 1', CURRENT_TIMESTAMP, 'andre@stern.com', '+447375901534', CURRENT_TIMESTAMP, 'andre', 'andrepass');
INSERT INTO users (id, firstnames, lastnames, address, created_timestamp, email, phone_number, updated_timestamp, username, password)
    VALUES ('usr-2', 'Mike', 'Four', '2 Street 2', CURRENT_TIMESTAMP, 'mike@gmail.com', '+447366661534', CURRENT_TIMESTAMP, 'mike', 'mikepass');

insert into accounts (account_number,user_id,account_type,balance,created_timestamp,currency,name,sort_code,updated_timestamp,id) values
    ('01837628', 'usr-1', 'PERSONAL', 0, CURRENT_TIMESTAMP, 'GBP','My current account', '10-10-10', CURRENT_TIMESTAMP, nextval('account_id_seq'));
insert into accounts (account_number,user_id,account_type,balance,created_timestamp,currency,name,sort_code,updated_timestamp,id) values
    ('01668595', 'usr-2', 'PERSONAL', 1000.0, CURRENT_TIMESTAMP, 'GBP','Trust fund', '10-10-10', CURRENT_TIMESTAMP, nextval('account_id_seq'));

insert into transactions (account_id,amount,type,id,created_timestamp) values
    ((select id from accounts where account_number = '01837628'),500.0,'DEPOSIT',nextVal('trasaction_id_seq'),CURRENT_TIMESTAMP);
insert into transactions (account_id,amount,type,id,created_timestamp) values
    ((select id from accounts where account_number = '01837628'),200.0,'WITHDRAWAL',nextVal('trasaction_id_seq'),CURRENT_TIMESTAMP);
insert into transactions (account_id,amount,type,id,created_timestamp) values
    ((select id from accounts where account_number = '01668595'),1000.0,'DEPOSIT',nextVal('trasaction_id_seq'),CURRENT_TIMESTAMP);

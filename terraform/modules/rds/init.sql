-- public.campaign definition

-- Drop table

-- DROP TABLE public.campaign;

CREATE TABLE public.campaign (
                                 end_date date NULL,
                                 start_date date NULL,
                                 created_at timestamp(6) NULL,
                                 id int8 NOT NULL,
                                 updated_at timestamp(6) NULL,
                                 description varchar(255) NULL,
                                 CONSTRAINT campaign_pkey PRIMARY KEY (id)
);


-- public.category definition

-- Drop table

-- DROP TABLE public.category;

CREATE TABLE public.category (
                                 enabled bool NULL,
                                 created_at timestamp(6) NULL,
                                 id int8 NOT NULL,
                                 updated_at timestamp(6) NULL,
                                 description varchar(255) NULL,
                                 "name" varchar(255) NULL,
                                 CONSTRAINT category_name_key UNIQUE (name),
                                 CONSTRAINT category_pkey PRIMARY KEY (id)
);


-- public.collaborator definition

-- Drop table

-- DROP TABLE public.collaborator;

CREATE TABLE public.collaborator (
                                     active bool NULL,
                                     birthdate date NULL,
                                     created_at timestamp(6) NULL,
                                     id int8 NOT NULL,
                                     updated_at timestamp(6) NULL,
                                     document_number varchar(255) NULL,
                                     document_type varchar(255) NULL,
                                     email varchar(255) NULL,
                                     "name" varchar(255) NULL,
                                     phone_number varchar(255) NULL,
                                     "role" varchar(255) NULL,
                                     CONSTRAINT collaborator_document_type_check CHECK (((document_type)::text = 'CPF'::text)),
	CONSTRAINT collaborator_pkey PRIMARY KEY (id),
	CONSTRAINT collaborator_role_check CHECK (((role)::text = ANY ((ARRAY['MANAGER'::character varying, 'BASE'::character varying])::text[])))
);


-- public.customer definition

-- Drop table

-- DROP TABLE public.customer;

CREATE TABLE public.customer (
                                 active bool NULL,
                                 birthdate date NULL,
                                 created_at timestamp(6) NULL,
                                 id int8 NOT NULL,
                                 updated_at timestamp(6) NULL,
                                 document_number varchar(255) NULL,
                                 document_type varchar(255) NULL,
                                 email varchar(255) NULL,
                                 "name" varchar(255) NULL,
                                 phone_number varchar(255) NULL,
                                 CONSTRAINT customer_document_type_check CHECK (((document_type)::text = 'CPF'::text)),
	CONSTRAINT customer_pkey PRIMARY KEY (id)
);


-- public.fastfood_user definition

-- Drop table

-- DROP TABLE public.fastfood_user;

CREATE TABLE public.fastfood_user (
                                      created_at timestamp(6) NULL,
                                      id int8 NOT NULL,
                                      person_id int8 NULL,
                                      updated_at timestamp(6) NULL,
                                      "password" varchar(255) NULL,
                                      CONSTRAINT fastfood_user_person_id_key UNIQUE (person_id),
                                      CONSTRAINT fastfood_user_pkey PRIMARY KEY (id)
);


-- public.invoice_vendor definition

-- Drop table

-- DROP TABLE public.invoice_vendor;

CREATE TABLE public.invoice_vendor (
                                       id int8 NOT NULL,
                                       "name" varchar(255) NULL,
                                       CONSTRAINT invoice_vendor_pkey PRIMARY KEY (id)
);


-- public.menu_product definition

-- Drop table

-- DROP TABLE public.menu_product;

CREATE TABLE public.menu_product (
                                     "cost" numeric(38, 2) NULL,
                                     ingredient bool NULL,
                                     optional bool NULL,
                                     price numeric(38, 2) NULL,
                                     quantity int4 NULL,
                                     created_at timestamp(6) NULL,
                                     id int8 NOT NULL,
                                     preparation_time int8 NULL,
                                     updated_at timestamp(6) NULL,
                                     description varchar(255) NULL,
                                     "name" varchar(255) NULL,
                                     active bool NULL,
                                     CONSTRAINT menu_product_pkey PRIMARY KEY (id)
);


-- public.menu_product_ingredient definition

-- Drop table

-- DROP TABLE public.menu_product_ingredient;

CREATE TABLE public.menu_product_ingredient (
                                                ingredient_id int8 NOT NULL,
                                                product_id int8 NOT NULL
);


-- public.activation_code definition

-- Drop table

-- DROP TABLE public.activation_code;

CREATE TABLE public.activation_code (
                                        customer_id int8 NULL,
                                        expiration timestamp(6) NULL,
                                        "key" uuid NOT NULL,
                                        CONSTRAINT activation_code_pkey PRIMARY KEY (key),
                                        CONSTRAINT fk7cilyvc04gysgsfo056yq8ewo FOREIGN KEY (customer_id) REFERENCES public.customer(id)
);


-- public.campaign_product definition

-- Drop table

-- DROP TABLE public.campaign_product;

CREATE TABLE public.campaign_product (
                                         "cost" numeric(38, 2) NULL,
                                         ingredient bool NULL,
                                         optional bool NULL,
                                         price numeric(38, 2) NULL,
                                         quantity int4 NULL,
                                         campaign_id int8 NULL,
                                         created_at timestamp(6) NULL,
                                         id int8 NOT NULL,
                                         preparation_time int8 NULL,
                                         updated_at timestamp(6) NULL,
                                         description varchar(255) NULL,
                                         "name" varchar(255) NULL,
                                         active bool NULL,
                                         CONSTRAINT campaign_product_pkey PRIMARY KEY (id),
                                         CONSTRAINT fkmxbi858du9wywnrtpon2lfmbm FOREIGN KEY (campaign_id) REFERENCES public.campaign(id)
);


-- public.category_product definition

-- Drop table

-- DROP TABLE public.category_product;

CREATE TABLE public.category_product (
                                         category_id int8 NOT NULL,
                                         product_id int8 NOT NULL,
                                         CONSTRAINT fkfr6rjc04htbtc3xas2b9xmq7r FOREIGN KEY (category_id) REFERENCES public.category(id)
);


-- public.customer_order definition

-- Drop table

-- DROP TABLE public.customer_order;

CREATE TABLE public.customer_order (
                                       price numeric(38, 2) NULL,
                                       collaborator_id int8 NULL,
                                       created_at timestamp(6) NULL,
                                       customer_id int8 NULL,
                                       id int8 NOT NULL,
                                       updated_at timestamp(6) NULL,
                                       status varchar(255) NULL,
                                       CONSTRAINT customer_order_pkey PRIMARY KEY (id),
                                       CONSTRAINT customer_order_status_check CHECK (((status)::text = ANY ((ARRAY['IN_CREATION'::character varying, 'AWAITING_PAYMENT'::character varying, 'RECEIVED'::character varying, 'IN_PREPARATION'::character varying, 'READY'::character varying, 'FINISHED'::character varying, 'CANCELLED'::character varying])::text[]))),
	CONSTRAINT fkf9abd30bhiqvugayxlpq8ryq9 FOREIGN KEY (customer_id) REFERENCES public.customer(id),
	CONSTRAINT fkitjd93c2mdbpsyiwdcjgmws52 FOREIGN KEY (collaborator_id) REFERENCES public.collaborator(id)
);


-- public.followup definition

-- Drop table

-- DROP TABLE public.followup;

CREATE TABLE public.followup (
                                 show_order int4 NULL,
                                 id int8 NOT NULL,
                                 order_id int8 NULL,
                                 state varchar(255) NULL,
                                 CONSTRAINT followup_pkey PRIMARY KEY (id),
                                 CONSTRAINT followup_state_check CHECK (((state)::text = ANY ((ARRAY['RECEIVED'::character varying, 'IN_PREPARATION'::character varying, 'READY'::character varying, 'FINISHED'::character varying])::text[]))),
	CONSTRAINT fkg9ljklsof7k98hk7wwuyvuukm FOREIGN KEY (order_id) REFERENCES public.customer_order(id)
);


-- public.invoice definition

-- Drop table

-- DROP TABLE public.invoice;

CREATE TABLE public.invoice (
                                price numeric(38, 2) NULL,
                                created_at timestamp(6) NULL,
                                external_invoice_identifier int8 NULL,
                                id int8 NOT NULL,
                                order_id int8 NULL,
                                updated_at timestamp(6) NULL,
                                vendor_id int8 NULL,
                                current_state varchar(255) NULL,
                                CONSTRAINT invoice_current_state_check CHECK (((current_state)::text = ANY ((ARRAY['EXPIRED'::character varying, 'PENDING'::character varying, 'PAID'::character varying, 'CANCELLED'::character varying])::text[]))),
	CONSTRAINT invoice_pkey PRIMARY KEY (id),
	CONSTRAINT fknmmmw4kidu1o9wb03u1r6wgth FOREIGN KEY (vendor_id) REFERENCES public.invoice_vendor(id),
	CONSTRAINT fkqc9etr7kuxcuxju749ep97sev FOREIGN KEY (order_id) REFERENCES public.customer_order(id)
);


-- public.order_product definition

-- Drop table

-- DROP TABLE public.order_product;

CREATE TABLE public.order_product (
                                      "cost" numeric(38, 2) NULL,
                                      ingredient bool NULL,
                                      optional bool NULL,
                                      price numeric(38, 2) NULL,
                                      quantity int4 NULL,
                                      should_remove bool NULL,
                                      created_at timestamp(6) NULL,
                                      id int8 NOT NULL,
                                      menu_product_id int8 NOT NULL,
                                      order_id int8 NULL,
                                      preparation_time int8 NULL,
                                      updated_at timestamp(6) NULL,
                                      description varchar(255) NULL,
                                      "name" varchar(255) NULL,
                                      CONSTRAINT order_product_pkey PRIMARY KEY (id),
                                      CONSTRAINT fkmx4qjk512djj1adniweb0599c FOREIGN KEY (order_id) REFERENCES public.customer_order(id)
);


-- public.order_product_ingredient definition

-- Drop table

-- DROP TABLE public.order_product_ingredient;

CREATE TABLE public.order_product_ingredient (
                                                 order_ingredient_id int8 NOT NULL,
                                                 order_product_id int8 NOT NULL,
                                                 CONSTRAINT fknwowclmd9sgha65wnkmeaqnhu FOREIGN KEY (order_product_id) REFERENCES public.order_product(id),
                                                 CONSTRAINT fkxvrnp7kononpblro51kbm2uy FOREIGN KEY (order_ingredient_id) REFERENCES public.order_product(id)
);


-- public.order_product_optional definition

-- Drop table

-- DROP TABLE public.order_product_optional;

CREATE TABLE public.order_product_optional (
                                               order_optional_id int8 NOT NULL,
                                               order_product_id int8 NOT NULL,
                                               CONSTRAINT fk65iqcboxgntw8ygwpknpcx21t FOREIGN KEY (order_product_id) REFERENCES public.order_product(id),
                                               CONSTRAINT fkmvtwrsxapsas4s1u5la69l93x FOREIGN KEY (order_optional_id) REFERENCES public.order_product(id)
);

CREATE TABLE public.payment (
                                id int8 NOT NULL,
                                amount numeric(38, 2) NULL,
                                callback_url varchar(255) NULL,
                                created_at timestamp(6) NULL,
                                currency varchar(255) NULL,
                                payment_description varchar(255) NULL,
                                due_at timestamp(6) NULL,
                                external_payment_id int8 NULL,
                                external_product_id int8 NULL,
                                payer_id int8 NULL,
                                receiver_id int8 NULL,
                                status varchar(255) NULL,
                                updated_at timestamp(6) NULL,
                                CONSTRAINT payment_pkey PRIMARY KEY (id),
                                CONSTRAINT payment_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'PAID'::character varying, 'EXPIRED'::character varying])::text[])))
);

-- Drop table

-- DROP TABLE public.users;
CREATE TABLE public.users (
                              id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                              name VARCHAR(255) NOT NULL,
                              cpf VARCHAR(11) NOT NULL
);

INSERT INTO public.users (name, cpf) VALUES
                                         ('Felipe', '12345678901'),
                                         ('Guilherme', '12345678902'),
                                         ('Robson', '12345678903');

INSERT INTO public.menu_product ("cost",ingredient,optional,price,quantity,created_at,id,preparation_time,updated_at,description,"name", "active") VALUES
                                                                                                                                             (0.80,true,true,1.00,1,'2024-08-12 20:00:51.497',1,60000,NULL,NULL,'Hamburguer', true),
                                                                                                                                             (0.30,true,true,0.50,1,'2024-08-12 20:01:08.070',2,NULL,NULL,NULL,'Mustard', true),
                                                                                                                                             (0.05,true,true,0.50,1,'2024-08-12 20:01:19.647',3,NULL,NULL,NULL,'Ketchup', true),
                                                                                                                                             (0.50,true,true,0.80,1,'2024-08-12 20:01:33.020',4,NULL,NULL,NULL,'Bread', true),
                                                                                                                                             (1.65,false,false,15.50,1,'2024-08-12 20:01:35.983',5,150000,NULL,NULL,'Hyper Burger', true),
                                                                                                                                             (0.50,true,false,2.50,1,'2024-08-12 20:06:20.570',6,0,NULL,NULL,'Água', true),
                                                                                                                                             (0.15,true,true,1.00,1,'2024-08-12 20:07:46.816',7,30000,NULL,NULL,'Limão', true),
                                                                                                                                             (0.90,true,true,1.50,1,'2024-08-12 20:08:15.114',8,60000,NULL,NULL,'Morango', true),
                                                                                                                                             (0.90,true,true,1.50,1,'2024-08-12 20:08:39.972',9,60000,NULL,NULL,'Amora', true),
                                                                                                                                             (2.45,false,false,13.00,1,'2024-08-12 20:10:13.518',10,180000,NULL,NULL,'Pink Lemonade', true);
INSERT INTO public.menu_product ("cost",ingredient,optional,price,quantity,created_at,id,preparation_time,updated_at,description,"name", "active") VALUES
                                                                                                                                             (2.00,true,false,4.00,1,'2024-08-12 22:14:11.589',11,240000,NULL,NULL,'Batata', true),
                                                                                                                                             (2.50,true,false,10.00,1,'2024-08-12 22:15:26.349',12,180000,NULL,'Batata e oleo!','Batata Frita', true),
                                                                                                                                             (2.50,true,false,8.00,1,'2024-08-12 22:17:55.462',13,0,NULL,'Glace, glace e mais glace!','Bolinho de pote', true),
                                                                                                                                             (2.50,false,false,20.00,1,'2024-08-12 22:18:54.881',14,120000,NULL,'Glace, glace e mais glace (com sorvete incluso)!','Petit Gateou', true);



INSERT INTO public.menu_product_ingredient (ingredient_id,product_id) VALUES
                                                                          (4,5),
                                                                          (1,5),
                                                                          (2,5),
                                                                          (3,5),
                                                                          (6,10),
                                                                          (7,10),
                                                                          (8,10),
                                                                          (9,10),
                                                                          (11,12),
                                                                          (13,14);



INSERT INTO public.category (enabled,created_at,id,updated_at,description,"name") VALUES
                                                                                      (true,'2024-08-12 20:04:14.624',1,NULL,'Os famosissimos lanches do FASTFOOD API!','Lanche'),
                                                                                      (true,'2024-08-12 20:11:20.435',2,NULL,'As mais saborosas bebidas do bairro!','Bebida'),
                                                                                      (true,'2024-08-12 22:16:14.258',3,NULL,'As mais diversas opcoes de acompanhamento da loja!','Acompanhamento'),
                                                                                      (true,'2024-08-12 22:19:29.478',4,NULL,'Apenas dois doces! Voce economiza tempo!','Sobremesa');


INSERT INTO public.category_product (category_id,product_id) VALUES
                                                                 (1,5),
                                                                 (2,10),
                                                                 (2,6),
                                                                 (3,11),
                                                                 (3,12),
                                                                 (4,13),
                                                                 (4,14);


-- public.campaign_seq definition

-- DROP SEQUENCE public.campaign_seq;

CREATE SEQUENCE public.campaign_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	NO CYCLE;
select setval('campaign_seq',  (SELECT coalesce((MAX(id)), 1) FROM public.campaign));

-- public.category_seq definition

-- DROP SEQUENCE public.category_seq;

CREATE SEQUENCE public.category_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	NO CYCLE;
select setval('category_seq',  (SELECT coalesce((MAX(id)), 1) FROM public.category));

-- public.customer_order_seq definition

-- DROP SEQUENCE public.customer_order_seq;

CREATE SEQUENCE public.customer_order_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
	NO CYCLE;

-- public.fastfood_user_seq definition

-- DROP SEQUENCE public.fastfood_user_seq;

CREATE SEQUENCE public.fastfood_user_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
	NO CYCLE;

-- public.followup_seq definition

-- DROP SEQUENCE public.followup_seq;

CREATE SEQUENCE public.followup_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
	NO CYCLE;

-- public.invoice_seq definition

-- DROP SEQUENCE public.invoice_seq;

CREATE SEQUENCE public.invoice_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
	NO CYCLE;

-- public.invoice_vendor_seq definition

-- DROP SEQUENCE public.invoice_vendor_seq;

CREATE SEQUENCE public.invoice_vendor_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
	NO CYCLE;

-- public.person_entity_seq definition

-- DROP SEQUENCE public.person_entity_seq;

CREATE SEQUENCE public.person_entity_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
	NO CYCLE;

-- public.product_entity_seq definition

-- DROP SEQUENCE public.product_entity_seq;

CREATE SEQUENCE public.payment_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

CREATE SEQUENCE public.product_entity_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	NO CYCLE;
select setval('product_entity_seq',  (SELECT coalesce((MAX(id)), 1) FROM (select id from public.menu_product mp union select id from public.order_product op) as products));


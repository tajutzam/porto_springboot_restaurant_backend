

CREATE TABLE users
(
  id varchar(100) NOT NULL,
  first_name varchar(100) NOT NULL,
  last_name varchar(100) NOT NULL,
  address varchar(100) NOT NULL,
  username varchar(100) NOT NULL,
  password varchar(100) NOT NULL,
  token varchar(400),
  refresh_token varchar (400) ,
  created_at timestamp NOT NULL,
  update_at timestamp NOT NULL,
  PRIMARY KEY (id)
)ENGINE INNODB;

CREATE TABLE admin
(
  id varchar(100) NOT NULL,
  username varchar(100) NOT NULL,
  password varchar(200) NOT NULL,
  first_name varchar(100) NOT NULL,
  last_name varchar(100) NOT NULL,
  PRIMARY KEY (id)
)ENGINE INNODB;

CREATE TABLE restaurant
(
  id varchar(100) NOT NULL,
  username varchar(100) NOT NULL,
  password varchar(100) NOT NULL,
  last_name varchar(100) NOT NULL,
  banner varchar(100) ,
  first_name varchar(100) NOT NULL,
  address varchar(100) NOT NULL,
  PRIMARY KEY (id)
)ENGINE INNODB;

CREATE TABLE category
(
  id varchar(100) NOT NULL,
  name varchar(100) NOT NULL,
  images varchar(100) NOT NULL,
  created_at timestamp not null,
  updated_at timestamp not null,
  PRIMARY KEY (id)
)ENGINE INNODB;

CREATE TABLE rating_restaurant
(
  id varchar(100) NOT NULL,
  restaurant_id varchar(100) NOT NULL,
  created_at timestamp not null,
  updated_at timestamp not null,
  PRIMARY KEY (id),
  FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) on delete CASCADE on update cascade
)ENGINE INNODB;

CREATE TABLE transaksi
(
  id varchar(100) NOT NULL,
  cart_id  varchar(100) NOT NULL,
  status_transaction enum('CANCELED' , 'DELIVERED' , 'DONE' , 'PROCESS'),
  created_at DATE NOT NULL,
  updated_at date NOT NULL,
  total_price INT DEFAULT 0,
  user_id varchar(100) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY(user_id) REFERENCES users(id) on delete CASCADE on UPDATE CASCADE
) ENGINE INNODB;

CREATE TABLE cart
(
  id varchar(100) NOT NULL,
  total_price INT DEFAULT 0,
  status_cart ENUM('CHECKOUT' , 'DONE' , 'QUEUE') NOT NULL,
  transaction_id varchar(100) NOT NULL,
    created_at timestamp not null,
  updated_at timestamp not null,
  PRIMARY KEY (id),
  FOREIGN KEY (transaction_id) REFERENCES transaksi(id) on DELETE CASCADE on UPDATE CASCADE
) ENGINE INNODB;

CREATE TABLE cart_detail
(
  id varchar(100) NOT NULL,
  menu_id varchar(100) NOT NULL,
  qty INT NOT NULL,
  sub_total INT NOT NULL,
  cart_id varchar(100) NOT NULL,
    created_at timestamp not null,
  updated_at timestamp not null,
  PRIMARY KEY (id),
  FOREIGN KEY (cart_id) REFERENCES cart(id) on DELETE CASCADE on UPDATE CASCADE
)ENGINE INNODB;

CREATE TABLE detail_rating_restaurant
(
  id varchar(100) NOT NULL,
  user_id varchar(100) NOT NULL,
  rating_restaurant_id varchar(100) NOT NULL,
  rate INT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users (id)on delete CASCADE on update cascade,
  FOREIGN KEY (rating_restaurant_id) REFERENCES rating_restaurant (id)on delete CASCADE on update cascade
) ENGINE INNODB;



CREATE TABLE Menu
(
  id varchar(100) NOT NULL,
  name varchar(100) NOT NULL,
  image varchar(100) NOT NULL,
  price INT NOT NULL,
  status_menu enum("0" , "1"),
  restaurant_id varchar(100) NOT NULL,
  category_id varchar(100) NOT NULL,
    created_at timestamp not null,
  updated_at timestamp not null,
  PRIMARY KEY (id),
  FOREIGN KEY (restaurant_id) REFERENCES restaurant(id)on delete CASCADE on update cascade,
  FOREIGN KEY (category_id) REFERENCES category(id)on delete CASCADE on update cascade
)ENGINE INNODB;

CREATE TABLE rating_menu
(
  id varchar(100) NOT NULL,
  total_rate INT NOT NULL,
  menu_id varchar(100) NOT NULL,
    created_at timestamp not null,
  updated_at timestamp not null,
  PRIMARY KEY (id),
  FOREIGN KEY (menu_id) REFERENCES Menu(id)on delete CASCADE on update cascade
) ENGINE INNODB;

CREATE TABLE detail_rating_menu
(
  id varchar(100) NOT NULL,
  rating_menu_id varchar(100) NOT NULL,
  rate INT NOT NULL,
  user_id varchar(100) NOT NULL,
    created_at timestamp not null,
  updated_at timestamp not null,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users (id) on delete CASCADE on update cascade,
  FOREIGN KEY (rating_menu_id) REFERENCES rating_menu(id) on delete CASCADE on update cascade
)ENGINE INNODB;


alter table users add avatar varchar(400);


alter table restaurant add token varchar(400) ;

alter table restaurant add refresh_token varchar(400) ;

alter table admin add token varchar(400);

alter table admin add refresh_token varchar(400) ;



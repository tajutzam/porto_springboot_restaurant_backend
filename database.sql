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
    ) ENGINE INNODB;

    CREATE TABLE admin
    (
      id varchar(100) NOT NULL,
      username varchar(100) NOT NULL,
      password varchar(200) NOT NULL,
      first_name varchar(100) NOT NULL,
      last_name varchar(100) NOT NULL,
      PRIMARY KEY (id)
    ) ENGINE INNODB;

    CREATE TABLE restaurant
    (
      id varchar(100) NOT NULL,
      username varchar(100) NOT NULL,
      password varchar(100) NOT NULL,
      last_name varchar(100) NOT NULL,
      banner varchar(100),
      first_name varchar(100) NOT NULL,
      address varchar(100) NOT NULL,
      bank_number bigint not null,
      saldo bigint,
      PRIMARY KEY (id)
    ) ENGINE INNODB;

    CREATE TABLE category
    (
      id varchar(100) NOT NULL,
      name varchar(100) NOT NULL,
      images varchar(100) NOT NULL,
      created_at timestamp NOT NULL,
      updated_at timestamp NOT NULL,
      PRIMARY KEY (id)
    ) ENGINE INNODB;

    CREATE TABLE rating_restaurant
    (
      id varchar(100) NOT NULL,
      restaurant_id varchar(100) NOT NULL,
      created_at timestamp NOT NULL,
      total_rate double not null,
      updated_at timestamp NOT NULL,
      PRIMARY KEY (id),
      FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE INNODB;

    CREATE TABLE transaksi
    (
      id varchar(100) NOT NULL,
      cart_id varchar(100) ,
      status_transaction enum('CANCELED', 'DELIVERED', 'DONE', 'PROCESS' , 'QUEUE' , 'WAITING_PAYMENT' , 'EXPIRE'),
      created_at DATE NOT NULL,
      updated_at date NOT NULL,
      total_price INT DEFAULT 0,
      user_id varchar(100) NOT NULL,
        restaurant_id varchar(100),
      bank_method enum('BCA' , 'BRI') not null,
      va_number varchar(100) not null,
      expired_payment timestamp,
      created_payment timestamp,
      PRIMARY KEY (id),
      FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
        foreign key (restaurant_id) references restaurant (id) on delete cascade on update cascade
    ) ENGINE INNODB;

    CREATE TABLE cart
    (
      id varchar(100) NOT NULL,
      total_price INT DEFAULT 0,
      status_cart ENUM('CHECKOUT', 'DONE', 'QUEUE' , 'CANCEL' , 'EXPIRE') NOT NULL,
      transaction_id varchar(100),
      created_at timestamp NOT NULL,
      updated_at timestamp NOT NULL,
      restaurant_id varchar(100) not null,
      user_id varchar(100) not null,
      PRIMARY KEY (id),
      foreign key(user_id) references users (id) on delete cascade on update cascade,
      FOREIGN KEY (transaction_id) REFERENCES transaksi (id) ON DELETE CASCADE ON UPDATE CASCADE,
      foreign key (restaurant_id) references restaurant (id) on delete cascade on update cascade
    ) ENGINE INNODB;

    CREATE TABLE cart_detail
    (
      id varchar(100) NOT NULL,
      menu_id varchar(100) NOT NULL,
      qty INT NOT NULL,
      sub_total INT NOT NULL,
      cart_id varchar(100) NOT NULL,
      created_at timestamp NOT NULL,
      updated_at timestamp NOT NULL,
      PRIMARY KEY (id),
      FOREIGN KEY (cart_id) REFERENCES cart (id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE INNODB;

    CREATE TABLE detail_rating_restaurant
    (
      id varchar(100) NOT NULL,
      user_id varchar(100) NOT NULL,
      rating_restaurant_id varchar(100) NOT NULL,
      rate INT NOT NULL,
      PRIMARY KEY (id),
      FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
      FOREIGN KEY (rating_restaurant_id) REFERENCES rating_restaurant (id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE INNODB;

    CREATE TABLE menu
    (
      id varchar(100) NOT NULL,
      name varchar(100) NOT NULL,
      image varchar(100) NOT NULL,
      price INT NOT NULL,
      status_menu enum('READY', 'NOT_READY'),
      restaurant_id varchar(100) NOT NULL,
      category_id varchar(100) NOT NULL,
      created_at timestamp NOT NULL,
      updated_at timestamp NOT NULL,
      PRIMARY KEY (id),
      FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE ON UPDATE CASCADE,
      FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE INNODB;

    CREATE TABLE rating_menu
    (
      id varchar(100) NOT NULL,
      total_rate double NOT NULL,
      menu_id varchar(100) NOT NULL,
      created_at timestamp NOT NULL,
      updated_at timestamp NOT NULL,
      PRIMARY KEY (id),
      FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE INNODB;

    CREATE TABLE detail_rating_menu
    (
      id varchar(100) NOT NULL,
      rating_menu_id varchar(100) NOT NULL,
      rate INT NOT NULL,
      user_id varchar(100) NOT NULL,
      created_at timestamp NOT NULL,
      updated_at timestamp NOT NULL,
      PRIMARY KEY (id),
      FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
      FOREIGN KEY (rating_menu_id) REFERENCES rating_menu (id) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE INNODB;

    ALTER TABLE users ADD avatar varchar(400);
    ALTER TABLE users ADD phone_number varchar(13) NOT NULL;
    ALTER TABLE users ADD email varchar(100) UNIQUE NOT NULL;

    ALTER TABLE restaurant ADD token varchar(400);
    ALTER TABLE restaurant ADD refresh_token varchar(400);

    ALTER TABLE admin ADD token varchar(400);
    ALTER TABLE admin ADD refresh_token varchar(400);

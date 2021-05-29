CREATE TABLE user_contacts(
      id SERIAL PRIMARY KEY NOT NULL,
      business_card_id INT NOT NULL,
      user_id INT NOT NULL,
      created_at TIMESTAMPTZ NOT NULL,
      updated_at TIMESTAMPTZ NOT NULL,
      version BIGINT,

      CONSTRAINT fk_contacts_users FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE,
      CONSTRAINT fk_contacts_business_cards FOREIGN KEY (business_card_id) REFERENCES business_cards ON DELETE CASCADE
)
CREATE TABLE contacts(
      id SERIAL PRIMARY KEY NOT NULL,
      business_card_id INT NOT NULL,
      user_id INT NOT NULL,
      created_at TIMESTAMPTZ NOT NULL,
      updated_at TIMESTAMPTZ NOT NULL,
      version BIGINT,

      CONSTRAINT fk_users_contacts FOREIGN KEY (user_id) REFERENCES users ON DELETE CASCADE,
      CONSTRAINT fk_business_cards_contacts FOREIGN KEY (business_card_id) REFERENCES business_cards ON DELETE CASCADE
)
CREATE INDEX ON apartment_external_photos(apartment_id);

CREATE INDEX ON apartments(country_code text_pattern_ops);
CREATE INDEX ON apartments(fee_period text_pattern_ops);
CREATE INDEX ON apartments(description text_pattern_ops);
CREATE INDEX ON apartments(data_source text_pattern_ops);
CREATE INDEX ON apartments(external_id text_pattern_ops);
CREATE INDEX ON apartments(ext_link text_pattern_ops);
CREATE INDEX ON apartments(target text_pattern_ops);
CREATE INDEX ON apartments(description_hash text_pattern_ops);
CREATE INDEX ON apartments(owner_id);

CREATE INDEX ON realty_users(username text_pattern_ops);
CREATE INDEX ON realty_users(password_hash text_pattern_ops);
CREATE INDEX ON realty_users(phone_number text_pattern_ops);
CREATE INDEX ON realty_users(national_formatted_number text_pattern_ops);
CREATE INDEX ON realty_users(fb_access_token text_pattern_ops);
CREATE INDEX ON realty_users(vk_access_token text_pattern_ops);

CREATE INDEX ON contacts(phone_number text_pattern_ops);
CREATE INDEX ON contacts(national_formatted_number text_pattern_ops);

CREATE INDEX ON vk_page(city_id);

CREATE INDEX ON metro_stations(city_id);

CREATE INDEX ON realty_users_authorities(user_id, authority_id);

CREATE INDEX ON vk_publishing_events(used_token text_pattern_ops);
CREATE INDEX ON vk_publishing_events(created_dt);
CREATE INDEX ON vk_publishing_events(data_source text_pattern_ops);
CREATE INDEX ON vk_publishing_events(target_group text_pattern_ops);
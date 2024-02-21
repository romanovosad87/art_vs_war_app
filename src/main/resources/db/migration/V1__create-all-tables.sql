--
-- Table structure for table `accounts`
--

CREATE TABLE `accounts` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `cognito_subject` varchar(255) NOT NULL,
                            `cognito_username` varchar(255) NOT NULL,
                            `created_at` datetime(6) DEFAULT NULL,
                            `first_name` varchar(255) NOT NULL,
                            `is_deleted` tinyint NOT NULL DEFAULT '0',
                            `last_name` varchar(255) NOT NULL,
                            `offset` int DEFAULT '0',
                            `phone` varchar(255) NOT NULL,
                            `stripe_customer_id` varchar(255) DEFAULT NULL,
                            `updated_at` datetime(6) DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `UK_accounts_cognito_subject` (`cognito_subject`),
                            UNIQUE KEY `UK_accounts_stripe_customer_id` (`stripe_customer_id`)
);

--
-- Table structure for table `account_email_datas`
--

CREATE TABLE `account_email_datas` (
                                       `id` bigint NOT NULL,
                                       `bounce` tinyint NOT NULL DEFAULT '0',
                                       `complaint` tinyint NOT NULL DEFAULT '0',
                                       `email` varchar(255) NOT NULL,
                                       `unsubscribed` tinyint NOT NULL DEFAULT '0',
                                       PRIMARY KEY (`id`),
                                       KEY `IDX_email` (`email`),
                                       CONSTRAINT `FK_account_email_details_accounts` FOREIGN KEY (`id`) REFERENCES `accounts` (`id`)
);

--
-- Table structure for table `account_shipping_addresses`
--

CREATE TABLE `account_shipping_addresses` (
                                              `account_id` bigint NOT NULL,
                                              `address_line1` varchar(255) NOT NULL,
                                              `address_line2` varchar(255) DEFAULT NULL,
                                              `city` varchar(255) NOT NULL,
                                              `country` varchar(255) NOT NULL,
                                              `country_code` varchar(255) NOT NULL,
                                              `first_name` varchar(255) NOT NULL,
                                              `last_name` varchar(255) NOT NULL,
                                              `phone` varchar(255) NOT NULL,
                                              `postal_code` varchar(255) NOT NULL,
                                              `state` varchar(255),
                                              KEY `FK_account_shipping_addresses_account` (`account_id`),
                                              CONSTRAINT `FK_account_shipping_addresses_account` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
);

--
-- Table structure for table `authors`
--

CREATE TABLE `authors` (
                           `id` bigint NOT NULL,
                           `cognito_subject` varchar(255) NOT NULL,
                           `cognito_username` varchar(255) NOT NULL,
                           `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           `is_deleted` tinyint NOT NULL DEFAULT '0',
                           `pretty_id` varchar(255) NOT NULL,
                           `updated_at` datetime(6) DEFAULT NULL,
                           `email` varchar(255) NOT NULL,
                           `about_me` longtext,
                           `city` varchar(255) NOT NULL,
                           `country` varchar(255) NOT NULL,
                           `full_name` varchar(255) NOT NULL,
                           `unsubscribed_email` tinyint NOT NULL DEFAULT '0',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK_authors_cognito_subject` (`cognito_subject`),
                           UNIQUE KEY `UK_authors_pretty_id` (`pretty_id`),
                           CONSTRAINT `FK_authors_accounts` FOREIGN KEY (`id`) REFERENCES `accounts` (`id`)
);

--
-- Table structure for table `stripe_profiles`
--

CREATE TABLE `stripe_profiles` (
                                   `id` bigint NOT NULL,
                                   `account_id` varchar(255) NOT NULL,
                                   `created_at` datetime(6) DEFAULT NULL,
                                   `is_details_submitted` tinyint NOT NULL DEFAULT '0',
                                   `updated_at` datetime(6) DEFAULT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `UK_stripe_profiles_account_id` (`account_id`),
                                   KEY `IDX_is_detail_submitted` (`is_details_submitted`),
                                   CONSTRAINT `FK_stripe_profiles_authors` FOREIGN KEY (`id`) REFERENCES `authors` (`id`)
);

--
-- Table structure for table `collections`
--

CREATE TABLE `collections` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `description` longtext NOT NULL,
                               `pretty_id` varchar(255) NOT NULL,
                               `title` varchar(255) NOT NULL,
                               `author_id` bigint NOT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `UK_collections_pretty_id` (`pretty_id`),
                               KEY `FK_collections_authors` (`author_id`),
                               CONSTRAINT `FK_collections_authors` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`)
);

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `charge_id` varchar(255) DEFAULT NULL,
                          `checkout_session_id` varchar(255) NOT NULL,
                          `completed_at` datetime(6) DEFAULT NULL,
                          `created_at` datetime(6) DEFAULT NULL,
                          `delivered_at` datetime(6) DEFAULT NULL,
                          `income` decimal(19,2) DEFAULT NULL,
                          `is_delivered` tinyint NOT NULL DEFAULT '0',
                          `net_amount` decimal(19,2) NOT NULL,
                          `payment_intent_id` varchar(255) NOT NULL,
                          `shipping_amount` decimal(19,2) DEFAULT NULL,
                          `subtotal_amount` decimal(19,2) DEFAULT NULL,
                          `total_amount` decimal(19,2) NOT NULL,
                          `transfer_amount` decimal(19,2) DEFAULT NULL,
                          `account_id` bigint NOT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `UK_orders_checkout_session_id` (`checkout_session_id`),
                          UNIQUE KEY `UK_orders_payment_intent_id` (`payment_intent_id`),
                          UNIQUE KEY `UK_orders_charge_id` (`charge_id`),
                          KEY `FK_orders_accounts` (`account_id`),
                          CONSTRAINT `FK_orders_accounts` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
);


--
-- Table structure for table `paintings`
--

CREATE TABLE `paintings` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `depth` double NOT NULL,
                             `description` longtext NOT NULL,
                             `entity_created_at` datetime(6) DEFAULT NULL,
                             `height` double NOT NULL,
                             `payment_status` tinyint NOT NULL DEFAULT '0',
                             `pretty_id` varchar(255) NOT NULL,
                             `price` decimal(19,2) NOT NULL,
                             `title` varchar(255) NOT NULL,
                             `updated_at` datetime(6) DEFAULT NULL,
                             `weight` double NOT NULL,
                             `width` double NOT NULL,
                             `year_of_creation` int NOT NULL,
                             `author_id` bigint NOT NULL,
                             `collection_id` bigint DEFAULT NULL,
                             `order_id` bigint DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `UK_paintings_pretty_id` (`pretty_id`),
                             KEY `IDX_price` (`price`),
                             KEY `IDX_width` (`width`),
                             KEY `IDX_height` (`height`),
                             KEY `IDX_payment_status` (`payment_status`),
                             KEY `FK_paintings_authors` (`author_id`),
                             KEY `FK_paintings_collections` (`collection_id`),
                             KEY `FK_paintings_orders` (`order_id`),
                             CONSTRAINT `FK_paintings_orders` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
                             CONSTRAINT `FK_paintings_collections` FOREIGN KEY (`collection_id`) REFERENCES `collections` (`id`),
                             CONSTRAINT `FK_paintings_authors` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`)
);

--
-- Table structure for table `images`
--

CREATE TABLE `images` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `created_at` datetime(6) DEFAULT NULL,
                          `moderation_status` tinyint NOT NULL DEFAULT '0',
                          `public_id` varchar(255) NOT NULL,
                          `updated_at` datetime(6) DEFAULT NULL,
                          `url` varchar(500) NOT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `UK_images_public_id` (`public_id`),
                          UNIQUE KEY `UK_images_url` (`url`),
                          KEY `IDX_moderation_status` (`moderation_status`)
);

--
-- Table structure for table `painting_images`
--

CREATE TABLE `painting_images` (
                                   `id` bigint NOT NULL,
                                   `height` double NOT NULL,
                                   `initial_ratio` double NOT NULL,
                                   `transformed_ratio` double NOT NULL,
                                   `width` double NOT NULL,
                                   `image_id` bigint NOT NULL,
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `UK_painting_images_image_id` (`image_id`),
                                   CONSTRAINT `FK_painting_images_paintings` FOREIGN KEY (`id`) REFERENCES `paintings` (`id`),
                                   CONSTRAINT `FK_painting_images_images` FOREIGN KEY (`image_id`) REFERENCES `images` (`id`)
);


--
-- Table structure for table `additional_images`
--

CREATE TABLE `additional_images` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `image_id` bigint NOT NULL,
                                     `painting_id` bigint NOT NULL,
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `UK_additional_images_image_id` (`image_id`),
                                     KEY `FK_additional_images_painting_id` (`painting_id`),
                                     CONSTRAINT `FK_additional_images_painting_id` FOREIGN KEY (`painting_id`) REFERENCES `paintings` (`id`),
                                     CONSTRAINT `FK_additional_images_image_id` FOREIGN KEY (`image_id`) REFERENCES `images` (`id`)
);


--
-- Table structure for table `admins`
--

CREATE TABLE `admins` (
                          `id` bigint NOT NULL,
                          `cognito_subject` varchar(255) NOT NULL,
                          `cognito_username` varchar(255) NOT NULL,
                          `created_at` datetime(6) DEFAULT NULL,
                          `is_deleted` tinyint NOT NULL DEFAULT '0',
                          `updated_at` datetime(6) DEFAULT NULL,
                          `full_name` varchar(255) NOT NULL,
                          `email` varchar(255) NOT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `UK_admins_cognito_subject` (`cognito_subject`),
                          CONSTRAINT `FK_admins_accounts` FOREIGN KEY (`id`) REFERENCES `accounts` (`id`)
);

--
-- Table structure for table `art_process`
--

CREATE TABLE `art_processes` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `description` varchar(255) DEFAULT NULL,
                               `author_id` bigint NOT NULL,
                               PRIMARY KEY (`id`),
                               KEY `FK_art_processes_author` (`author_id`),
                               CONSTRAINT `FK_art_processes_author` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`)
);

--
-- Table structure for table `art_process_image`
--

CREATE TABLE `art_process_image` (
                                     `id` bigint NOT NULL,
                                     `height` double NOT NULL,
                                     `width` double NOT NULL,
                                     `image_id` bigint NOT NULL,
                                     PRIMARY KEY (`id`),
                                     UNIQUE KEY `UK_art_process_image_image_id` (`image_id`),
                                     CONSTRAINT `FK_art_process_image_images` FOREIGN KEY (`image_id`) REFERENCES `images` (`id`),
                                     CONSTRAINT `FK_art_process_image_art_processes` FOREIGN KEY (`id`) REFERENCES `art_processes` (`id`)
);

--
-- Table structure for table `author_photos`
--

CREATE TABLE `author_photos` (
                                 `id` bigint NOT NULL,
                                 `image_id` bigint NOT NULL,
                                 PRIMARY KEY (`id`),
                                 UNIQUE KEY `UK_author_photos_image_id` (`image_id`),
                                 CONSTRAINT `FK_author_photos_images` FOREIGN KEY (`image_id`) REFERENCES `images` (`id`),
                                 CONSTRAINT `FK_author_photos_authors` FOREIGN KEY (`id`) REFERENCES `authors` (`id`)
);


--
-- Table structure for table `author_shipping_addresses`
--

CREATE TABLE `author_shipping_addresses` (
                                             `author_id` bigint NOT NULL,
                                             `address_line1` varchar(255) NOT NULL,
                                             `address_line2` varchar(255) DEFAULT NULL,
                                             `city` varchar(255) NOT NULL,
                                             `country` varchar(255) NOT NULL,
                                             `country_code` varchar(255) NOT NULL,
                                             `created_at` datetime(6) DEFAULT NULL,
                                             `phone` varchar(255) DEFAULT NULL,
                                             `postal_code` varchar(255) NOT NULL,
                                             `state` varchar(255),
                                             `updated_at` datetime(6) DEFAULT NULL,
                                             PRIMARY KEY (`author_id`),
                                             CONSTRAINT `FK_author_shipping_addresses_authors` FOREIGN KEY (`author_id`) REFERENCES `authors` (`id`)
);

--
-- Table structure for table `mediums`
--

CREATE TABLE `mediums` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `name` varchar(255) NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK_mediums_name` (`name`)
);

--
-- Table structure for table `styles`
--

CREATE TABLE `styles` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `name` varchar(255) NOT NULL,
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `UK_styles_name` (`name`)
);

--
-- Table structure for table `subject`
--

CREATE TABLE `subjects` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `name` varchar(255) NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK_subjects_name` (`name`)
);

--
-- Table structure for table `supports`
--

CREATE TABLE `supports` (
                            `id` bigint NOT NULL AUTO_INCREMENT,
                            `name` varchar(255) NOT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `UK_supports_name` (`name`)
);

--
-- Table structure for table `paintings_mediums`
--

CREATE TABLE `paintings_mediums` (
                                     `painting_id` bigint NOT NULL,
                                     `medium_id` bigint NOT NULL,
                                     PRIMARY KEY (`painting_id`,`medium_id`),
                                     KEY `FK_paintings_mediums_mediums` (`medium_id`),
                                     CONSTRAINT `FK_paintings_mediums_paintings` FOREIGN KEY (`painting_id`) REFERENCES `paintings` (`id`),
                                     CONSTRAINT `FK_paintings_mediums_mediums` FOREIGN KEY (`medium_id`) REFERENCES `mediums` (`id`)
);

--
-- Table structure for table `paintings_styles`
--

CREATE TABLE `paintings_styles` (
                                    `painting_id` bigint NOT NULL,
                                    `style_id` bigint NOT NULL,
                                    PRIMARY KEY (`painting_id`,`style_id`),
                                    KEY `FK_paintings_styles_styles` (`style_id`),
                                    CONSTRAINT `FK_paintings_styles_styles` FOREIGN KEY (`style_id`) REFERENCES `styles` (`id`),
                                    CONSTRAINT `FK_paintings_styles_paintings` FOREIGN KEY (`painting_id`) REFERENCES `paintings` (`id`)
);

--
-- Table structure for table `paintings_subjects`
--

CREATE TABLE `paintings_subjects` (
                                      `painting_id` bigint NOT NULL,
                                      `subject_id` bigint NOT NULL,
                                      PRIMARY KEY (`painting_id`,`subject_id`),
                                      KEY `FK_paintings_subjects_subjects` (`subject_id`),
                                      CONSTRAINT `FK_paintings_subjects_subjects` FOREIGN KEY (`subject_id`) REFERENCES `subjects` (`id`),
                                      CONSTRAINT `FK_paintings_subjects_paintings` FOREIGN KEY (`painting_id`) REFERENCES `paintings` (`id`)
);

--
-- Table structure for table `paintings_supports`
--

CREATE TABLE `paintings_supports` (
                                      `painting_id` bigint NOT NULL,
                                      `support_id` bigint NOT NULL,
                                      PRIMARY KEY (`painting_id`,`support_id`),
                                      KEY `FK_paintings_supports_supports` (`support_id`),
                                      CONSTRAINT `FK_paintings_supports_paintings` FOREIGN KEY (`painting_id`) REFERENCES `paintings` (`id`),
                                      CONSTRAINT `FK_paintings_supports_supports` FOREIGN KEY (`support_id`) REFERENCES `supports` (`id`)
);

--
-- Table structure for table `room_views`
--

CREATE TABLE `room_views` (
                              `painting_image_id` bigint NOT NULL,
                              `image_url` varchar(500) NOT NULL,
                              KEY `IDX_painting_image_room_view` (`painting_image_id`),
                              CONSTRAINT `FK_room_views_painting_images` FOREIGN KEY (`painting_image_id`) REFERENCES `painting_images` (`id`)
);

--
-- Table structure for table `shopping_carts`
--

CREATE TABLE `shopping_carts` (
                                  `id` bigint NOT NULL,
                                  PRIMARY KEY (`id`),
                                  CONSTRAINT `FK_shopping_carts_accounts` FOREIGN KEY (`id`) REFERENCES `accounts` (`id`)
);

--
-- Table structure for table `shopping_carts_paintings`
--

CREATE TABLE `shopping_carts_paintings` (
                                            `painting_id` bigint NOT NULL,
                                            `shopping_cart_id` bigint NOT NULL,
                                            `created_at` datetime(6) DEFAULT NULL,
                                            PRIMARY KEY (`painting_id`,`shopping_cart_id`),
                                            KEY `FK_shopping_carts_paintings_shopping_carts` (`shopping_cart_id`),
                                            CONSTRAINT `FK_shopping_carts_paintings_shopping_carts` FOREIGN KEY (`shopping_cart_id`) REFERENCES `shopping_carts` (`id`),
                                            CONSTRAINT `FK_shopping_carts_paintings_paintings` FOREIGN KEY (`painting_id`) REFERENCES `paintings` (`id`)
);

--
-- Table structure for table `mock_rooms`
--

CREATE TABLE `mock_rooms` (
                              `id` bigint NOT NULL,
                              `public_id` varchar(255) DEFAULT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `UK_mock_rooms_public_id` (`public_id`)
);

-- --------------------------
-- Table structure for donates
-- --------------------------
CREATE TABLE `donates` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `amount` decimal(19,2) NOT NULL,
                           `created_at` datetime(6) DEFAULT NULL,
                           `email` varchar(255) DEFAULT NULL,
                           `message` varchar(255) DEFAULT NULL,
                           `name` varchar(255) DEFAULT NULL,
                           `net_amount` decimal(19,2) NOT NULL,
                           PRIMARY KEY (`id`)
);


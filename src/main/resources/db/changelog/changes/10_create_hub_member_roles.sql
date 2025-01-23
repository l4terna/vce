-- liquibase formatted sql

-- changeset laterna:1
CREATE TABLE hub_member_roles (
    hub_member_id BIGINT REFERENCES hub_members(id) ON DELETE CASCADE NOT NULL,
    role_id BIGINT REFERENCES roles(id) ON DELETE CASCADE NOT NULL,
    UNIQUE (hub_member_id, role_id)
);
-- rollback DROP TABLE hub_member_roles;
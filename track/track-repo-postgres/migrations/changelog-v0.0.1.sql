CREATE TYPE "ticket_state_type" AS ENUM ('new', 'progress', 'finish');

CREATE TABLE "tickets" (
	"id" serial primary key,
	"subject" text constraint ticket_title_length_ctr check (length(subject) < 128),
	"description" text constraint ticket_description_length_ctr check (length(description) < 4096),
	"state" ticket_state_type,
	"owner_id" varchar(64),
	"creation_date" timestamp not null,
	"finish_date" timestamp,
	"lock" integer not null
);

CREATE TABLE "comments" (
	"id" serial primary key,
    "ticket_id" integer references tickets(id),
    "author" varchar(32),
    "creation_date" timestamp not null,
    "text" text
);

CREATE INDEX IF NOT EXISTS ticket_owner_id_idx on "tickets" using hash ("owner_id");
CREATE INDEX IF NOT EXISTS ticket_ticket_id_idx on "comments" using hash ("ticket_id");

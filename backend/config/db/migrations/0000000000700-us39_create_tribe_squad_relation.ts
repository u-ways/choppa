import { Migration } from "../../nessie.config.ts";
import { createTable, dropTable } from "../../../src/utils/db_utils.ts";

export const up: Migration = () => {
  return createTable("tribe_current_members", (table: any) => {
    table.integer("tribe_id");
    table.integer("squad_id");
    table.primary(["tribe_id", "squad_id"]);
    table.foreign("tribe_id").references("tribe_id").inTable("tribe");
    table.foreign("squad_id").references("squad_id").inTable("squad");
  });
};

export const down: Migration = () => {
  return dropTable("tribe_current_members");
};

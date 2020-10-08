import {dex, Migration} from "../../nessie.config.ts";

export const up: Migration = () => {
    return dex.schema.createTable("squad_current_members", (table: any) => {
        table.integer("squad_id");
        table.integer("mem_id");
        table.primary(["squad_id", "mem_id"]);
        table.foreign("squad_id").references("squad_id").inTable("squad");
        table.foreign("mem_id").references("mem_id").inTable("member");
        table.timestamp("rotation_date");
    }).toString();
}
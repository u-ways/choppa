Choppa Backend:
===

Choppa Backend module. It is built using [deno.land][1]. 
(A secure runtime for JavaScript and TypeScript.)

#### Available Commands/Tasks:

We use [Denon][4] as our task runner for Deno.
Please run the command [`denon`][4] to view the list of available commands/tasks.   

**Installing Denon:**

To install simply run:
`deno install -qAf --unstable https://deno.land/x/denon@2.4.0/denon.ts`

Afterwards, make sure you add denon to your path:
`export PATH="/home/u-ways/snap/deno/12/.deno/bin:$PATH"`

#### Folder structure and component architecture

The folder structure and component architecture was inspired from Gonzalo P. 
blog post ["Structure of a NodeJS API Project"][3] that follows similar patterns 
and frameworks to our Deno project.

#### Style guide:

We follow Deno's official style guide. It includes guidelines for things like 
filenames, new feature testing, TODO comments, exports/imports, JSDoc and more.
You can find the complete style guide document by [clicking here][2].

#### Database Migrations

We use [deno-nessie ][5] for our database migrations.

**Commands:**
- `denon migrate` to migrate the database
- `denon seed` to seed the database. 

**Example of a migration file:** 

A migration file should be named as follows: `[TIMESTAMP]-[DESCRIPTION].ts` 
and are located in `choppa/config/db/migrations`

**Note:** Migration files are ordered and _recognised_ by the timestamp (Epoch)
 at the start of file name.

```ts
// Filename 0000000000001-example.ts
import {Migration} from "https://raw.githubusercontent.com/halvardssm/deno-nessie/master/mod.ts";

export const up: Migration = () => {
    return `
        CREATE TABLE squads (
            id SERIAL PRIMARY KEY
        ,   name VARCHAR(100) NOT NULL
        );
    `
};

export const down: Migration = () => {
    return `
        DROP TABLE squads;
    `
};
```

**Example of a seed file:** 

A seed file can be named as follows: `[DESCRIPTION].ts` 
and are located in `choppa/config/db/seeds`

```ts
// Filename example_squad.ts
import { Seed } from "https://raw.githubusercontent.com/halvardssm/deno-nessie/master/mod.ts";

/** Runs on seed */
export const run: Seed = () => {
    return `
        INSERT INTO squads (
            name
        ) VALUES (
            'Uways'
        );
    `
};
```

___

[1]:https://deno.land/
[2]:https://deno.land/manual/contributing/style_guide
[3]:https://medium.com/codebase/structure-of-a-nodejs-api-project-cdecb46ef3f8
[4]:https://github.com/denosaurs/denon
[5]:https://github.com/halvardssm/deno-nessie
[5]:https://github.com/halvardssm/deno-nessie
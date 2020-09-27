import db from '../../../config/mongodb.ts';

interface SquadSchema {
    _id: {
        $oid: string;
    };
    name: string;
}

export interface NewSquad {
    name: string;
}

const database = db.getDatabase;
export const squad = database.collection<SquadSchema>('squad');
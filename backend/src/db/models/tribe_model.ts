import db from '../../../config/mongodb.ts';

interface TribeSchema {
    _id: {
        $oid: string;
    };
    name: string;
}

export interface NewTribe {
    name: string;
}

const database = db.getDatabase;
export const tribe = database.collection<TribeSchema>('tribe');
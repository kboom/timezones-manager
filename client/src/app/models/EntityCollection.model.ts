export interface EntityFactory<T> {
    construct(obj: any): T
}

export class EntityCollectionModel<T> {

    readonly entities: T[] = [];
    // readonly links: Map<String, String>;

    constructor(readonly entityName: string, body: string, entityFactory: EntityFactory<T>) {
        this.entities = body['_embedded'][entityName].map(entityFactory.construct);
        // this.links = new Map<String, String>(body['_links']);
    }

}
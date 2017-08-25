export interface ModelFactory<T> {
    constructModel(obj: any): T
}

export class Entity<T> {

    constructor(readonly entity: T,
                readonly links: Map<String, String>) {

    }

    static fromJSON<T>(body: any, modelFactory: ModelFactory<T>): Entity<T> {
        return new Entity(modelFactory.constructModel(body), body['_links'])
    }

    withUpdatedEntity = (entity: T): Entity<T> => {
        return new Entity(entity, this.links);
    }

}
export class AdvertisementRequest {
    deadline: string;
    activeFrom: string;
    activeUntil: string;
    description: string = '';
    isCreated: boolean = false;
    username: string = '';
    id: number;

    constructor() {}
}

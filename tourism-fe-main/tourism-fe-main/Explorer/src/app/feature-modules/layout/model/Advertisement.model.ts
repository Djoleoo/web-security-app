export class Advertisement {
  id: number;
  username: string;
  activeFrom: string;
  activeUntil: string;
  description: string;
  slogan: string;
  requestId: number;

  constructor(id: number, username: string, activeFrom: string, activeUntil: string, description: string, slogan: string, requestId: number) {
    this.id = id;
    this.username = username;
    this.activeFrom = activeFrom;
    this.activeUntil = activeUntil;
    this.description = description;
    this.slogan = slogan;
    this.requestId = requestId;
  }
}

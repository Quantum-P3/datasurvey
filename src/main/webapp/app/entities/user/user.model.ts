export interface IUser {
  id?: number;
  login?: string;
  firstName?: string | null;
  lastName?: string | null;
  email?: string;
  authorities?: string[];
}

export class User implements IUser {
  constructor(
    public id: number,
    public login: string,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public authorities?: string[]
  ) {}
}

export function getUserIdentifier(user: IUser): number | undefined {
  return user.id;
}

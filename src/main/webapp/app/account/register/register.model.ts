export class Registration {
  constructor(
    public login: string,
    public email: string,
    public password: string,
    public langKey: string,
    public name: string,
    public profileIcon: number,
    public isAdmin: number,
    public isGoogle: number,
    public firstName: string
  ) {}
}

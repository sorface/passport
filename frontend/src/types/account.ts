export enum AccountRole {
    ADMIN = 'ADMIN',
}

export interface Account {
    id: string;
    nickname: string;
    email: string;
    firstName?: string;
    lastName?: string;
    avatar: string;
    roles: AccountRole[];
}

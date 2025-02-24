import {Account} from '../types/account';

export const getUserAvatarCaption = (account: Account | null): string | undefined => {
    if (account?.firstName && account?.lastName) {
        return `${account?.firstName[0].toUpperCase()} ${account?.lastName[0].toUpperCase()}`
    }

    return account?.nickname[0];
}

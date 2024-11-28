import { Account, AccountRole } from '../types/account';

export const checkAdmin = (account: Account | null) =>
  !!account && account.roles.includes(AccountRole.ADMIN);

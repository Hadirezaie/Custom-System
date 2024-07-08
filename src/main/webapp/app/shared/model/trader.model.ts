export interface ITrader {
  id?: number;
  name?: string | null;
  nidNumber?: string | null;
  tIN?: string | null;
  licenseNumber?: string | null;
}

export const defaultValue: Readonly<ITrader> = {};

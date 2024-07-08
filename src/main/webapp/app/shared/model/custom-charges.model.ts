export interface ICustomCharges {
  id?: number;
  deviceModel?: string | null;
  customFee?: number | null;
}

export const defaultValue: Readonly<ICustomCharges> = {};

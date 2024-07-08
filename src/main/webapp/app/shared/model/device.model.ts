import { ITrader } from 'app/shared/model/trader.model';

export interface IDevice {
  id?: number;
  serialNumber?: string;
  modelNumber?: string;
  trader?: ITrader | null;
}

export const defaultValue: Readonly<IDevice> = {};

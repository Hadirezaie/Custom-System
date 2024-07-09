import { ITrader } from 'app/shared/model/trader.model';
import { ITarif } from 'app/shared/model/tarif.model';

export interface IDevice {
  id?: number;
  serialNumber?: string;
  modelNumber?: string;
  trader?: ITrader | null;
  tarif?: ITarif | null;
}

export const defaultValue: Readonly<IDevice> = {};

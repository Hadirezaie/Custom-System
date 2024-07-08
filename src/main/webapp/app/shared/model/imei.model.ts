import { IDevice } from 'app/shared/model/device.model';

export interface IImei {
  id?: number;
  imeiNumber?: number | null;
  device?: IDevice | null;
}

export const defaultValue: Readonly<IImei> = {};

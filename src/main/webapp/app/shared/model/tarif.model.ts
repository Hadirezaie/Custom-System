import dayjs from 'dayjs';
import { IDevice } from 'app/shared/model/device.model';

export interface ITarif {
  id?: number;
  amount?: number | null;
  paidDate?: string | null;
  paid?: boolean | null;
  device?: IDevice | null;
}

export const defaultValue: Readonly<ITarif> = {
  paid: false,
};

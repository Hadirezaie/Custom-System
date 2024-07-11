package af.mcit.customsystem.service.dto;

import af.mcit.customsystem.domain.Trader;
import java.util.List;

public class DeviceRegisterBatchDTO {

    private Trader trader;
    private List<DeviceRegisterDTO> deviceRegisterDTOs;

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public List<DeviceRegisterDTO> getDeviceRegisterDTOs() {
        return deviceRegisterDTOs;
    }

    public void setDeviceRegisterDTOs(List<DeviceRegisterDTO> deviceRegisterDTOs) {
        this.deviceRegisterDTOs = deviceRegisterDTOs;
    }
}

package af.mcit.customsystem.service.dto;

import java.util.Set;

public class ImeiCheckDTO {

    private String deviceModel;
    private String deviceSerialNumber;
    private Set<String> imeis;

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public Set<String> getImeis() {
        return imeis;
    }

    public void setImeis(Set<String> imeis) {
        this.imeis = imeis;
    }
}

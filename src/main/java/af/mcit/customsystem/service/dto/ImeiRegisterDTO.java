package af.mcit.customsystem.service.dto;

import java.util.Set;

public class ImeiRegisterDTO {

    private String deviceSerialNumber;

    private String DeviceModelNumber;

    private String traderName;

    private String traderNidNumber;

    private String traderTIN;

    private String traderLicenseNumber;
    private Set<String> imeis;

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getDeviceModelNumber() {
        return DeviceModelNumber;
    }

    public void setDeviceModelNumber(String deviceModelNumber) {
        DeviceModelNumber = deviceModelNumber;
    }

    public String getTraderName() {
        return traderName;
    }

    public void setTraderName(String traderName) {
        this.traderName = traderName;
    }

    public String getTraderNidNumber() {
        return traderNidNumber;
    }

    public void setTraderNidNumber(String traderNidNumber) {
        this.traderNidNumber = traderNidNumber;
    }

    public String getTraderTIN() {
        return traderTIN;
    }

    public void setTraderTIN(String traderTIN) {
        this.traderTIN = traderTIN;
    }

    public String getTraderLicenseNumber() {
        return traderLicenseNumber;
    }

    public void setTraderLicenseNumber(String traderLicenseNumber) {
        this.traderLicenseNumber = traderLicenseNumber;
    }

    public Set<String> getImeis() {
        return imeis;
    }

    public void setImeis(Set<String> imeis) {
        this.imeis = imeis;
    }
}

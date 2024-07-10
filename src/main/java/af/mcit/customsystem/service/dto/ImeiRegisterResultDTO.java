package af.mcit.customsystem.service.dto;

public class ImeiRegisterResultDTO {

    private String message;
    private String deviceModel;
    private Long customCharge;
    private String paidStatus;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public Long getCustomCharge() {
        return customCharge;
    }

    public void setCustomCharge(Long customCharge) {
        this.customCharge = customCharge;
    }

    public String getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }
}

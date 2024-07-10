package af.mcit.customsystem.service.dto;

public class ImeiCheckResultDTO {

    private Long imei;
    private String deviceModel;
    private String description;
    private String traderName;
    private String traderNidNumber;

    public Long getImei() {
        return imei;
    }

    public void setImei(Long imei) {
        this.imei = imei;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}

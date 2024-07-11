package af.mcit.customsystem.service.dto;

import java.util.List;

public class DeviceRegisterDTO {

    private DeviceDTO deviceDTO;
    private List<ImeiDTO> imeis;

    public DeviceDTO getDeviceDTO() {
        return deviceDTO;
    }

    public void setDeviceDTO(DeviceDTO deviceDTO) {
        this.deviceDTO = deviceDTO;
    }

    public List<ImeiDTO> getImeis() {
        return imeis;
    }

    public void setImeis(List<ImeiDTO> imeis) {
        this.imeis = imeis;
    }
}

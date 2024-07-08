package af.mcit.customsystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import af.mcit.customsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImeiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Imei.class);
        Imei imei1 = new Imei();
        imei1.setId(1L);
        Imei imei2 = new Imei();
        imei2.setId(imei1.getId());
        assertThat(imei1).isEqualTo(imei2);
        imei2.setId(2L);
        assertThat(imei1).isNotEqualTo(imei2);
        imei1.setId(null);
        assertThat(imei1).isNotEqualTo(imei2);
    }
}

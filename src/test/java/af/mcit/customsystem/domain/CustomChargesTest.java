package af.mcit.customsystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import af.mcit.customsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomChargesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomCharges.class);
        CustomCharges customCharges1 = new CustomCharges();
        customCharges1.setId(1L);
        CustomCharges customCharges2 = new CustomCharges();
        customCharges2.setId(customCharges1.getId());
        assertThat(customCharges1).isEqualTo(customCharges2);
        customCharges2.setId(2L);
        assertThat(customCharges1).isNotEqualTo(customCharges2);
        customCharges1.setId(null);
        assertThat(customCharges1).isNotEqualTo(customCharges2);
    }
}

package pillihuaman.com.pe.support.foreing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pillihuaman.com.pe.lib.common.ResponseUser;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ExternalData {
    private ResponseUser field1;

    // Getters and setters
}
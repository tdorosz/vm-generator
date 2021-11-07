package com.tdorosz.vmgenerator;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TemplateResolver {

    private static final String HOST_NAME_PLACEHOLDER = "{{hostname}}";
    private static final String IP_ADDRESS_PLACEHOLDER = "{{ip_address}}";
    private static final String BOX_VERSION_PLACEHOLDER = "{{box_version}}";

    private String hostname;
    private String ipAddress;
    private String boxVersion;
    private String template;

    public String generateVagrantfile() {
        return template.replace(HOST_NAME_PLACEHOLDER, hostname)
                .replace(IP_ADDRESS_PLACEHOLDER, ipAddress)
                .replace(BOX_VERSION_PLACEHOLDER, boxVersion);
    }

}

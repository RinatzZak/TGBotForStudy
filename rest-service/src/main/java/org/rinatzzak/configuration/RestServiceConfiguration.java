package org.rinatzzak.configuration;

import org.rinatzzak.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestServiceConfiguration {
    @Value("${salt}")
    private String salt;

    public CryptoTool getCryptoTool() {
        return new CryptoTool(salt);
    }
}

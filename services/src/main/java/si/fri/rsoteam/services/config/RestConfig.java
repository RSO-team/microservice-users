package si.fri.rsoteam.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ConfigBundle("rest-config")
@ApplicationScoped
public class RestConfig {
    @ConfigValue(watch = true)
    Boolean maintenanceMode;

    @ConfigValue(watch = true)
    Boolean apiToken;

    public Boolean getMaintenanceMode() {
        return this.maintenanceMode;
    }

    public void setMaintenanceMode(final Boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public Boolean getApiToken() {
        return apiToken;
    }

    public void setApiToken(Boolean apiToken) {
        this.apiToken = apiToken;
    }
}

package si.fri.rsoteam.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;
import com.kumuluz.ee.logs.cdi.Log;

import javax.enterprise.context.ApplicationScoped;

@Log
@ConfigBundle("rest-config")
@ApplicationScoped
public class RestConfig {
    @ConfigValue(watch = true)
    Boolean maintenanceMode;

    @ConfigValue(watch = true)
    String apiToken;

    public Boolean getMaintenanceMode() {
        return this.maintenanceMode;
    }

    public void setMaintenanceMode(final Boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}

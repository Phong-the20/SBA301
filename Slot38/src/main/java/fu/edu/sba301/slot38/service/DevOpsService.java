package fu.edu.sba301.slot38.service;

import fu.edu.sba301.slot38.model.AppStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

@Service
public class DevOpsService implements IDevOpsService {

    @Value("${spring.application.name:slot38-devops}")
    private String applicationName;

    @Override
    public AppStatus getApplicationStatus() {
        long uptimeMs = ManagementFactory.getRuntimeMXBean().getUptime();
        long uptimeSeconds = uptimeMs / 1000;
        String javaVersion = System.getProperty("java.version");

        return new AppStatus(applicationName, "UP", uptimeSeconds, javaVersion);
    }

    @Override
    public String echoMessage(String message) {
        if (message == null || message.isBlank()) {
            return "Echo: [EMPTY_INPUT]";
        }
        return "Echo: " + message.trim();
    }

    @Override
    public Map<String, Object> performHealthDiagnosis() {
        Map<String, Object> details = new HashMap<>();
        long totalMemory = Runtime.getRuntime().totalMemory() / (1024 * 1024);
        long freeMemory = Runtime.getRuntime().freeMemory() / (1024 * 1024);
        long maxMemory = Runtime.getRuntime().maxMemory() / (1024 * 1024);

        details.put("status", "HEALTHY");
        details.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        details.put("totalMemoryMB", totalMemory);
        details.put("freeMemoryMB", freeMemory);
        details.put("maxMemoryMB", maxMemory);
        details.put("liveReloadPort", 35729);
        details.put("jdwpPort", 5005);
        return details;
    }
}

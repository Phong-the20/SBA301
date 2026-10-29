package fu.edu.sba301.slot38.model;

import java.time.Instant;

public class AppStatus {
    private String applicationName;
    private String status;
    private long uptimeSeconds;
    private String javaVersion;
    private Instant timestamp;

    public AppStatus() {}

    public AppStatus(String applicationName, String status, long uptimeSeconds, String javaVersion) {
        this.applicationName = applicationName;
        this.status = status;
        this.uptimeSeconds = uptimeSeconds;
        this.javaVersion = javaVersion;
        this.timestamp = Instant.now();
    }

    public String getApplicationName() { return applicationName; }
    public void setApplicationName(String applicationName) { this.applicationName = applicationName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getUptimeSeconds() { return uptimeSeconds; }
    public void setUptimeSeconds(long uptimeSeconds) { this.uptimeSeconds = uptimeSeconds; }

    public String getJavaVersion() { return javaVersion; }
    public void setJavaVersion(String javaVersion) { this.javaVersion = javaVersion; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}

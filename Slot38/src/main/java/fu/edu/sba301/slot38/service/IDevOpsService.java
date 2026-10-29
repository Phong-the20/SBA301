package fu.edu.sba301.slot38.service;

import fu.edu.sba301.slot38.model.AppStatus;
import java.util.Map;

public interface IDevOpsService {
    AppStatus getApplicationStatus();
    String echoMessage(String message);
    Map<String, Object> performHealthDiagnosis();
}

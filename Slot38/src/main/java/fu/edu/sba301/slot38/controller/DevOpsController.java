package fu.edu.sba301.slot38.controller;

import fu.edu.sba301.slot38.model.AppStatus;
import fu.edu.sba301.slot38.service.IDevOpsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/devops")
public class DevOpsController {

    private final IDevOpsService devOpsService;

    public DevOpsController(IDevOpsService devOpsService) {
        this.devOpsService = devOpsService;
    }

    @GetMapping("/status")
    public AppStatus getStatus() {
        return devOpsService.getApplicationStatus();
    }

    @GetMapping("/echo")
    public String echo(@RequestParam(value = "msg", defaultValue = "Hello Slot 38") String msg) {
        return devOpsService.echoMessage(msg);
    }

    @GetMapping("/health-check")
    public Map<String, Object> healthCheck() {
        return devOpsService.performHealthDiagnosis();
    }
}

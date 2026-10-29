package fu.edu.sba301.slot38;

import fu.edu.sba301.slot38.controller.DevOpsController;
import fu.edu.sba301.slot38.model.AppStatus;
import fu.edu.sba301.slot38.service.IDevOpsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DevOpsController.class)
class DevOpsServiceAndControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDevOpsService devOpsService;

    @Test
    void testGetApplicationStatus() throws Exception {
        AppStatus status = new AppStatus("slot38-devops", "UP", 42L, "21");
        when(devOpsService.getApplicationStatus()).thenReturn(status);

        mockMvc.perform(get("/api/devops/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.applicationName").value("slot38-devops"))
                .andExpect(jsonPath("$.uptimeSeconds").value(42));

        verify(devOpsService, times(1)).getApplicationStatus();
    }

    @Test
    void testEchoEndpoint() throws Exception {
        when(devOpsService.echoMessage("Hello DevOps")).thenReturn("Echo: Hello DevOps");

        mockMvc.perform(get("/api/devops/echo").param("msg", "Hello DevOps"))
                .andExpect(status().isOk())
                .andExpect(content().string("Echo: Hello DevOps"));

        verify(devOpsService, times(1)).echoMessage("Hello DevOps");
    }

    @Test
    void testHealthDiagnosisEndpoint() throws Exception {
        when(devOpsService.performHealthDiagnosis()).thenReturn(Map.of(
                "status", "HEALTHY",
                "jdwpPort", 5005,
                "liveReloadPort", 35729
        ));

        mockMvc.perform(get("/api/devops/health-check"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("HEALTHY"))
                .andExpect(jsonPath("$.jdwpPort").value(5005));

        verify(devOpsService, times(1)).performHealthDiagnosis();
    }
}

package pl.vistula.firstrestapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    public record Info(String studentId, String applicationName, String version) {}

    @GetMapping("/api/info")
    public Info info() {
        return new Info("73195", "First-Java-CRUD-REST", "1.0.0");
    }
}

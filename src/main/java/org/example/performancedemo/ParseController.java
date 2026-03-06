import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ParseController {

    private volatile String result = "processing";

    @GetMapping("/start")
    public String start() throws Exception {
        result = "processing";

        new Thread(() -> {
            try {
                Thread.sleep(5000);
                result = "done";
            } catch (Exception ignored) {}
        }).start();

        return "started";
    }

    @GetMapping("/status")
    public String status() {
        return result;
    }
}
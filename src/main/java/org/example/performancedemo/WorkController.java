package org.example.performancedemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
public class WorkController {

    private final ExecutorService platformPool = Executors.newFixedThreadPool(200);

    @GetMapping("/work/platform")
    public String platform(
            @RequestParam(defaultValue = "50") int tasks,
            @RequestParam(defaultValue = "200") int sleepMs
    ) throws Exception {
        runTasks(platformPool, tasks, sleepMs);
        return "OK platform tasks=" + tasks + " sleepMs=" + sleepMs;
    }

    @GetMapping("/work/virtual")
    public String virtual(
            @RequestParam(defaultValue = "50") int tasks,
            @RequestParam(defaultValue = "200") int sleepMs
    ) throws Exception {
        try (ExecutorService vexec = Executors.newVirtualThreadPerTaskExecutor()) {
            runTasks(vexec, tasks, sleepMs);
        }
        return "OK virtual tasks=" + tasks + " sleepMs=" + sleepMs;
    }

    private void runTasks(ExecutorService exec, int tasks, int sleepMs) throws Exception {
        List<Callable<Void>> jobs = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            jobs.add(() -> {
                Thread.sleep(sleepMs);
                return null;
            });
        }

        List<Future<Void>> futures = exec.invokeAll(jobs, 30, TimeUnit.SECONDS);
        for (Future<Void> f : futures) {
            f.get();
        }
    }
}

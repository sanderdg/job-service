package nl.sadego.demo.job;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@RestController
@RequestMapping(path = "/jobs")
@RequiredArgsConstructor
public class JobController {

    private final Logger logger = LoggerFactory.getLogger(JobController.class);
    private final ExecutorService executorService;
    private final ConcurrentHashMap<Job, Future<?>> jobTasks = new ConcurrentHashMap<>();

    @GetMapping()
    List<Job> listJobs() {
        return new ArrayList<>(jobTasks.keySet());
    }

    @PostMapping
    Job createJob(@Nullable @RequestParam Long sleepMs) {
        if (sleepMs == null) {
            sleepMs = 3000L;
        }
        logger.info("Creating new job");
        Job job = new Job(UUID.randomUUID().toString(), JobStatus.SCHEDULED);
        SimpleJobTask simpleJobTask = new SimpleJobTask(job, sleepMs);
        jobTasks.put(job, executorService.submit(simpleJobTask));
        return job;
    }

    @GetMapping("/{id}")
    Job getJob(@PathVariable(name = "id") String id) {
        return getJobById(id);
    }

    @PutMapping("/{id}")
    void updateJob(@PathVariable(name = "id") String id, @RequestBody Job job) {
        validateJobStatus(job.getStatus());
        Job existingJob = getJobById(id);
        switch (existingJob.getStatus()) {
            case FAILED:
            case COMPLETED:
            case CANCELLED:
                throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Cannot change job status of " + existingJob.getStatus() + " job"
                );
            default:
                jobTasks.get(existingJob).cancel(true);
                existingJob.setStatus(job.getStatus());
                logger.info("Updated job status to {}", existingJob.getStatus());
        }
    }

    private void validateJobStatus(JobStatus jobStatus) {
        if (JobStatus.CANCELLED != jobStatus) {
            throw new ResponseStatusException(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Cannot change job status to " + jobStatus + " job"
            );
        }
    }

    private Job getJobById(String id) {
        logger.info("Fetching job {}", id);
        return jobTasks.keySet().stream()
            .filter(j -> j.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}

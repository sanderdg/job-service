package nl.sadego.demo.job;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class SimpleJobTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(SimpleJobTask.class);
    private final Job job;
    private final long sleepMs;

    @Override
    public void run() {
        logger.info("Running job {}", job.getId());
        job.setStatus(JobStatus.RUNNING);
        try {
            Thread.sleep(sleepMs);
            if (Math.random() < 0.5) {
                job.setStatus(JobStatus.COMPLETED);
                logger.info("Job {} completed", job.getId());
            } else {
                job.setStatus(JobStatus.FAILED);
                logger.info("Job {} failed", job.getId());
            }
        } catch (InterruptedException e) {
            job.setStatus(JobStatus.CANCELLED);
            logger.info("Job {} cancelled", job.getId());
        }
    }

}

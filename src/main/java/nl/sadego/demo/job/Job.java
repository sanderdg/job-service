package nl.sadego.demo.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
public class Job {

    private String id;

    @EqualsAndHashCode.Exclude
    private JobStatus status;

}

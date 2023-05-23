package io.github.unaimillan;

import lombok.SneakyThrows;
import ru.covariance.codeforcesapi.entities.Submission;

import java.io.File;
import java.util.List;

import static io.github.unaimillan.Main.*;

public class DownloadStoreContestStatus {

    public static void downloadStoreContestStatus() {
        downloadStoreContestStatus(contestId);
    }

    @SneakyThrows
    public static void downloadStoreContestStatus(int contestId) {
        List<Submission> submissions = cfapi.contestStatus(contestId, null, null, null);

        jsonMapper.writeValue(new File("data/contest-status.json"), submissions);

        System.out.printf("Downloaded %d submissions and saved them%n", submissions.size());
    }

    public static void main(String[] args) {
        downloadStoreContestStatus();
    }
}

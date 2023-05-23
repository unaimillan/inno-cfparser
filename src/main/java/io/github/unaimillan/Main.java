package io.github.unaimillan;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.unaimillan.innocfparser.Config;
import io.github.unaimillan.innocfparser.enums.ParticipantType;
import io.github.unaimillan.innocfparser.types.StudentSolution;
import io.github.unaimillan.innocfparser.types.StudentSubmission;
import io.github.unaimillan.innocfparser.types.helpers.CSVStudent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.covariance.codeforcesapi.CodeforcesApi;
import ru.covariance.codeforcesapi.entities.Member;
import ru.covariance.codeforcesapi.entities.Submission;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class Main {

    public static final CodeforcesApi cfapi = new CodeforcesApi(Config.CF_KEY, Config.CF_SECRET);

    public static final ObjectMapper jsonMapper = new ObjectMapper();

    public static final CsvMapper csvMapper = CsvMapper.csvBuilder()
            .addModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    public static final int contestId = Config.CONTEST_ID;
    public static Map<Integer, String> sourceFilesIndex;

    @SneakyThrows
    public static void main(String[] args) {
        try (Stream<Path> stream = Files.list(Path.of("data", String.valueOf(contestId)))) {
            sourceFilesIndex = stream.collect(Collectors.toMap(
                    filename -> Integer.valueOf(filename.getFileName().toString().replaceFirst("[.][^.]+$", "")), path -> path.getFileName().toString()
            ));
            log.info("Build index for {} source submission files", sourceFilesIndex.size());
        }

        List<Submission> submissions = jsonMapper.readValue(new File("data/contest-status.json"), new TypeReference<List<Submission>>() {
        });

        // Calculate some useful statistics about all participants
        Map<ParticipantType, HashSet<Member>> participantStatistics = submissions.stream().map(s -> new AbstractMap.SimpleEntry<>(s.getAuthor().getParticipantType(), s.getAuthor().getMembers().get(0)))
                .collect(Collectors.toMap(k -> ParticipantType.valueOf(k.getKey()), mem -> new HashSet<>(Collections.singleton(mem.getValue())),
                        (s1, s2) -> {
                            HashSet<Member> distinct = new HashSet<>();
                            distinct.addAll(s1);
                            distinct.addAll(s2);
                            return distinct;
                        }));
        log.info("Loaded {} users, including {} participants, {} virtual, {} managers",
                submissions.stream().map(s->s.getAuthor().getMembers().get(0).getHandle()).distinct().toList().size(),
                participantStatistics.get(ParticipantType.CONTESTANT).size(),
                participantStatistics.getOrDefault(ParticipantType.VIRTUAL, new HashSet<>()).size(),
                participantStatistics.getOrDefault(ParticipantType.MANAGER, new HashSet<>()).size()
        );

        List<Submission> filteredSubmissions = submissions
                .stream().filter(subm -> {
                    ParticipantType studentType = ParticipantType.valueOf(subm.getAuthor().getParticipantType());
                    //                            , ParticipantType.VIRTUAL
                    //                            , ParticipantType.MANAGER
                    return Objects.equals(ParticipantType.CONTESTANT, studentType);
                })
                .toList();

        log.info("Parsed {} submissions out of {} total", filteredSubmissions.size(), submissions.size());

        Map<Member, List<Submission>> submissionsPerUser = new HashMap<>();
        for (Submission subm : filteredSubmissions) {
            Member mem = subm.getAuthor().getMembers().get(0);

            if (submissionsPerUser.containsKey(mem)) {
                submissionsPerUser.get(mem).add(subm);
            } else {
                submissionsPerUser.put(mem, new ArrayList<>(List.of(subm)));
            }
        }
        submissionsPerUser.forEach((mem, subs) -> subs.sort(Comparator.comparingInt(Submission::getCreationTimeSeconds)));

        log.info("Parsed {} users (after filtering)", submissionsPerUser.size());

        Map<Member, StudentSubmission> studentFinalSubmissions = submissionsPerUser.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entr -> {
                    List<Submission> lst = entr.getValue();
                    return StudentSubmission.of(lst.get(lst.size() - 1));
                }));

        Map<Member, StudentSolution> studentSolutions = submissionsPerUser.keySet().stream()
                .collect(Collectors.toMap(Function.identity(),
                        mem -> new StudentSolution(studentFinalSubmissions.get(mem), submissionsPerUser.get(mem).size())));

        studentSolutions.forEach((mem, sol) -> sol.moveSourceCode(
                Path.of("data", String.valueOf(contestId)),
                Path.of("results", "solutions")));

        CsvSchema schema = csvMapper.schemaFor(CSVStudent.class).withHeader();

        csvMapper.writer(schema).writeValue(new File("results/results.csv"), studentSolutions.values());

        log.info("All done");
    }
}

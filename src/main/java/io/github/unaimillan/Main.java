package io.github.unaimillan;

import io.github.unaimillan.innocfparser.Config;
import ru.covariance.codeforcesapi.CodeforcesApi;

public class Main {

    public static CodeforcesApi cfapi = new CodeforcesApi(Config.CF_KEY, Config.CF_SECRET);

    public static void main(String[] args) {

        System.out.println("Hello world!");
    }
}
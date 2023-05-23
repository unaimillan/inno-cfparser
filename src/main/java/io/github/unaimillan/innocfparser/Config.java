package io.github.unaimillan.innocfparser;

import io.github.cdimascio.dotenv.Dotenv;

public final class Config {

    private static final Dotenv dotenv = Dotenv.load();

    public static final String CF_KEY = dotenv.get("CF_KEY", null);
    public static final String CF_SECRET = dotenv.get("CF_SECRET", null);

    public static final int CONTEST_ID = Integer.parseInt(dotenv.get("CONTEST_ID", "-1"));
}

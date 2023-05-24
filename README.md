# Inno CF Parser

## Usage

1. Create `.env` file and put [codeforces API](https://codeforces.com/settings/api) credentials there (use `.env.example` as an example)
2. Setup proper `contestId` in `Main` class (or inside `.env` file)
3. Download all student submissions meta data using `DownloadStoreContestStatus` (stores result inside `data` folder)
4. Put source codes of all student submissions into the `data` folder
5. Play with `Main` class to achieve the required properties and compose the result

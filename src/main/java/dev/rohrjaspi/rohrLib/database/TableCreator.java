package dev.rohrjaspi.rohrlib.database;

public class TableCreator {

	private final HikariDatabaseManager databaseManager;

	public TableCreator(HikariDatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

	public void createTables(String... sqlStatements) {
		for (String sql : sqlStatements) {
			databaseManager.executeSync(sql);
		}
	}
}
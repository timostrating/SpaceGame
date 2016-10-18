package spacegame;

public class Level {
	public static final int MAX_LEVEL = 10;
	public static final int LEVEL_ENEMIES = 4;
	
	public static int numberOfEnemies = LEVEL_ENEMIES;
	public static int currentLevel = 1;
	public static int levelCompletionScore = (int) (100.0 + Math.pow(10.0, (double)currentLevel));
	public static int killScore = 5;
	public static int totalScore = 0;
	public static int levelScore = 0;
	
	public static void nextLevel() {
		if (currentLevel < MAX_LEVEL) {
			currentLevel++;
			
			//totalScore += levelCompletionScore;
			numberOfEnemies = (int)(((double)currentLevel / 2.0) * (double)LEVEL_ENEMIES);
			killScore = numberOfEnemies * 10;
			levelCompletionScore = (int) (100.0 + Math.pow(10.0, (double)currentLevel));
			//totalScore += levelScore;
			levelScore = 0;
		}
	}
	
	public static int getMaxLevelScore() {
		return levelScore + (killScore * numberOfEnemies);
	}
}

package com.food_factory_spring.common;

public class StringSimilarity {

    // 计算Levenshtein距离
    public static int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= b.length(); j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                    Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                );
            }
        }
        return dp[a.length()][b.length()];
    }

    // 计算相似度得分（0~1）
    public static double similarity(String a, String b) {
        if (a.isEmpty() && b.isEmpty()) return 1.0;
        int maxLength = Math.max(a.length(), b.length());
        int distance = levenshteinDistance(a, b);
        return 1.0 - (double) distance / maxLength;
    }
}
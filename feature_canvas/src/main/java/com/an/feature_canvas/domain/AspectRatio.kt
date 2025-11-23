package com.an.feature_canvas.domain

class AspectRatio {
    companion object {
        const val RATIO_1_1 = 1f
        const val RATIO_4_3 = 4f / 3f
        const val RATIO_3_4 = 3f / 4f
        const val RATIO_3_2 = 3f / 2f
        const val RATIO_16_10 = 16f / 10f
        const val RATIO_16_9 = 16f / 9f
        const val RATIO_9_16 = 9f / 16f
        const val RATIO_2_1 = 2f / 1f
        const val RATIO_9_21 = 9f / 21f
        const val RATIO_5_4 = 5f / 4f

        val ALL_RATIOS = listOf(
            "3:2" to RATIO_3_2,
            "4:3" to RATIO_4_3,
            "1:1" to RATIO_1_1,
            "3:4" to RATIO_3_4,
            "9:16" to RATIO_9_16,
            "9:21" to RATIO_9_21,
            "5:4" to RATIO_5_4,
            "16:10" to RATIO_16_10,
            "16:9" to RATIO_16_9,
            "2:1" to RATIO_2_1,
        )
    }
}
package com.an.facefilters.canvas.presentation.util

class AspectRatio {
    companion object {
        const val RATIO_9_16 = 9f / 16f
        const val RATIO_16_9 = 16f / 9f
        const val RATIO_3_4 = 3f / 4f
        const val RATIO_4_3 = 4f / 3f
        const val RATIO_1_1 = 1f
        const val RATIO_21_9 = 21f / 9f
        const val RATIO_9_21 = 9f / 21f


        val ALL_RATIOS = listOf(
            "9:16" to RATIO_9_16,
            "16:9" to RATIO_16_9,
            "3:4" to RATIO_3_4,
            "4:3" to RATIO_4_3,
            "1:1" to RATIO_1_1,
            "21:9" to RATIO_21_9,
            "9:21" to RATIO_9_21
        )
    }
}

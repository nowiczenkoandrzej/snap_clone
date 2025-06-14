package com.an.facefilters.canvas.domain.use_cases

import com.an.facefilters.canvas.domain.model.Element

class UpdateElementsOrder {

    operator fun invoke(
        list: List<Element>,
        from: Int,
        to: Int
    ): List<Element> {
        return list
            .toMutableList()
            .apply {
                add(to, removeAt(from))
            }
            .toList()
    }
}
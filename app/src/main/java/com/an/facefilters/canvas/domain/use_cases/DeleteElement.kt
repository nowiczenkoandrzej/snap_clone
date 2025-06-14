package com.an.facefilters.canvas.domain.use_cases

import com.an.facefilters.canvas.domain.model.Element

class DeleteElement {
    operator fun invoke(
        list: List<Element>,
        index: Int?
    ): List<Element> {
        index?.let {
            return list.toMutableList()
                .apply {
                    removeAt(index)
                }
                .toList()
        } ?: return list
    }
}
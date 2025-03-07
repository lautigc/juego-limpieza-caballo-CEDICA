package com.cedica.cedica.core.utils

import androidx.annotation.DrawableRes
import androidx.compose.ui.geometry.Offset
import com.cedica.cedica.R

data class HorsePart(val name: String, val polygon: List<Pair<Float, Float>>, val zoomedPolygon: List<Pair<Float, Float>>, val margin: Pair<Float, Float>, @DrawableRes val drawableRes: Int)

val horseParts = arrayOf(
    HorsePart("Cabeza", listOf(
        0.2392857f to 0.2224719f,
        0.05f to 0.3483146f,
        0.0017857f to 0.2921348f,
        0.0107143f to 0.2179775f,
        0.0857143f to 0.0179775f,
        0.2053571f to 0.0179775f
    ), listOf(
        0.06f to 0.71f,
        0.18f to 0.91f,
        0.39f to 0.76f,
        0.66f to 0.71f,
        0.95f to 0.99f,
        0.97f to 0.45f,
        0.43f to 0.08f,
        0.45f to 0.33f,
        0.24f to 0.56f
    ), 50f to 50f,
        R.drawable.caballo_cabeza),
    HorsePart(name = "Cuerpo", listOf(
        0.6714286f to 0.258427f,
        0.6928571f to 0.6044944f,
        0.5125f to 0.6426966f,
        0.4767857f to 0.611236f,
        0.4821429f to 0.4719101f,
        0.4f to 0.2674157f
    ), listOf(
        0.22f to 0.12f,
        0.33f to 0.67f,
        0.67f to 0.62f,
        0.71f to 0.09f,
        0.48f to 0.15f
    ), 70f to 100f,
        R.drawable.caballo_cuerpo),
    HorsePart("Cola", listOf(
        0.9767857f to 0.2831461f,
        0.9946429f to 0.4426966f,
        0.9821429f to 0.8f,
        0.9178571f to 0.8044944f,
        0.8303571f to 0.3011236f
    ), listOf(
        0.35f to 0.13f,
        0.52f to 0.27f,
        0.49f to 0.38f,
        0.55f to 0.62f,
        0.63f to 0.74f,
        0.76f to 0.81f,
        0.78f to 0.95f,
        0.93f to 0.80f,
        0.90f to 0.67f,
        0.72f to 0.51f,
        0.67f to 0.31f,
        0.56f to 0.20f
    ), 60f to 50f,
        R.drawable.caballo_cola),
    HorsePart("Pierna izquierda", listOf(
        0.4553571f to 0.5955056f,
        0.4928571f to 0.7820225f,
        0.4410714f to 0.9910112f,
        0.3410714f to 0.9842697f,
        0.3785714f to 0.4359551f
    ), listOf(
        0.48f to 0.08f,
        0.37f to 0.19f,
        0.48f to 0.57f,
        0.52f to 0.79f,
        0.58f to 0.62f,
        0.58f to 0.46f,
        0.69f to 0.23f,
        0.66f to 0.13f
    ), 100f to 50f,
        R.drawable.caballo_pierna_izquierda),
    HorsePart("Pierna derecha", listOf(
        0.8285714f to 0.3842697f,
        0.8785714f to 0.6898876f,
        0.8785714f to 0.7617978f,
        0.8696429f to 0.9775281f,
        0.7464286f to 0.988764f,
        0.7017857f to 0.4292135f,
        0.7482143f to 0.3280899f
    ), listOf(
        0.43f to 0.06f,
        0.28f to 0.10f,
        0.21f to 0.21f,
        0.25f to 0.36f,
        0.59f to 0.58f,
        0.67f to 0.66f,
        0.62f to 0.80f,
        0.74f to 0.65f,
        0.69f to 0.56f,
        0.67f to 0.27f,
        0.68f to 0.18f,
        0.63f to 0.11f
    ), 100f to 50f,
        R.drawable.caballo_pierna_derecha),
    HorsePart("Cuello", listOf(
        0.3303571f to 0.5011236f,
        0.2017857f to 0.2921348f,
        0.2732143f to 0.1820225f,
        0.3803571f to 0.2921348f
    ), listOf(
        0.19f to 0.39f,
        0.43f to 0.59f,
        0.73f to 0.92f,
        0.83f to 0.67f,
        0.80f to 0.54f,
        0.64f to 0.53f,
        0.29f to 0.30f
    ), 70f to 50f,
        R.drawable.caballo_cuello)

)

enum class DrawableParts(@DrawableRes val imageRes: Int) {
    CABEZA(R.drawable.caballo_cabeza),
    CUERPO(R.drawable.caballo_cuerpo),
    COLA(R.drawable.caballo_cola),
    PIERNA_IZQUIERDA(R.drawable.caballo_pierna_izquierda),
    PIERNA_DERECHA(R.drawable.caballo_pierna_derecha),
    CUELLO(R.drawable.caballo_cuello)
}

// TODO: podria aplicar enums en horseParts y en stages al igual que en DrawableParts

data class Stage(
    val stageNumber: Int,
    val horsePart: String,
    val tool: String,
)
val stages = arrayOf(
    Stage(1, "Cabeza", "Rasqueta blanda"),
    Stage(2, "Cuerpo", "Cepillo blando"),
    Stage(3, "Cola", "Cepillo duro"),
    Stage(4, "Pierna izquierda", "Rasqueta blanda"),
    Stage(5, "Pierna derecha", "Rasqueta blanda"),
    Stage(6, "Cuello", "Rasqueta dura"),
)

data class StageInfo(val correctHorsePart: HorsePart, val incorrectRandomHorseParts: Array<HorsePart>, val tool: String)

fun getStageInfo(stageNumber: Int, numRandomParts: Int = 2): StageInfo? {
    /**
    * Devuelve que parte y herramienta son las correctas
    * además de partes incorrectas para desafiar.
    * @stageNumber Es el numero de etapa
    * @numRandomParts Es la cantidad de partes random se quiere, default 2
    *
    * */
    val stage = stages.find { it.stageNumber == stageNumber } ?: return null
    val correctHorsePart = smoothedHorseParts.find { it.name == stage.horsePart } ?: return null
    val horseParts =  selectRandomParts(numRandomParts, exceptPart = correctHorsePart)

    return StageInfo(correctHorsePart, horseParts, stage.tool)
}

fun selectRandomParts(n: Int, exceptPart: HorsePart?): Array<HorsePart> {
    return smoothedHorseParts.shuffled().filter { h -> h != exceptPart }.take(n).toTypedArray()
}

val smoothedHorseParts = horseParts.map { part ->
    part.copy(polygon = smoothPolygon(part.polygon, iterations = 2))
}

fun isPointInPolygon(x: Float, y: Float, polygon: List<Pair<Float, Float>>): Boolean {
    var intersectCount = 0
    for (i in polygon.indices) {
        val j = (i + 1) % polygon.size
        val (xi, yi) = polygon[i]
        val (xj, yj) = polygon[j]

        if ((yi > y) != (yj > y) &&
            (x < (xj - xi) * (y - yi) / (yj - yi) + xi)
        ) {
            intersectCount++
        }
    }
    return intersectCount % 2 == 1 // Si es impar, el punto está dentro
}

// algoritmo de chaikin
fun smoothPolygon(polygon: List<Pair<Float, Float>>, iterations: Int = 2): List<Pair<Float, Float>> {
    var smoothed = polygon.toList()
    repeat(iterations) {
        val newPoints = mutableListOf<Pair<Float, Float>>()
        for (i in smoothed.indices) {
            val p1 = smoothed[i]
            val p2 = smoothed[(i + 1) % smoothed.size]
            newPoints.add(Pair(0.75f * p1.first + 0.25f * p2.first, 0.75f * p1.second + 0.25f * p2.second))
            newPoints.add(Pair(0.25f * p1.first + 0.75f * p2.first, 0.25f * p1.second + 0.75f * p2.second))
        }
        smoothed = newPoints
    }
    return smoothed
}

// genera una lista de n manchas para la parte del caballo
fun generateStains(numRandomStains: Int, width: Int, height: Int) : List<Pair<Offset, Float>> {
    return List(numRandomStains) {
        Offset(
            x = (0..width.toInt()).random().toFloat(),
            y = (0..height.toInt()).random().toFloat()
        ) to (10..50).random().toFloat() // Radio aleatorio entre 10 y 50
    }
}
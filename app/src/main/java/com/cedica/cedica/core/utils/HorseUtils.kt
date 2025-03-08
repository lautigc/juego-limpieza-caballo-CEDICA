package com.cedica.cedica.core.utils

import androidx.annotation.DrawableRes
import androidx.compose.ui.geometry.Offset
import com.cedica.cedica.R

data class HorsePart(val name: String, val polygon: List<Pair<Float, Float>>, val zoomedPolygon: List<Pair<Float, Float>>, @DrawableRes val drawableRes: Int)

val horseParts = arrayOf(
    HorsePart("Cabeza", listOf(
        0.2392857f to 0.2224719f,
        0.05f to 0.3483146f,
        0.0017857f to 0.2921348f,
        0.0107143f to 0.2179775f,
        0.0857143f to 0.0179775f,
        0.2053571f to 0.0179775f
    ), listOf(
        0.5238095f to 0.254902f,
        0.1768707f to 0.6535948f,
        0.3401361f to 0.7712418f,
        0.7414966f to 0.6666667f,
        0.7346939f to 0.4313725f
    ), R.drawable.caballo_cabeza),
    HorsePart(name = "Cuerpo", listOf(
        0.6714286f to 0.258427f,
        0.6928571f to 0.6044944f,
        0.5125f to 0.6426966f,
        0.4767857f to 0.611236f,
        0.4821429f to 0.4719101f,
        0.4f to 0.2674157f
    ), listOf(
        0.6838906f to 0.1515152f,
        0.668693f to 0.6818182f,
        0.325228f to 0.7272727f,
        0.2674772f to 0.1666667f
    ), R.drawable.caballo_cuerpo),
    HorsePart("Cola", listOf(
        0.9767857f to 0.2831461f,
        0.9946429f to 0.4426966f,
        0.9821429f to 0.8f,
        0.9178571f to 0.8044944f,
        0.8303571f to 0.3011236f
    ), listOf(
        0.6384615f to 0.2698413f,
        0.7076923f to 0.4285714f,
        0.7769231f to 0.5634921f,
        0.9384615f to 0.7261905f,
        0.7923077f to 0.8253968f,
        0.5538462f to 0.6865079f,
        0.5230769f to 0.4007937f,
        0.5076923f to 0.1825397f
    ),  R.drawable.caballo_cola),
    HorsePart("Pierna izquierda", listOf(
        0.4553571f to 0.5955056f,
        0.4928571f to 0.7820225f,
        0.4410714f to 0.9910112f,
        0.3410714f to 0.9842697f,
        0.3785714f to 0.4359551f
    ), listOf(
        0.4969325f to 0.0877193f,
        0.398773f to 0.1157895f,
        0.3680982f to 0.1859649f,
        0.4785276f to 0.5649123f,
        0.5521472f to 0.5578947f,
        0.6441718f to 0.3368421f,
        0.6932515f to 0.2315789f,
        0.6441718f to 0.1192982f
    ),  R.drawable.caballo_pierna_izquierda),
    HorsePart("Pierna derecha", listOf(
        0.8285714f to 0.3842697f,
        0.8785714f to 0.6898876f,
        0.8785714f to 0.7617978f,
        0.8696429f to 0.9775281f,
        0.7464286f to 0.988764f,
        0.7017857f to 0.4292135f,
        0.7482143f to 0.3280899f
    ), listOf(
        0.6555556f to 0.1293375f,
        0.6888889f to 0.2397476f,
        0.6555556f to 0.3312303f,
        0.6833333f to 0.5646688f,
        0.6055556f to 0.5930599f,
        0.3333333f to 0.4164038f,
        0.2166667f to 0.3028391f,
        0.2111111f to 0.1735016f,
        0.2944444f to 0.0946372f,
        0.4277778f to 0.0694006f
    ),  R.drawable.caballo_pierna_derecha),
    HorsePart("Cuello", listOf(
        0.3303571f to 0.5011236f,
        0.2017857f to 0.2921348f,
        0.2732143f to 0.1820225f,
        0.3803571f to 0.2921348f
    ), listOf(
        0.5481928f to 0.4780702f,
        0.813253f to 0.5833333f,
        0.7650602f to 0.7412281f,
        0.7650602f to 0.9254386f,
        0.1987952f to 0.377193f,
        0.2710843f to 0.245614f
    ),  R.drawable.caballo_cuello)

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
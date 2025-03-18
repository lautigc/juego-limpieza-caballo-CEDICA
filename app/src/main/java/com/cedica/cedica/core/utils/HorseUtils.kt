package com.cedica.cedica.core.utils

import androidx.annotation.DrawableRes
import androidx.compose.ui.geometry.Offset
import com.cedica.cedica.R

data class HorsePart(
    val name: String,
    val polygon: List<Pair<Float, Float>>,
    val zoomedPolygon: List<Pair<Float, Float>>,
    @DrawableRes val drawableRes: Int
)

enum class HorsePartType(val partName: String, @DrawableRes val imageRes: Int) {
    CABEZA("Cabeza", R.drawable.caballo_cabeza),
    CUELLO("Cuello", R.drawable.caballo_cuello),
    PALETA("Paleta", R.drawable.caballo_paleta), // Add proper drawable resource
    LOMO("Lomo", R.drawable.caballo_cuerpo),
    PANZA("Panza", R.drawable.caballo_cuerpo),
    ANCA("Anca", R.drawable.caballo_cuerpo),
    MANOS("Manos", R.drawable.caballo_pierna_izquierda),
    PATAS("Patas", R.drawable.caballo_pierna_derecha),
    VERIJA("Verija", R.drawable.caballo_cuerpo),
    CUERPO("Cuerpo", R.drawable.caballo_cuerpo),
    CRINES("Crines", R.drawable.caballo_crines),
    COLA("Cola", R.drawable.caballo_cola),
    VASOS("Vasos", R.drawable.caballo_vasos);

    companion object {
        fun fromName(name: String): HorsePartType? = entries.find { it.partName == name }
    }
}

enum class GroomingTool(val displayName: String) {
    RASQUETA_BLANDA("Rasqueta blanda"),
    RASQUETA_DURA("Rasqueta dura"),
    CEPILLO_BLANDO("Cepillo blando"),
    CEPILLO_DURO("Cepillo duro"),
    ESCARBA_VASOS("Escarba vasos");

    companion object {
        fun fromName(name: String): GroomingTool? = entries.find { it.displayName == name }
    }
}

data class Stage(
    val stageNumber: Int,
    val horsePartType: HorsePartType,
    val tool: GroomingTool
)

data class StageInfo(
    val correctHorsePart: HorsePart,
    val incorrectRandomHorseParts: List<HorsePart>,
    val tool: GroomingTool
)

val horseParts = listOf(
    HorsePart(
        HorsePartType.CABEZA.partName,
        listOf(
            0.2392857f to 0.2224719f,
            0.05f to 0.3483146f,
            0.0017857f to 0.2921348f,
            0.0107143f to 0.2179775f,
            0.0857143f to 0.0179775f,
            0.2053571f to 0.0179775f
        ),
        listOf(
            0.5238095f to 0.254902f,
            0.1768707f to 0.6535948f,
            0.3401361f to 0.7712418f,
            0.7414966f to 0.6666667f,
            0.7346939f to 0.4313725f
        ),
        HorsePartType.CABEZA.imageRes
    ),
    HorsePart(
        HorsePartType.CUERPO.partName,
        listOf(
            0.6714286f to 0.258427f,
            0.6928571f to 0.6044944f,
            0.5125f to 0.6426966f,
            0.4767857f to 0.611236f,
            0.4821429f to 0.4719101f,
            0.4f to 0.2674157f
        ),
        listOf(
            0.6838906f to 0.1515152f,
            0.668693f to 0.6818182f,
            0.325228f to 0.7272727f,
            0.2674772f to 0.1666667f
        ),
        HorsePartType.CUERPO.imageRes
    ),
    HorsePart(
        HorsePartType.COLA.partName,
        listOf(
            0.9767857f to 0.2831461f,
            0.9946429f to 0.4426966f,
            0.9821429f to 0.8f,
            0.9178571f to 0.8044944f,
            0.8303571f to 0.3011236f
        ),
        listOf(
            0.6384615f to 0.2698413f,
            0.7076923f to 0.4285714f,
            0.7769231f to 0.5634921f,
            0.9384615f to 0.7261905f,
            0.7923077f to 0.8253968f,
            0.5538462f to 0.6865079f,
            0.5230769f to 0.4007937f,
            0.5076923f to 0.1825397f
        ),
        HorsePartType.COLA.imageRes
    ),
    HorsePart(
        HorsePartType.MANOS.partName,
        listOf(
            0.4553571f to 0.5955056f,
            0.4928571f to 0.7820225f,
            0.4410714f to 0.9910112f,
            0.3410714f to 0.9842697f,
            0.3785714f to 0.4359551f
        ),
        listOf(
            0.4969325f to 0.0877193f,
            0.398773f to 0.1157895f,
            0.3680982f to 0.1859649f,
            0.4785276f to 0.5649123f,
            0.5521472f to 0.5578947f,
            0.6441718f to 0.3368421f,
            0.6932515f to 0.2315789f,
            0.6441718f to 0.1192982f
        ),
        HorsePartType.MANOS.imageRes
    ),
    HorsePart(
        HorsePartType.PATAS.partName,
        listOf(
            0.8285714f to 0.3842697f,
            0.8785714f to 0.6898876f,
            0.8785714f to 0.7617978f,
            0.8696429f to 0.9775281f,
            0.7464286f to 0.988764f,
            0.7017857f to 0.4292135f,
            0.7482143f to 0.3280899f
        ),
        listOf(
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
        ),
        HorsePartType.PATAS.imageRes
    ),
    HorsePart(
        HorsePartType.CUELLO.partName,
        listOf(
            0.3303571f to 0.5011236f,
            0.2017857f to 0.2921348f,
            0.2732143f to 0.1820225f,
            0.3803571f to 0.2921348f
        ),
        listOf(
            0.5481928f to 0.4780702f,
            0.813253f to 0.5833333f,
            0.7650602f to 0.7412281f,
            0.7650602f to 0.9254386f,
            0.1987952f to 0.377193f,
            0.2710843f to 0.245614f
        ),
        HorsePartType.CUELLO.imageRes
    ),
    HorsePart(
        HorsePartType.LOMO.partName,
        listOf(),
        listOf(),
        HorsePartType.LOMO.imageRes
    ),
    HorsePart(
        HorsePartType.PALETA.partName,
        listOf(),
        listOf(),
        HorsePartType.PALETA.imageRes
    ),
    HorsePart(
        HorsePartType.PANZA.partName,
        listOf(),
        listOf(),
        HorsePartType.PANZA.imageRes
    ),
    HorsePart(
        HorsePartType.ANCA.partName,
        listOf(),
        listOf(),
        HorsePartType.ANCA.imageRes
    ),
    HorsePart(
        HorsePartType.VERIJA.partName,
        listOf(),
        listOf(),
        HorsePartType.VERIJA.imageRes
    ),
    HorsePart(
        HorsePartType.CRINES.partName,
        listOf(),
        listOf(),
        HorsePartType.CRINES.imageRes
    ),
    HorsePart(
        HorsePartType.VASOS.partName,
        listOf(),
        listOf(),
        HorsePartType.VASOS.imageRes
    )
)

val groomingStages = listOf(
    Stage(1, HorsePartType.CABEZA, GroomingTool.RASQUETA_BLANDA),
    Stage(2, HorsePartType.CUELLO, GroomingTool.RASQUETA_DURA),
    Stage(3, HorsePartType.PALETA, GroomingTool.RASQUETA_DURA),
    Stage(4, HorsePartType.LOMO, GroomingTool.RASQUETA_DURA),
    Stage(5, HorsePartType.PANZA, GroomingTool.RASQUETA_DURA),
    Stage(6, HorsePartType.ANCA, GroomingTool.RASQUETA_DURA),
    Stage(7, HorsePartType.MANOS, GroomingTool.RASQUETA_BLANDA),
    Stage(8, HorsePartType.PATAS, GroomingTool.RASQUETA_BLANDA),
    Stage(9, HorsePartType.VERIJA, GroomingTool.CEPILLO_BLANDO),
    Stage(10, HorsePartType.CUERPO, GroomingTool.CEPILLO_BLANDO),
    Stage(11, HorsePartType.CRINES, GroomingTool.CEPILLO_DURO),
    Stage(12, HorsePartType.COLA, GroomingTool.CEPILLO_DURO),
    Stage(13, HorsePartType.VASOS, GroomingTool.ESCARBA_VASOS)
)

/**
 * Gets information for a specific stage including correct and incorrect options
 *
 * @param stageNumber The stage number to get info for
 * @param numRandomParts Number of incorrect options to include
 * @return StageInfo object or null if stage doesn't exist
 */
fun getStageInfo(stageNumber: Int, numRandomParts: Int = 2): StageInfo? {
    // Find the requested stage
    val stage = groomingStages.find { it.stageNumber == stageNumber } ?: return null

    // Find the corresponding horse part
    val correctHorsePart = horseParts.find { it.name == stage.horsePartType.partName } ?: return null

    // Get random incorrect parts
    val incorrectParts = selectRandomParts(numRandomParts, correctHorsePart)

    return StageInfo(correctHorsePart, incorrectParts, stage.tool)
}

/**
 * Selects random horse parts excluding a specific part
 *
 * @param count Number of parts to select
 * @param exceptPart Part to exclude from selection
 * @return List of randomly selected horse parts
 */
fun selectRandomParts(count: Int, exceptPart: HorsePart): List<HorsePart> {
    return horseParts
        .filter { it != exceptPart }
        .shuffled()
        .take(count)
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
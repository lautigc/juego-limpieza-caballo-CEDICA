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
        listOf(0.4178571f to 0.6404494f, 0.3714286f to 0.6314607f, 0.3428571f to 0.8404494f, 0.3482143f to 0.988764f, 0.4428571f to 0.9955056f, 0.4892857f to 0.9235955f, 0.4821429f to 0.6651685f),
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
        listOf(0.85f to 0.6f, 0.7232143f to 0.6067416f, 0.7446429f to 0.7617978f, 0.7589286f to 0.952809f, 0.7767857f to 0.9910112f, 0.8803571f to 0.9932584f, 0.9142857f to 0.8157303f, 0.9178571f to 0.7146067f),
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
        listOf(0.7589286f to 0.211236f, 0.7678571f to 0.3101124f, 0.6446429f to 0.3640449f, 0.3892857f to 0.3573034f, 0.3375f to 0.2674157f, 0.4142857f to 0.2179775f),
        listOf(0.2765957f to 0.0959596f, 0.1489362f to 0.1212121f, 0.1732523f to 0.2878788f, 0.56231f to 0.2828283f, 0.8085106f to 0.2272727f, 0.775076f to 0.0909091f, 0.4893617f to 0.1666667f),
        HorsePartType.LOMO.imageRes
    ),
    HorsePart(
        HorsePartType.PALETA.partName,
        listOf(0.2553571f to 0.3955056f, 0.3464286f to 0.3550562f, 0.3803571f to 0.4382022f, 0.3678571f to 0.5685393f, 0.2660714f to 0.4719101f),
        listOf(0.5061728f to 0.2388889f, 0.3497942f to 0.3888889f, 0.3292181f to 0.7055556f, 0.3868313f to 0.8333333f, 0.436214f to 0.9166667f, 0.4938272f to 0.9055556f, 0.6419753f to 0.6611111f, 0.6460905f to 0.5055556f, 0.5884774f to 0.2722222f),
        HorsePartType.PALETA.imageRes
    ),
    HorsePart(
        HorsePartType.PANZA.partName,
        listOf(0.5071429f to 0.4561798f, 0.4285714f to 0.5438202f, 0.4410714f to 0.6314607f, 0.5196429f to 0.6561798f, 0.6964286f to 0.6089888f, 0.7553571f to 0.552809f, 0.7339286f to 0.4426966f),
        listOf(0.2948328f to 0.540404f, 0.2857143f to 0.7828283f, 0.2644377f to 0.8434343f, 0.4012158f to 0.9040404f, 0.5531915f to 0.8181818f, 0.6930091f to 0.6818182f, 0.674772f to 0.469697f),
        HorsePartType.PANZA.imageRes
    ),
    HorsePart(
        HorsePartType.ANCA.partName,
        listOf(0.7142857f to 0.2898876f, 0.6375f to 0.3752809f, 0.6839286f to 0.5078652f, 0.7678571f to 0.5460674f, 0.8410714f to 0.447191f, 0.8232143f to 0.3235955f),
        listOf(0.7112462f to 0.1717172f, 0.5987842f to 0.4292929f, 0.6382979f to 0.6868687f, 0.9057751f to 0.5808081f, 0.8905775f to 0.2222222f),
        HorsePartType.ANCA.imageRes
    ),
    HorsePart(
        HorsePartType.VERIJA.partName,
        listOf(0.7160714f to 0.4696629f, 0.6428571f to 0.5550562f, 0.7107143f to 0.6337079f, 0.75f to 0.6696629f, 0.8035714f to 0.5685393f),
        listOf(0.6899696f to 0.6010101f, 0.8176292f to 0.8484848f, 0.8541033f to 0.9444444f, 0.7720365f to 0.9545455f, 0.6930091f to 0.7727273f, 0.662614f to 0.7272727f, 0.5866261f to 0.7828283f, 0.6018237f to 0.6212121f),
        HorsePartType.VERIJA.imageRes
    ),
    HorsePart(
        HorsePartType.CRINES.partName,
        listOf(0.1053571f to 0.058427f, 0.2392857f to 0.0898876f, 0.3767857f to 0.2629213f, 0.3857143f to 0.3460674f, 0.2803571f to 0.3393258f, 0.1928571f to 0.2022472f),
        listOf(0.3484848f to 0.1555556f, 0.4280303f to 0.4088889f, 0.6439394f to 0.68f, 0.8371212f to 0.7111111f, 0.8295455f to 0.5511111f, 0.7386364f to 0.5288889f, 0.4848485f to 0.2666667f),
        HorsePartType.CRINES.imageRes
    ),
    HorsePart(
        HorsePartType.VASOS.partName,
        listOf(0.3767857f to 0.8966292f, 0.475f to 0.9168539f, 0.4517857f to 0.9730337f, 0.4285714f to 0.988764f, 0.3535714f to 0.9842697f),
        listOf(0.145749f to 0.8545455f, 0.0283401f to 0.7181818f, 0.0445344f to 0.2409091f, 0.1659919f to 0.2090909f, 0.4817814f to 0.0818182f, 0.8380567f to 0.1772727f, 0.8461538f to 0.6909091f, 0.7975709f to 0.8909091f, 0.3036437f to 0.8545455f),
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
    val correctHorsePart = smoothedHorseParts.find { it.name == stage.horsePartType.partName } ?: return null

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
    return smoothedHorseParts
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
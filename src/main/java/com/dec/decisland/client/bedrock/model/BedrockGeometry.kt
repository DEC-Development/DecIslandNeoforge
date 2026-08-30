package com.dec.decisland.client.bedrock.model

data class BedrockVec3(
    val x: Float,
    val y: Float,
    val z: Float,
) {
    fun lerp(other: BedrockVec3, delta: Float): BedrockVec3 = BedrockVec3(
        x + (other.x - x) * delta,
        y + (other.y - y) * delta,
        z + (other.z - z) * delta,
    )

    companion object {
        val ZERO: BedrockVec3 = BedrockVec3(0.0f, 0.0f, 0.0f)
    }
}

data class BedrockLocator(
    val name: String,
    val position: BedrockVec3,
)

data class BedrockCube(
    val origin: BedrockVec3,
    val size: BedrockVec3,
    val pivot: BedrockVec3,
    val rotation: BedrockVec3,
    val uv: Pair<Int, Int>?,
    val inflate: Float,
    val mirror: Boolean,
)

data class BedrockTextureMesh(
    val texture: String,
    val position: BedrockVec3,
    val rotation: BedrockVec3,
    val localPivot: BedrockVec3,
    val scale: BedrockVec3,
)

data class BedrockBone(
    val name: String,
    val parent: String?,
    val pivot: BedrockVec3,
    val rotation: BedrockVec3,
    val cubes: List<BedrockCube>,
    val textureMeshes: List<BedrockTextureMesh>,
    val locators: Map<String, BedrockLocator>,
)

data class BedrockGeometry(
    val identifier: String,
    val textureWidth: Int,
    val textureHeight: Int,
    val bones: List<BedrockBone>,
) {
    val bonesByName: Map<String, BedrockBone> = bones.associateBy(BedrockBone::name)
    val childBonesByParent: Map<String, List<BedrockBone>> = bones
        .filter { it.parent != null }
        .groupBy { it.parent!! }

    val rootBones: List<BedrockBone> = bones.filter { it.parent == null }
}

package com.dec.decisland.entity.projectile.dart

import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import java.util.function.Supplier

class DartDefinition(
    val path: String,
    val entitySettings: DartSettings,
    val itemSettings: com.dec.decisland.item.custom.dart.DartItemSettings,
    val onHitEntityServer: DartEntity.(EntityHitResult) -> Unit = {},
    val onHitBlockServer: DartEntity.(BlockHitResult) -> Unit = {},
    val onHitServer: DartEntity.(HitResult) -> Unit = {},
) {
    private var itemSupplier: Supplier<Item>? = null
    private var entityTypeSupplier: Supplier<EntityType<DartEntity>>? = null

    fun bindItemSupplier(supplier: Supplier<Item>) {
        itemSupplier = supplier
    }

    fun bindEntityTypeSupplier(supplier: Supplier<EntityType<DartEntity>>) {
        entityTypeSupplier = supplier
    }

    fun item(): Item =
        requireNotNull(itemSupplier) { "Dart item supplier not bound for $path" }.get()

    fun entityType(): EntityType<DartEntity> =
        requireNotNull(entityTypeSupplier) { "Dart entity type supplier not bound for $path" }.get()
}

package com.dec.decisland.lang.item

import com.dec.decisland.lang.item.en_us.Accessory
import com.dec.decisland.lang.item.en_us.Armor
import com.dec.decisland.lang.item.en_us.Mask
import com.dec.decisland.lang.item.en_us.Weapon
import com.dec.decisland.lang.item.zh_cn.Accessory as ZhCnAccessory
import com.dec.decisland.lang.item.zh_cn.Armor as ZhCnArmor
import com.dec.decisland.lang.item.zh_cn.Mask as ZhCnMask
import com.dec.decisland.lang.item.zh_cn.Weapon as ZhCnWeapon

object ItemLang {
    private val enUs = Accessory.translations + Armor.translations + Mask.translations + Weapon.translations
    private val zhCn = ZhCnAccessory.translations + ZhCnArmor.translations + ZhCnMask.translations + ZhCnWeapon.translations

    private val translationsById: Map<String, Map<String, String>> =
        (enUs.keys + zhCn.keys).associateWith { name ->
            mutableMapOf<String, String>().apply {
                enUs[name]?.let { put("en_us", it) }
                zhCn[name]?.let { put("zh_cn", it) }
            }
        }

    fun get(name: String): Map<String, String> =
        translationsById[name] ?: error("Missing item translations for $name")
}
